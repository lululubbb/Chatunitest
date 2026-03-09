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
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        parser = spy(constructor.newInstance(mock(java.io.Reader.class), mock(CSVFormat.class)));
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNextAndNext_withRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1).doReturn(record2).doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // First hasNext() should fetch record1
        assertTrue(iterator.hasNext());
        // next() should return record1
        assertSame(record1, iterator.next());

        // Second hasNext() should fetch record2
        assertTrue(iterator.hasNext());
        // next() should return record2
        assertSame(record2, iterator.next());

        // Third hasNext() should fetch null and return false
        assertFalse(iterator.hasNext());

        // next() after no more records should throw NoSuchElementException
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_whenClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // hasNext() should return false immediately if parser is closed
        assertFalse(iterator.hasNext());

        // next() should throw NoSuchElementException with message about closed parser
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("CSVParser has been closed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_withoutCallingHasNext() throws IOException {
        CSVRecord record = mock(CSVRecord.class);
        doReturn(record).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // Directly call next() without hasNext(), should return the record
        assertSame(record, iterator.next());

        // Next call to next() with no more records should throw NoSuchElementException
        doReturn(null).when(parser).nextRecord();
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_throwsRuntimeExceptionOnIOException() throws IOException {
        doReturn(false).when(parser).isClosed();
        IOException ioException = new IOException("io error");
        doThrow(ioException).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, iterator::hasNext);
        assertSame(ioException, ex.getCause());

        ex = assertThrows(RuntimeException.class, iterator::next);
        assertSame(ioException, ex.getCause());
    }

    @Test
    @Timeout(8000)
    void testIterator_remove_throwsUnsupportedOperationException() {
        doReturn(false).when(parser).isClosed();
        // nextRecord throws IOException, so use doAnswer to avoid checked exception issue
        try {
            doAnswer(invocation -> null).when(parser).nextRecord();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
}