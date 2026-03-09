package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserInitializeHeaderTest {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws Throwable {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        try {
            return (Map<String, Integer>) method.invoke(parser);
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullFormatHeader_ReturnsNull() throws Throwable {
        when(formatMock.getHeader()).thenReturn(null);
        parser = new CSVParser(new StringReader(""), formatMock);

        Map<String, Integer> result = invokeInitializeHeader(parser);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_ReadFromNextRecord() throws Throwable {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);

        parser = spy(new CSVParser(new StringReader(""), formatMock));
        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[]{"h1", "h2"});
        doReturn(recordMock).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("h1"));
        assertEquals(Integer.valueOf(1), result.get("h2"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderRecordTrue() throws Throwable {
        String[] headers = new String[]{"h1", "h2"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);

        parser = spy(new CSVParser(new StringReader(""), formatMock));
        doReturn(mock(CSVRecord.class)).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("h1"));
        assertEquals(Integer.valueOf(1), result.get("h2"));

        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderRecordFalse_IgnoreHeaderCaseTrue() throws Throwable {
        String[] headers = new String[]{"h1", "h2"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(true);

        parser = spy(new CSVParser(new StringReader(""), formatMock));

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("h1"));
        assertEquals(Integer.valueOf(1), result.get("h2"));

        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateHeader_ThrowsIllegalArgumentException() throws Throwable {
        String[] headers = new String[]{"h1", "h1"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        parser = spy(new CSVParser(new StringReader(""), formatMock));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeInitializeHeader(parser));
        assertTrue(ex.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateHeader_AllowMissingColumnNamesTrue_EmptyHeader() throws Throwable {
        String[] headers = new String[]{"h1", " ", null, "h1"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(true);

        parser = spy(new CSVParser(new StringReader(""), formatMock));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeInitializeHeader(parser));
        assertTrue(ex.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeadersAllowed() throws Throwable {
        String[] headers = new String[]{"h1", " ", null};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(true);

        parser = spy(new CSVParser(new StringReader(""), formatMock));

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("h1"));
        assertEquals(Integer.valueOf(1), result.get(" "));
        assertEquals(Integer.valueOf(2), result.get(null));
    }
}