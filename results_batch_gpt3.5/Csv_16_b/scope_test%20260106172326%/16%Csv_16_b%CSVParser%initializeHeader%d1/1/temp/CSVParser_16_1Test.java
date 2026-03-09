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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_16_1Test {

    private CSVParser parser;
    private CSVFormat format;

    @BeforeEach
    public void setUp() {
        format = mock(CSVFormat.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        return (Map<String, Integer>) method.invoke(parser);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNull() throws Exception {
        when(format.getHeader()).thenReturn(null);
        when(format.getIgnoreHeaderCase()).thenReturn(false);

        parser = new CSVParser(new java.io.StringReader(""), format);

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);
        assertNull(headerMap);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderEmpty_readsFirstLine() throws Exception {
        when(format.getHeader()).thenReturn(new String[0]);
        when(format.getIgnoreHeaderCase()).thenReturn(false);

        // Use a spy to override nextRecord() without subclassing final class
        parser = spy(new CSVParser(new java.io.StringReader(""), format));
        doReturn(new CSVRecord(new String[] {"col1", "col2"}, null, null, 0, 0)).when(parser).nextRecord();

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("col1"));
        assertEquals(Integer.valueOf(1), headerMap.get("col2"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordFalse() throws Exception {
        String[] headers = new String[] {"A", "B"};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(false);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getAllowMissingColumnNames()).thenReturn(false);

        parser = new CSVParser(new java.io.StringReader(""), format);

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("A"));
        assertEquals(Integer.valueOf(1), headerMap.get("B"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordTrue() throws Exception {
        String[] headers = new String[] {"A", "B"};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(false);
        when(format.getSkipHeaderRecord()).thenReturn(true);
        when(format.getAllowMissingColumnNames()).thenReturn(false);

        // Use a spy to verify nextRecord() call without subclassing final class
        parser = spy(new CSVParser(new java.io.StringReader(""), format));
        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);
        assertNotNull(headerMap);
        assertTrue(format.getSkipHeaderRecord());
        assertEquals(2, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("A"));
        assertEquals(Integer.valueOf(1), headerMap.get("B"));
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_ignoreHeaderCase_true() throws Exception {
        String[] headers = new String[] {"A", "B"};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(true);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getAllowMissingColumnNames()).thenReturn(false);

        parser = new CSVParser(new java.io.StringReader(""), format);

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);
        assertNotNull(headerMap);
        assertTrue(headerMap instanceof TreeMap);
        assertEquals(Integer.valueOf(0), headerMap.get("A"));
        assertEquals(Integer.valueOf(1), headerMap.get("B"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_duplicateHeader_throwsException() throws Exception {
        String[] headers = new String[] {"A", "A"};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(false);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getAllowMissingColumnNames()).thenReturn(false);

        parser = new CSVParser(new java.io.StringReader(""), format);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> invokeInitializeHeader(parser));
        assertTrue(thrown.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_duplicateEmptyHeaderAllowed() throws Exception {
        String[] headers = new String[] {"", ""};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(false);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getAllowMissingColumnNames()).thenReturn(true);

        parser = new CSVParser(new java.io.StringReader(""), format);

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);
        assertNotNull(headerMap);
        // Map size is 1 because keys are same empty string
        assertEquals(1, headerMap.size());
        assertEquals(Integer.valueOf(1), headerMap.get(""));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_duplicateEmptyHeaderNotAllowed_throwsException() throws Exception {
        String[] headers = new String[] {"", ""};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(false);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getAllowMissingColumnNames()).thenReturn(false);

        parser = new CSVParser(new java.io.StringReader(""), format);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> invokeInitializeHeader(parser));
        assertTrue(thrown.getMessage().contains("duplicate name"));
    }
}