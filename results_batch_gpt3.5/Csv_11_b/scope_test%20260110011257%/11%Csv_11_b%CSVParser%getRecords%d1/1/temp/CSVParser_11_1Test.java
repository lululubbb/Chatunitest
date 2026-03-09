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
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class CSVParser_11_1Test {

    private CSVParser parser;

    private Method getRecordsMethod;

    @BeforeEach
    public void setUp() throws Exception {
        parser = Mockito.mock(CSVParser.class);

        // Use reflection to get the generic getRecords method with Collection parameter
        getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", Collection.class);
        getRecordsMethod.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    @Test
    @Timeout(8000)
    public void testGetRecords_withEmptyCollection_returnsEmptyCollection() throws Throwable {
        List<CSVRecord> input = new ArrayList<>();

        when(parser.nextRecord()).thenReturn(null);

        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, input);

        assertSame(input, result);
        assertTrue(result.isEmpty());
        verify(parser, times(1)).nextRecord();
    }

    @SuppressWarnings("unchecked")
    @Test
    @Timeout(8000)
    public void testGetRecords_withMultipleRecords_returnsAllRecords() throws Throwable {
        List<CSVRecord> input = new ArrayList<>();
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);

        when(parser.nextRecord()).thenReturn(rec1, rec2, null);

        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, input);

        assertSame(input, result);
        assertEquals(2, result.size());
        assertEquals(rec1, result.get(0));
        assertEquals(rec2, result.get(1));
        verify(parser, times(3)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_throwsIOException_propagatesException() throws Throwable {
        List<CSVRecord> input = new ArrayList<>();

        when(parser.nextRecord()).thenThrow(new IOException("IO error"));

        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                getRecordsMethod.invoke(parser, input);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // unwrap the IOException thrown by the method
                throw e.getCause();
            }
        });
        assertEquals("IO error", thrown.getMessage());
        verify(parser, times(1)).nextRecord();
    }
}