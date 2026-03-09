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
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_11_3Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_WithNonNullNonEmptyFormatHeader_SkipHeaderFalse() throws Exception {
        String[] header = new String[] { "col1", "col2", "col3" };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        parser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> hdrMap = (Map<String, Integer>) method.invoke(parser);

        assertNotNull(hdrMap);
        assertEquals(3, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("col1"));
        assertEquals(Integer.valueOf(1), hdrMap.get("col2"));
        assertEquals(Integer.valueOf(2), hdrMap.get("col3"));

        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_WithNonNullNonEmptyFormatHeader_SkipHeaderTrue() throws Exception {
        String[] header = new String[] { "a", "b" };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        parser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));
        CSVRecord recordMock = mock(CSVRecord.class);
        doReturn(recordMock).when(parser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> hdrMap = (Map<String, Integer>) method.invoke(parser);

        assertNotNull(hdrMap);
        assertEquals(2, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("a"));
        assertEquals(Integer.valueOf(1), hdrMap.get("b"));

        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_WithEmptyFormatHeader_ReadsFromNextRecord() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        parser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));
        CSVRecord recordMock = mock(CSVRecord.class);
        when(parser.nextRecord()).thenReturn(recordMock);
        when(recordMock.values()).thenReturn(new String[] { "x", "y" });

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> hdrMap = (Map<String, Integer>) method.invoke(parser);

        assertNotNull(hdrMap);
        assertEquals(2, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("x"));
        assertEquals(Integer.valueOf(1), hdrMap.get("y"));

        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_WithNullFormatHeader_ReturnsNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        parser = new CSVParser(mock(java.io.Reader.class), formatMock);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Object result = method.invoke(parser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NextRecordReturnsNull_EmptyHeaderMap() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        parser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));
        when(parser.nextRecord()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Object result = method.invoke(parser);

        assertNull(result);
        verify(parser).nextRecord();
    }
}