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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserIteratorTest {

    private CSVParser csvParser;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Use reflection to call the CSVParser constructor
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVParser realParser = constructor.newInstance(mock(java.io.Reader.class), mock(CSVFormat.class));
        csvParser = spy(realParser);
    }

    @Test
    @Timeout(8000)
    void iterator_hasNext_and_next_withRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1).doReturn(record2).doReturn(null).when(csvParser).nextRecord();
        doReturn(false).when(csvParser).isClosed();

        Iterator<CSVRecord> it = csvParser.iterator();

        // First hasNext() triggers nextRecord() and caches record1
        assertTrue(it.hasNext());
        // next() returns cached record1 and clears cache
        assertSame(record1, it.next());

        // Second hasNext() triggers nextRecord() and caches record2
        assertTrue(it.hasNext());
        // next() returns cached record2 and clears cache
        assertSame(record2, it.next());

        // Third hasNext() triggers nextRecord() returns null, no more records
        assertFalse(it.hasNext());

        // next() after no more records throws NoSuchElementException
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void iterator_next_without_hasNext_called() throws IOException {
        CSVRecord record = mock(CSVRecord.class);

        doReturn(record).when(csvParser).nextRecord();
        doReturn(false).when(csvParser).isClosed();

        Iterator<CSVRecord> it = csvParser.iterator();

        // Call next() directly without hasNext()
        assertSame(record, it.next());
    }

    @Test
    @Timeout(8000)
    void iterator_hasNext_whenParserClosed() {
        doReturn(true).when(csvParser).isClosed();

        Iterator<CSVRecord> it = csvParser.iterator();

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void iterator_next_throwsIllegalStateException_onIOException() throws IOException {
        doThrow(new IOException("io error")).when(csvParser).nextRecord();
        doReturn(false).when(csvParser).isClosed();

        Iterator<CSVRecord> it = csvParser.iterator();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, it::hasNext);
        assertTrue(thrown.getMessage().contains("IOException reading next record"));

        // next() also throws IllegalStateException when getNextRecord() throws IOException
        IllegalStateException thrownNext = assertThrows(IllegalStateException.class, it::next);
        assertTrue(thrownNext.getMessage().contains("IOException reading next record"));
    }

    @Test
    @Timeout(8000)
    void iterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> it = csvParser.iterator();

        assertThrows(UnsupportedOperationException.class, it::remove);
    }
}