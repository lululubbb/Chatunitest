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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Method;

public class CSVParser_11_4Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() {
        parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withEmptyCollection_returnsEmpty() throws Exception {
        // Setup nextRecord to return null immediately
        when(parser.nextRecord()).thenReturn(null);

        List<CSVRecord> records = new ArrayList<>();
        List<CSVRecord> result = parser.getRecords(records);

        assertSame(records, result);
        assertTrue(result.isEmpty());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withMultipleRecords_addsAllRecords() throws Exception {
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);
        CSVRecord rec3 = mock(CSVRecord.class);

        // Setup nextRecord to return three records then null
        when(parser.nextRecord()).thenReturn(rec1, rec2, rec3, null);

        List<CSVRecord> records = new ArrayList<>();
        List<CSVRecord> result = parser.getRecords(records);

        assertSame(records, result);
        assertEquals(3, result.size());
        assertEquals(rec1, result.get(0));
        assertEquals(rec2, result.get(1));
        assertEquals(rec3, result.get(2));
        verify(parser, times(4)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withNullCollection_throwsNullPointerException() throws Exception {
        when(parser.nextRecord()).thenReturn(null);
        assertThrows(NullPointerException.class, () -> {
            parser.getRecords(null);
        });
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_throwsIOExceptionFromNextRecord() throws Exception {
        when(parser.nextRecord()).thenThrow(new IOException("IO error"));
        List<CSVRecord> records = new ArrayList<>();
        IOException thrown = assertThrows(IOException.class, () -> {
            parser.getRecords(records);
        });
        assertEquals("IO error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_reflectionInvoke() throws Exception {
        // Prepare CSVParser mock to return two records then null
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);
        when(parser.nextRecord()).thenReturn(rec1, rec2, null);

        List<CSVRecord> records = new ArrayList<>();

        Method method = CSVParser.class.getDeclaredMethod("getRecords", Collection.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) method.invoke(parser, (Collection<?>) records);

        assertSame(records, result);
        assertEquals(2, result.size());
        assertEquals(rec1, result.get(0));
        assertEquals(rec2, result.get(1));
    }
}