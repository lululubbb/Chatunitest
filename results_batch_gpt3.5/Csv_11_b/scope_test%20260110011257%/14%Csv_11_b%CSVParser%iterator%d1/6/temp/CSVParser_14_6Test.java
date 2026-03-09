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
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserIteratorTest {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws IOException {
        parser = spy(new CSVParser(mock(java.io.Reader.class), mock(CSVFormat.class)));
    }

    @Test
    @Timeout(8000)
    void testHasNextWhenClosed() {
        doReturn(true).when(parser).isClosed();
        Iterator<CSVRecord> iterator = parser.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    void testHasNextWhenCurrentNotNull() throws IOException {
        doReturn(false).when(parser).isClosed();
        CSVRecord record = mock(CSVRecord.class);
        Iterator<CSVRecord> iterator = parser.iterator();

        // set current to non-null via reflection
        setCurrent(iterator, record);

        assertTrue(iterator.hasNext());
        // current should remain the same
        assertSame(record, getCurrent(iterator));
    }

    @Test
    @Timeout(8000)
    void testHasNextWhenCurrentNullAndNextRecordReturnsRecord() throws IOException {
        doReturn(false).when(parser).isClosed();
        CSVRecord record = mock(CSVRecord.class);
        doReturn(record).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        // current initially null
        assertTrue(iterator.hasNext());
        // current should be set to record
        assertSame(record, getCurrent(iterator));
    }

    @Test
    @Timeout(8000)
    void testHasNextWhenCurrentNullAndNextRecordReturnsNull() throws IOException {
        doReturn(false).when(parser).isClosed();
        doReturn(null).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertFalse(iterator.hasNext());
        assertNull(getCurrent(iterator));
    }

    @Test
    @Timeout(8000)
    void testHasNextWhenNextRecordThrowsIOException() throws IOException {
        doReturn(false).when(parser).isClosed();
        doThrow(new IOException("io error")).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException thrown = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(thrown.getCause() instanceof IOException);
    }

    @Test
    @Timeout(8000)
    void testNextWhenClosed() {
        doReturn(true).when(parser).isClosed();
        Iterator<CSVRecord> iterator = parser.iterator();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("CSVParser has been closed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testNextWhenCurrentNotNull() throws IOException {
        doReturn(false).when(parser).isClosed();
        CSVRecord record = mock(CSVRecord.class);
        Iterator<CSVRecord> iterator = parser.iterator();

        setCurrent(iterator, record);

        CSVRecord result = iterator.next();
        assertSame(record, result);
        assertNull(getCurrent(iterator));
    }

    @Test
    @Timeout(8000)
    void testNextWhenCurrentNullAndNextRecordReturnsRecord() throws IOException {
        doReturn(false).when(parser).isClosed();
        CSVRecord record = mock(CSVRecord.class);
        doReturn(record).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        CSVRecord result = iterator.next();
        assertSame(record, result);
        assertNull(getCurrent(iterator));
    }

    @Test
    @Timeout(8000)
    void testNextWhenCurrentNullAndNextRecordReturnsNull() throws IOException {
        doReturn(false).when(parser).isClosed();
        doReturn(null).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testNextWhenNextRecordThrowsIOException() throws IOException {
        doReturn(false).when(parser).isClosed();
        doThrow(new IOException("io error")).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException thrown = assertThrows(RuntimeException.class, iterator::next);
        assertTrue(thrown.getCause() instanceof IOException);
    }

    @Test
    @Timeout(8000)
    void testRemoveThrowsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    // Reflection helpers to access private 'current' field inside anonymous Iterator class
    private void setCurrent(Iterator<CSVRecord> iterator, CSVRecord record) {
        try {
            Field field = findField(iterator.getClass(), "current");
            field.setAccessible(true);
            field.set(iterator, record);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CSVRecord getCurrent(Iterator<CSVRecord> iterator) {
        try {
            Field field = findField(iterator.getClass(), "current");
            field.setAccessible(true);
            return (CSVRecord) field.get(iterator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}