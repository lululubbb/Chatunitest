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
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserIteratorTest {

    private CSVParser parser;

    private CSVRecord createCSVRecord(String[] values, long recordNumber) {
        try {
            Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                    String[].class, Map.class, String.class, long.class, long.class);
            constructor.setAccessible(true);
            // Pass null for Map<String,Integer> headerMap and String for comment
            return constructor.newInstance(values, null, null, recordNumber, -1L);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        parser = spy(new CSVParser(new java.io.StringReader("a,b\n1,2\n3,4"), CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextAndNext_withRecords() throws IOException {
        // Arrange
        doReturn(createCSVRecord(new String[]{"1", "2"}, 1L))
            .doReturn(createCSVRecord(new String[]{"3", "4"}, 2L))
            .doReturn(null)
            .when(parser).nextRecord();

        Iterator<CSVRecord> it = parser.iterator();

        // Act & Assert
        assertTrue(it.hasNext());
        CSVRecord first = it.next();
        assertNotNull(first);
        assertArrayEquals(new String[]{"1", "2"}, first.values());

        assertTrue(it.hasNext());
        CSVRecord second = it.next();
        assertNotNull(second);
        assertArrayEquals(new String[]{"3", "4"}, second.values());

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNext_whenParserClosed() throws IOException {
        // Arrange
        doReturn(true).when(parser).isClosed();
        Iterator<CSVRecord> it = parser.iterator();

        // Act & Assert
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_withoutCallingHasNext() throws IOException {
        // Arrange
        doReturn(createCSVRecord(new String[]{"1", "2"}, 1L))
            .doReturn(null)
            .when(parser).nextRecord();
        Iterator<CSVRecord> it = parser.iterator();

        // Act
        CSVRecord record = it.next();

        // Assert
        assertNotNull(record);
        assertArrayEquals(new String[]{"1", "2"}, record.values());
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_throwsIllegalStateExceptionOnIOException() throws IOException {
        // Arrange
        doThrow(new IOException("io error")).when(parser).nextRecord();
        Iterator<CSVRecord> it = parser.iterator();

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, it::hasNext);
        assertTrue(ex.getMessage().contains("IOException reading next record"));

        IllegalStateException ex2 = assertThrows(IllegalStateException.class, it::next);
        assertTrue(ex2.getMessage().contains("IOException reading next record"));
    }

    @Test
    @Timeout(8000)
    void testIteratorRemove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> it = parser.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_throwsNoSuchElementExceptionWhenClosed() {
        doReturn(true).when(parser).isClosed();
        Iterator<CSVRecord> it = parser.iterator();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_throwsNoSuchElementExceptionWhenNoMoreRecords() throws IOException {
        doReturn(null).when(parser).nextRecord();
        Iterator<CSVRecord> it = parser.iterator();

        assertFalse(it.hasNext());
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, it::next);
        assertEquals("No more CSV records available", ex.getMessage());
    }
}