package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParserIteratorTest {

    private CSVParser csvParser;

    private int callCount = 0;

    @BeforeEach
    public void setUp() throws Exception {
        csvParser = Mockito.spy(CSVParser.parse("", CSVFormat.DEFAULT));

        // Use reflection to set closed = false
        Field fieldClosed = CSVParser.class.getDeclaredField("closed");
        fieldClosed.setAccessible(true);
        fieldClosed.set(csvParser, false);

        // Reset callCount before mocking nextRecord
        callCount = 0;

        doAnswer(invocation -> {
            callCount++;
            if (callCount == 1) {
                return mock(CSVRecord.class);
            } else if (callCount == 2) {
                return mock(CSVRecord.class);
            } else {
                return null;
            }
        }).when(csvParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testIterator_hasNextAndNext_NormalFlow() {
        Iterator<CSVRecord> iterator = csvParser.iterator();

        // First hasNext should fetch first record
        assertTrue(iterator.hasNext());
        CSVRecord firstRecord = iterator.next();
        assertNotNull(firstRecord);

        // Second hasNext should fetch second record
        assertTrue(iterator.hasNext());
        CSVRecord secondRecord = iterator.next();
        assertNotNull(secondRecord);

        // Third hasNext should return false (no more records)
        assertFalse(iterator.hasNext());

        // next() after no more records should throw NoSuchElementException
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    public void testIterator_hasNextWhenClosed() throws Exception {
        // Close the parser via reflection
        Field fieldClosed = CSVParser.class.getDeclaredField("closed");
        fieldClosed.setAccessible(true);
        fieldClosed.set(csvParser, true);

        Iterator<CSVRecord> iterator = csvParser.iterator();

        // hasNext should return false immediately
        assertFalse(iterator.hasNext());

        // next should throw NoSuchElementException because parser is closed
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    public void testIterator_nextWithoutHasNext() {
        Iterator<CSVRecord> iterator = csvParser.iterator();

        // Call next() without calling hasNext() first
        CSVRecord record = iterator.next();
        assertNotNull(record);

        // Second next() call without hasNext(), should return second record
        CSVRecord record2 = iterator.next();
        assertNotNull(record2);

        // Third next() call without hasNext(), should throw NoSuchElementException
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    public void testIterator_removeThrowsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    public void testIterator_nextRecordThrowsIOException() throws Exception {
        CSVParser spyParser = Mockito.spy(CSVParser.parse("", CSVFormat.DEFAULT));

        // Use reflection to set closed = false on spyParser
        Field fieldClosed = CSVParser.class.getDeclaredField("closed");
        fieldClosed.setAccessible(true);
        fieldClosed.set(spyParser, false);

        // Make nextRecord throw IOException
        doThrow(new IOException("io error")).when(spyParser).nextRecord();

        Iterator<CSVRecord> iterator = spyParser.iterator();

        // hasNext should throw IllegalStateException wrapping IOException
        IllegalStateException ex = assertThrows(IllegalStateException.class, iterator::hasNext);
        assertTrue(ex.getMessage().contains("IOException"));

        // next() should also throw IllegalStateException wrapping IOException
        IllegalStateException ex2 = assertThrows(IllegalStateException.class, iterator::next);
        assertTrue(ex2.getMessage().contains("IOException"));
    }
}