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
import org.mockito.Mockito;

class CSVParser_16_5Test {

    private CSVParser csvParser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        // Create a CSVParser instance with a Reader and the mocked format.
        // Using a StringReader with empty content because nextRecord will be mocked.
        csvParser = new CSVParser(new java.io.StringReader(""), formatMock);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNull() throws Throwable {
        when(formatMock.getHeader()).thenReturn(null);

        Map<String, Integer> result = invokeInitializeHeader(csvParser);

        assertNull(result, "Header map should be null when format header is null");
        verify(formatMock).getHeader();
        verifyNoMoreInteractions(formatMock);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordReturnsNull() throws Throwable {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord recordMock = null;
        CSVParser spyParser = Mockito.spy(csvParser);
        doReturn(recordMock).when(spyParser).nextRecord();

        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNull(result, "Header map should be null when header is empty and nextRecord returns null");
        verify(formatMock).getHeader();
        verify(spyParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordReturnsValues() throws Throwable {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] {"A", "B", "C"});

        CSVParser spyParser = Mockito.spy(csvParser);
        doReturn(recordMock).when(spyParser).nextRecord();

        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get("A"));
        assertEquals(1, result.get("B"));
        assertEquals(2, result.get("C"));

        verify(formatMock).getHeader();
        verify(spyParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordFalse() throws Throwable {
        String[] header = new String[] {"X", "Y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        CSVParser spyParser = Mockito.spy(csvParser);
        doNothing().when(spyParser).nextRecord(); // Should not be called

        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(2, result.size());
        assertEquals(0, result.get("X"));
        assertEquals(1, result.get("Y"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(formatMock).getIgnoreHeaderCase();
        verify(formatMock).getAllowMissingColumnNames();
        verify(spyParser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordTrue() throws Throwable {
        String[] header = new String[] {"X", "Y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        CSVParser spyParser = Mockito.spy(csvParser);
        doReturn(mock(CSVRecord.class)).when(spyParser).nextRecord();

        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(2, result.size());
        assertEquals(0, result.get("X"));
        assertEquals(1, result.get("Y"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(formatMock).getIgnoreHeaderCase();
        verify(formatMock).getAllowMissingColumnNames();
        verify(spyParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_ignoreHeaderCaseTrue() throws Throwable {
        String[] header = new String[] {"A", "b"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(true);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        CSVParser spyParser = Mockito.spy(csvParser);
        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertEquals(2, result.size());
        assertEquals(0, result.get("A"));
        assertEquals(1, result.get("b"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_duplicateHeaderThrows() throws Throwable {
        String[] header = new String[] {"dup", "dup"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        CSVParser spyParser = Mockito.spy(csvParser);
        setField(spyParser, "format", formatMock);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> invokeInitializeHeader(spyParser));
        assertTrue(thrown.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_duplicateHeaderAllowedMissingColumnNames() throws Throwable {
        String[] header = new String[] {"dup", "dup"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(true);

        CSVParser spyParser = Mockito.spy(csvParser);
        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get("dup"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_emptyHeaderAllowedMissingColumnNames() throws Throwable {
        String[] header = new String[] {"", " "};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(true);

        CSVParser spyParser = Mockito.spy(csvParser);
        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0, result.get(""));
        assertEquals(1, result.get(" "));
    }

    // Helper method to invoke private initializeHeader via reflection
    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws Throwable {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);
            return result;
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // Helper method to set private final field via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = CSVParser.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}