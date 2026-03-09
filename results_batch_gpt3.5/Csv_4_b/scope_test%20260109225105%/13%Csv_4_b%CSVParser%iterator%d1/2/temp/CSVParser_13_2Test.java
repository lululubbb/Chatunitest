package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

class CSVParserIteratorTest {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        CSVParser mockParser = mock(CSVParser.class);
        when(mockParser.isClosed()).thenReturn(false);

        // Mock nextRecord() to return two records then null
        when(mockParser.nextRecord())
                .thenReturn(mock(CSVRecord.class))
                .thenReturn(mock(CSVRecord.class))
                .thenReturn(null);

        // Spy on the mock to use real iterator() method
        parser = spy(mockParser);
        doCallRealMethod().when(parser).iterator();
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNextAndNext_normalFlow() {
        Iterator<CSVRecord> it = parser.iterator();

        assertTrue(it.hasNext(), "hasNext should be true initially");
        CSVRecord first = it.next();
        assertNotNull(first, "First record should not be null");

        assertTrue(it.hasNext(), "hasNext should be true for second record");
        CSVRecord second = it.next();
        assertNotNull(second, "Second record should not be null");

        assertFalse(it.hasNext(), "hasNext should be false after all records consumed");
        assertThrows(NoSuchElementException.class, it::next, "next() should throw after end");
    }

    @Test
    @Timeout(8000)
    void testIterator_nextWithoutHasNext() {
        Iterator<CSVRecord> it = parser.iterator();

        CSVRecord first = it.next();
        assertNotNull(first, "First record should not be null");

        CSVRecord second = it.next();
        assertNotNull(second, "Second record should not be null");

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_removeUnsupported() {
        Iterator<CSVRecord> it = parser.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    @Timeout(8000)
    void testIterator_whenParserClosed_hasNextReturnsFalse() {
        when(parser.isClosed()).thenReturn(true);
        Iterator<CSVRecord> it = parser.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIterator_whenParserClosed_nextThrows() {
        when(parser.isClosed()).thenReturn(true);
        Iterator<CSVRecord> it = parser.iterator();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_nextRecordThrowsIOException_runtimeExceptionThrown() throws Exception {
        CSVParser throwingParser = mock(CSVParser.class);
        when(throwingParser.isClosed()).thenReturn(false);
        when(throwingParser.nextRecord()).thenThrow(new IOException("IO failure"));

        CSVParser spyThrowingParser = spy(throwingParser);
        doCallRealMethod().when(spyThrowingParser).iterator();

        Iterator<CSVRecord> it = spyThrowingParser.iterator();

        Method getNextRecordMethod = it.getClass().getDeclaredMethod("getNextRecord");
        getNextRecordMethod.setAccessible(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            try {
                getNextRecordMethod.invoke(it);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // unwrap the cause
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else {
                    throw new RuntimeException(cause);
                }
            }
        });
        assertTrue(ex.getCause() instanceof IOException);
    }
}