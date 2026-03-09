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
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_13_3Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Use reflection to call the CSVParser constructor with Reader and CSVFormat mocks
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        parser = spy(constructor.newInstance(mock(java.io.Reader.class), mock(CSVFormat.class)));
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_and_next_withRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1).doReturn(record2).doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // First hasNext() call returns true, caches record1
        assertTrue(iterator.hasNext());

        // next() returns record1
        assertSame(record1, iterator.next());

        // Second hasNext() call returns true, caches record2
        assertTrue(iterator.hasNext());

        // next() returns record2
        assertSame(record2, iterator.next());

        // Third hasNext() call returns false (no more records)
        assertFalse(iterator.hasNext());

        // next() after no more records throws NoSuchElementException
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_whenParserClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // hasNext() returns false if parser is closed
        assertFalse(iterator.hasNext());

        // next() throws NoSuchElementException if parser is closed
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_next_withoutCallingHasNext_firstRecordNull() throws IOException {
        doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // next() without hasNext() calls getNextRecord which returns null -> NoSuchElementException
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    void testIterator_next_throwsRuntimeException_onIOException() throws IOException {
        doThrow(new IOException("IO error")).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // hasNext() calls getNextRecord which throws IOException wrapped in RuntimeException
        RuntimeException ex = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(ex.getCause() instanceof IOException);

        // next() also calls getNextRecord and throws RuntimeException wrapping IOException
        RuntimeException ex2 = assertThrows(RuntimeException.class, iterator::next);
        assertTrue(ex2.getCause() instanceof IOException);
    }
}