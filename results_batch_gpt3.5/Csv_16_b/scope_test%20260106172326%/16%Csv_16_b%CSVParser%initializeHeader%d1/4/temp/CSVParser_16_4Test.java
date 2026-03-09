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
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_16_4Test {

    private CSVFormat formatMock;
    private CSVParser parser;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNull() throws Throwable {
        when(formatMock.getHeader()).thenReturn(null);

        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordNotNull() throws Throwable, IOException {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);

        // Spy CSVParser to mock nextRecord()
        parser = spy(new CSVParser(new java.io.StringReader("header1,header2\nvalue1,value2"), formatMock));
        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] {"headerA", "headerB"});
        doReturn(recordMock).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0, result.get("headerA"));
        assertEquals(1, result.get("headerB"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordTrue() throws Throwable, IOException {
        String[] headers = new String[] {"h1", "h2"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(true);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));
        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertEquals(2, result.size());
        assertEquals(0, result.get("h1"));
        assertEquals(1, result.get("h2"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordFalse() throws Throwable {
        String[] headers = new String[] {"h1", "h2"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(2, result.size());
        assertEquals(0, result.get("h1"));
        assertEquals(1, result.get("h2"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_duplicateHeaderThrows() throws Throwable, IOException {
        String[] headers = new String[] {"dup", "dup"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeInitializeHeader(parser));
        assertTrue(ex.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_duplicateEmptyHeaderAllowed() throws Throwable, IOException {
        String[] headers = new String[] {"", ""};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(true);

        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(2, result.size());
        // Since keys are identical empty strings, map will only hold one entry.
        // So we verify the map size and that key "" exists.
        assertTrue(result.containsKey(""));
        assertEquals(0, result.get(""));
    }

    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws Throwable {
        try {
            Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
            method.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);
            return result;
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}