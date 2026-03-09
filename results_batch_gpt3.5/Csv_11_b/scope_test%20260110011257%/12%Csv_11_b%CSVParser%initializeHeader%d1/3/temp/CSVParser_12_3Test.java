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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserInitializeHeaderTest {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        parser = new CSVParser(null, formatMock);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullFormatHeader() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeaderWithNextRecordNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVParser spyParser = spy(parser);
        doReturn(null).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeaderWithNextRecordValues() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord nextRecordMock = mock(CSVRecord.class);
        when(nextRecordMock.values()).thenReturn(new String[] {"a", "b", "c"});

        CSVParser spyParser = spy(parser);
        doReturn(nextRecordMock).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get("a"));
        assertEquals(1, result.get("b"));
        assertEquals(2, result.get("c"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeaderSkipHeaderTrue() throws Exception {
        String[] headers = new String[] {"x", "y", "z"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        CSVParser spyParser = spy(parser);
        doReturn(mock(CSVRecord.class)).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get("x"));
        assertEquals(1, result.get("y"));
        assertEquals(2, result.get("z"));

        verify(spyParser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeaderSkipHeaderFalse() throws Exception {
        String[] headers = new String[] {"x", "y", "z"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        CSVParser spyParser = spy(parser);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get("x"));
        assertEquals(1, result.get("y"));
        assertEquals(2, result.get("z"));

        verify(spyParser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateHeaderThrows() throws Exception {
        String[] headers = new String[] {"a", "b", "a"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        CSVParser spyParser = spy(parser);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(spyParser);
        });
        assertTrue(thrown.getCause().getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeaderAllowedWhenIgnoreEmptyHeadersTrue() throws Exception {
        String[] headers = new String[] {"a", " ", "c", ""};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(true);

        CSVParser spyParser = spy(parser);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(0, result.get("a"));
        assertEquals(1, result.get(" "));
        assertEquals(2, result.get("c"));
        assertEquals(3, result.get(""));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeaderThrowsWhenIgnoreEmptyHeadersFalse() throws Exception {
        String[] headers = new String[] {"a", " ", "c", ""};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        CSVParser spyParser = spy(parser);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(spyParser);
        });
        assertTrue(thrown.getCause().getMessage().contains("duplicate name") || thrown.getCause().getMessage().contains("empty"));
    }
}