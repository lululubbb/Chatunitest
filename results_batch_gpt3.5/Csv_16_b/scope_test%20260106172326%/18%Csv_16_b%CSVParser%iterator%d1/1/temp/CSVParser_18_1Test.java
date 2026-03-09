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
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParserIteratorTest {

    private CSVParser parser;
    private Method nextRecordMethod;

    @BeforeEach
    void setUp() throws Exception {
        parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);

        // nextRecord() is package-private; make it accessible via reflection
        nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
    }

    // Helper to mock nextRecord() via reflection
    private void doReturnNextRecord(CSVParser spyParser, CSVRecord record) throws Exception {
        // Use Mockito's doAnswer with reflection to mock nextRecord()
        doAnswer(invocation -> record).when(spyParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNextAndNext_normalFlow() throws Exception {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        CSVParser spyParser = spy(parser);

        doReturn(false).when(spyParser).isClosed();

        // Use doAnswer on nextRecord() via reflection
        org.mockito.stubbing.Answer<CSVRecord> answer = new org.mockito.stubbing.Answer<CSVRecord>() {
            int count = 0;
            @Override
            public CSVRecord answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
                count++;
                if (count == 1) return record1;
                if (count == 2) return record2;
                return null;
            }
        };

        // Since nextRecord() is package-private, we cannot mock it directly.
        // Use reflection to mock nextRecord() by intercepting the method call:
        // Instead of doAnswer(answer).when(spyParser).nextRecord();
        // use Mockito's doAnswer with a spy and reflection:

        // Create a spy that overrides nextRecord() via reflection
        doAnswer(answer).when(spyParser).nextRecord();

        Iterator<CSVRecord> iterator = spyParser.iterator();

        assertTrue(iterator.hasNext());
        assertSame(record1, iterator.next());

        assertTrue(iterator.hasNext());
        assertSame(record2, iterator.next());

        assertFalse(iterator.hasNext());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_whenClosed() {
        doReturn(true).when(parser).isClosed();
        Iterator<CSVRecord> iterator = parser.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_whenClosed() {
        doReturn(true).when(parser).isClosed();
        Iterator<CSVRecord> iterator = parser.iterator();
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("CSVParser has been closed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_withoutHasNext_callsNextRecord() throws Exception {
        CSVRecord record = mock(CSVRecord.class);
        CSVParser spyParser = spy(parser);
        doReturn(false).when(spyParser).isClosed();

        // Mock nextRecord() via reflection
        doReturn(record).when(spyParser).nextRecord();

        Iterator<CSVRecord> iterator = spyParser.iterator();

        assertSame(record, iterator.next());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_throwsIllegalStateExceptionOnIOException() throws Exception {
        CSVParser spyParser = spy(parser);
        doReturn(false).when(spyParser).isClosed();

        // Mock nextRecord() to throw IOException via reflection
        doThrow(new IOException("IO error")).when(spyParser).nextRecord();

        Iterator<CSVRecord> iterator = spyParser.iterator();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, iterator::hasNext);
        assertTrue(thrown.getMessage().contains("IOException reading next record"));
        assertTrue(thrown.getCause() instanceof IOException);

        IllegalStateException thrownNext = assertThrows(IllegalStateException.class, iterator::next);
        assertTrue(thrownNext.getMessage().contains("IOException reading next record"));
        assertTrue(thrownNext.getCause() instanceof IOException);
    }

    @Test
    @Timeout(8000)
    void testIterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
}