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

class CSVParser_11_6Test {

    private CSVFormat formatMock;

    private CSVParser parser;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> invokeInitializeHeader(CSVParser parser)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        try {
            return (Map<String, Integer>) method.invoke(parser);
        } catch (InvocationTargetException e) {
            // unwrap IOException thrown by initializeHeader
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullFormatHeader() throws Throwable {
        when(formatMock.getHeader()).thenReturn(null);

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);

        assertNull(headerMap);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_NextRecordReturnsNull() throws Throwable {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);

        assertNull(headerMap);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_NextRecordReturnsRecord() throws Throwable {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] { "a", "b", "c" });

        doReturn(recordMock).when(parser).nextRecord();

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);

        assertNotNull(headerMap);
        assertEquals(3, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("a"));
        assertEquals(Integer.valueOf(1), headerMap.get("b"));
        assertEquals(Integer.valueOf(2), headerMap.get("c"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderRecordTrue() throws Throwable {
        String[] formatHeader = new String[] { "x", "y" };
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);

        verify(parser, times(1)).nextRecord();

        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("x"));
        assertEquals(Integer.valueOf(1), headerMap.get("y"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderRecordFalse() throws Throwable {
        String[] formatHeader = new String[] { "x", "y", "z" };
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        Map<String, Integer> headerMap = invokeInitializeHeader(parser);

        verify(parser, never()).nextRecord();

        assertNotNull(headerMap);
        assertEquals(3, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("x"));
        assertEquals(Integer.valueOf(1), headerMap.get("y"));
        assertEquals(Integer.valueOf(2), headerMap.get("z"));
    }
}