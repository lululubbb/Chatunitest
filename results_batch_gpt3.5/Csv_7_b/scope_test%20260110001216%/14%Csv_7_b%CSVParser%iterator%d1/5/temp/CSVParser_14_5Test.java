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
import java.util.Collection;
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

class CSVParserIteratorTest {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        parser = spy(constructor.newInstance(mock(java.io.Reader.class), mock(CSVFormat.class)));
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextAndNextNormalFlow() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(false).when(parser).isClosed();

        // Chain nextRecord() calls: record1, record2, null
        when(parser.nextRecord()).thenReturn(record1, record2, null);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertTrue(iterator.hasNext());
        assertSame(record1, iterator.next());

        assertTrue(iterator.hasNext());
        assertSame(record2, iterator.next());

        assertFalse(iterator.hasNext());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextWhenParserClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertFalse(iterator.hasNext());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("CSVParser has been closed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorNextWithoutHasNextCalledReturnsRecord() throws IOException {
        CSVRecord record = mock(CSVRecord.class);

        doReturn(false).when(parser).isClosed();
        when(parser.nextRecord()).thenReturn(record).thenReturn(null);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertSame(record, iterator.next());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorRemoveThrowsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    void testIteratorNextRecordThrowsIOExceptionWrappedInRuntimeException() throws IOException {
        doReturn(false).when(parser).isClosed();
        when(parser.nextRecord()).thenThrow(new IOException("io error"));

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("io error", ex.getCause().getMessage());

        ex = assertThrows(RuntimeException.class, iterator::next);
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("io error", ex.getCause().getMessage());
    }
}