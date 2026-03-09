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
import java.util.ArrayList;
import java.util.Collection;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParserInitializeHeaderTest {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        // We need to create a CSVParser instance with a Reader and format
        // Reader can be null since we won't use it directly in initializeHeader
        parser = Mockito.spy(new CSVParser(null, formatMock));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullFormatHeader() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNull(result);
        verify(formatMock).getHeader();
        verifyNoMoreInteractions(formatMock);
        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_NextRecordNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNull(result);
        verify(formatMock).getHeader();
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_NextRecordNonNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] {"a", "b", "c"});
        doReturn(recordMock).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));
        assertEquals(Integer.valueOf(2), result.get("c"));

        verify(formatMock).getHeader();
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderTrue() throws Exception {
        String[] headers = new String[] {"x", "y"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        doReturn(mock(CSVRecord.class)).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderFalse() throws Exception {
        String[] headers = new String[] {"col1"};
        when(formatMock.getHeader()).thenReturn(headers);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(0), result.get("col1"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(parser, never()).nextRecord();
    }

    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);
        return result;
    }
}