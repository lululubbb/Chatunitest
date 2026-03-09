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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_18_2Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws IOException {
        parser = spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_and_next_NormalFlow() throws IOException {
        // Mock nextRecord to return a CSVRecord first, then null
        CSVRecord record = mock(CSVRecord.class);
        doReturn(record).doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // First hasNext call triggers getNextRecord and caches record
        assertTrue(iterator.hasNext());
        // Second hasNext call returns true without calling nextRecord again
        assertTrue(iterator.hasNext());

        // next returns cached record and clears it
        assertSame(record, iterator.next());

        // After next, current is null, hasNext triggers getNextRecord again which returns null
        assertFalse(iterator.hasNext());

        // next after no more records throws NoSuchElementException
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", ex.getMessage());
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
    void testIterator_next_whenClosed_throws() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("CSVParser has been closed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_without_hasNext_calls_nextRecord() throws IOException {
        CSVRecord record = mock(CSVRecord.class);
        doReturn(record).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // current is null, so next calls getNextRecord directly
        assertSame(record, iterator.next());
    }

    @Test
    @Timeout(8000)
    void testIterator_nextRecord_throwsIOException_isWrappedInIllegalStateException() throws IOException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        IOException ioException = new IOException("io error");
        doThrow(ioException).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // Use reflection to access the private 'current' field and set it to null to force getNextRecord call
        Field currentField = iterator.getClass().getDeclaredField("current");
        currentField.setAccessible(true);
        currentField.set(iterator, null);

        // Using reflection to access private getNextRecord method
        Method getNextRecordMethod = iterator.getClass().getDeclaredMethod("getNextRecord");
        getNextRecordMethod.setAccessible(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            try {
                getNextRecordMethod.invoke(iterator);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

        assertTrue(ex.getMessage().contains("IOException reading next record"));
        assertSame(ioException, ex.getCause());
    }

    @Test
    @Timeout(8000)
    void testIterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
}