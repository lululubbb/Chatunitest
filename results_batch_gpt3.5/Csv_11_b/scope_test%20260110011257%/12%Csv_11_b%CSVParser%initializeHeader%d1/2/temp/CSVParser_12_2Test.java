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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserInitializeHeaderTest {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        parser = new CSVParser(mock(java.io.Reader.class), formatMock);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullFormatHeader_ReturnsNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_ReadsFromNextRecord() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[]{"col1", "col2"});
        CSVParser spyParser = spy(parser);
        doReturn(recordMock).when(spyParser).nextRecord();

        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0, result.get("col1"));
        assertEquals(1, result.get("col2"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_FormatHeaderNonEmpty_SkipHeaderRecordFalse() throws Exception {
        String[] headers = new String[]{"a", "b"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVParser spyParser = spy(parser);
        // nextRecord() should not be called in this case
        doReturn(null).when(spyParser).nextRecord();

        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0, result.get("a"));
        assertEquals(1, result.get("b"));
        verify(spyParser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_FormatHeaderNonEmpty_SkipHeaderRecordTrue() throws Exception {
        String[] headers = new String[]{"a", "b"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        CSVRecord dummyRecord = mock(CSVRecord.class);
        CSVParser spyParser = spy(parser);
        doReturn(dummyRecord).when(spyParser).nextRecord();

        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0, result.get("a"));
        assertEquals(1, result.get("b"));
        verify(spyParser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateHeaders_ThrowsIllegalArgumentException() throws Exception {
        String[] headers = new String[]{"a", "a"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVParser spyParser = spy(parser);
        setField(spyParser, "format", formatMock);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> invokeInitializeHeader(spyParser));
        assertTrue(thrown.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeader_IgnoreEmptyHeadersFalse_ThrowsIllegalArgumentException() throws Exception {
        String[] headers = new String[]{"", "b"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        CSVParser spyParser = spy(parser);
        setField(spyParser, "format", formatMock);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> invokeInitializeHeader(spyParser));
        assertTrue(thrown.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeader_IgnoreEmptyHeadersTrue_AllowsDuplicates() throws Exception {
        String[] headers = new String[]{"", ""};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(true);

        CSVParser spyParser = spy(parser);
        setField(spyParser, "format", formatMock);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        // Both empty headers inserted, last one overwrites first
        assertEquals(1, result.size());
        assertTrue(result.containsKey(""));
        assertEquals(1, result.get(""));
    }

    // Helper method to invoke private initializeHeader() via reflection
    private Map<String, Integer> invokeInitializeHeader(CSVParser parserInstance)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parserInstance);
        return result;
    }

    // Helper method to set private final field via reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if needed
        java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        field.set(target, value);
    }
}