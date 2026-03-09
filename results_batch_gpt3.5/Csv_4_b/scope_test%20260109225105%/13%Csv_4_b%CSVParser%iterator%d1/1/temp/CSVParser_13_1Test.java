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
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_13_1Test {

    private CSVParser parser;

    private int callCount = 0;
    private boolean closed = false;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a real CSVParser instance using reflection to call the constructor with (Reader, CSVFormat)
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        parser = constructor.newInstance((java.io.Reader) null, (CSVFormat) null);

        // Use spy on the real parser
        parser = spy(parser);

        // Override nextRecord method behavior using Mockito doAnswer
        doAnswer(invocation -> {
            callCount++;
            if (callCount == 1) {
                return mock(CSVRecord.class);
            } else if (callCount == 2) {
                return mock(CSVRecord.class);
            } else {
                return null;
            }
        }).when(parser).nextRecord();

        // Override isClosed method to use the closed field
        doAnswer(invocation -> closed).when(parser).isClosed();
    }

    private void closeParser() throws Exception {
        closed = true;
    }

    @Test
    @Timeout(8000)
    public void testHasNextReturnsTrueWhenRecordAvailable() {
        Iterator<CSVRecord> it = parser.iterator();

        assertTrue(it.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNextReturnsFalseWhenClosed() throws Exception {
        closeParser();

        Iterator<CSVRecord> it = parser.iterator();

        assertFalse(it.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testNextReturnsRecordWhenAvailable() {
        Iterator<CSVRecord> it = parser.iterator();

        CSVRecord first = it.next();
        assertNotNull(first);

        CSVRecord second = it.next();
        assertNotNull(second);
    }

    @Test
    @Timeout(8000)
    public void testNextThrowsNoSuchElementExceptionWhenNoMoreRecords() {
        Iterator<CSVRecord> it = parser.iterator();

        it.next();
        it.next();

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, it::next);
        assertEquals("No more CSV records available", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testNextThrowsNoSuchElementExceptionWhenClosed() throws Exception {
        Iterator<CSVRecord> it = parser.iterator();

        closeParser();

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, it::next);
        assertEquals("CSVParser has been closed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testRemoveThrowsUnsupportedOperationException() {
        Iterator<CSVRecord> it = parser.iterator();

        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    @Timeout(8000)
    public void testHasNextCallsNextRecordOnlyOnce() throws IOException {
        Iterator<CSVRecord> it = parser.iterator();

        assertTrue(it.hasNext());
        assertTrue(it.hasNext());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testNextCallsNextRecordIfCurrentNull() {
        Iterator<CSVRecord> it = parser.iterator();

        // call next without hasNext
        CSVRecord record = it.next();
        assertNotNull(record);
    }

    @Test
    @Timeout(8000)
    public void testNextWrapsIOExceptionInRuntimeException() throws Exception {
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVParser parserWithException = spy(constructor.newInstance((java.io.Reader) null, (CSVFormat) null));

        doAnswer(invocation -> false).when(parserWithException).isClosed();

        doThrow(new IOException("IO error")).when(parserWithException).nextRecord();

        Iterator<CSVRecord> it = parserWithException.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, it::hasNext);
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("IO error", ex.getCause().getMessage());

        RuntimeException ex2 = assertThrows(RuntimeException.class, it::next);
        assertTrue(ex2.getCause() instanceof IOException);
        assertEquals("IO error", ex2.getCause().getMessage());
    }
}