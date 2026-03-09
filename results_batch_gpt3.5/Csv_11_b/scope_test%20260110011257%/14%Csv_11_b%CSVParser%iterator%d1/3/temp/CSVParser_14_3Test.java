package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserIteratorTest {

    CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<CSVParser> ctor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        ctor.setAccessible(true);
        parser = spy(ctor.newInstance(mock(java.io.Reader.class), mock(CSVFormat.class)));
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextAndNext_NormalFlow() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1).doReturn(record2).doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> it = parser.iterator();

        assertTrue(it.hasNext());
        assertSame(record1, it.next());

        assertTrue(it.hasNext());
        assertSame(record2, it.next());

        assertFalse(it.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIteratorNextWithoutHasNext() throws IOException {
        CSVRecord record = mock(CSVRecord.class);

        doReturn(false).when(parser).isClosed();
        doReturn(record).doReturn(null).when(parser).nextRecord();

        Iterator<CSVRecord> it = parser.iterator();

        assertSame(record, it.next());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, it::next);
        assertEquals("No more CSV records available", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextWhenClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> it = parser.iterator();

        assertFalse(it.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIteratorNextWhenClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> it = parser.iterator();

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, it::next);
        assertEquals("CSVParser has been closed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorRemoveUnsupported() {
        Iterator<CSVRecord> it = parser.iterator();

        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, it::remove);
        assertNotNull(ex);
    }

    @Test
    @Timeout(8000)
    void testIteratorNextRecordThrowsIOException() throws IOException {
        doReturn(false).when(parser).isClosed();
        doThrow(new IOException("io error")).when(parser).nextRecord();

        Iterator<CSVRecord> it = parser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, it::hasNext);
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("io error", ex.getCause().getMessage());

        RuntimeException ex2 = assertThrows(RuntimeException.class, it::next);
        assertTrue(ex2.getCause() instanceof IOException);
        assertEquals("io error", ex2.getCause().getMessage());
    }
}