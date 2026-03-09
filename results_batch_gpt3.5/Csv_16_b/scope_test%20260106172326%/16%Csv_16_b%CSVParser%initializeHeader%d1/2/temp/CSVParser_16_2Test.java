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

class CSVParserInitializeHeaderTest {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        parser = Mockito.spy(new CSVParser(new java.io.StringReader(""), formatMock));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> invokeInitializeHeader() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        return (Map<String, Integer>) method.invoke(parser);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullFormatHeader_ReturnsNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        Map<String, Integer> result = invokeInitializeHeader();

        assertNull(result);
        verify(formatMock).getHeader();
        verifyNoMoreInteractions(formatMock);
        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_ReadHeaderFromFirstLine() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        CSVRecord mockRecord = mock(CSVRecord.class);
        when(mockRecord.values()).thenReturn(new String[]{"col1", "col2"});
        doReturn(mockRecord).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("col1"));
        assertEquals(Integer.valueOf(1), result.get("col2"));
        verify(formatMock).getHeader();
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_NextRecordReturnsNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader();

        assertNull(result);
        verify(formatMock).getHeader();
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderRecordTrue() throws Exception {
        String[] header = new String[]{"a", "b"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        doReturn(mock(CSVRecord.class)).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(formatMock).getIgnoreHeaderCase();
        verify(formatMock).getAllowMissingColumnNames();
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderRecordFalse_IgnoreHeaderCaseTrue() throws Exception {
        String[] header = new String[]{"A", "B"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(true);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("A"));
        assertEquals(Integer.valueOf(1), result.get("B"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(formatMock).getIgnoreHeaderCase();
        verify(formatMock).getAllowMissingColumnNames();
        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateHeader_ThrowsIllegalArgumentException() throws Exception {
        String[] header = new String[]{"dup", "dup"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeInitializeHeader());
        assertTrue(thrown.getMessage().contains("duplicate name"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(formatMock).getIgnoreHeaderCase();
        verify(formatMock).getAllowMissingColumnNames();
        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateEmptyHeaderAllowed() throws Exception {
        String[] header = new String[]{"", ""};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(true);

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(1), result.get(""));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(formatMock).getIgnoreHeaderCase();
        verify(formatMock).getAllowMissingColumnNames();
        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_HeaderContainsNull_ThrowsIllegalArgumentException() throws Exception {
        String[] header = new String[]{"valid", null, "valid2"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreHeaderCase()).thenReturn(false);
        when(formatMock.getAllowMissingColumnNames()).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeInitializeHeader());
        assertTrue(thrown.getMessage().contains("duplicate name") || thrown.getMessage().contains("null"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(formatMock).getIgnoreHeaderCase();
        verify(formatMock).getAllowMissingColumnNames();
        verify(parser, never()).nextRecord();
    }
}