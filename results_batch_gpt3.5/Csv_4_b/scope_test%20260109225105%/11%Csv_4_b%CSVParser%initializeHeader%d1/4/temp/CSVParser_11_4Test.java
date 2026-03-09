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

class CSVParser_11_4Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        // Create a CSVParser instance with a Reader and the mocked format
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_recordNotNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] { "col1", "col2" });

        CSVParser spyParser = spy(parser);
        doReturn(recordMock).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> hdrMap = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(hdrMap);
        assertEquals(2, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("col1"));
        assertEquals(Integer.valueOf(1), hdrMap.get("col2"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_recordNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVParser spyParser = spy(parser);
        doReturn(null).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> hdrMap = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(hdrMap);
        assertTrue(hdrMap.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordTrue() throws Exception {
        String[] header = new String[] { "a", "b" };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        CSVParser spyParser = spy(parser);
        doReturn(mock(CSVRecord.class)).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> hdrMap = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(hdrMap);
        assertEquals(2, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("a"));
        assertEquals(Integer.valueOf(1), hdrMap.get("b"));

        verify(spyParser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordFalse() throws Exception {
        String[] header = new String[] { "x", "y", "z" };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVParser spyParser = spy(parser);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> hdrMap = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(hdrMap);
        assertEquals(3, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("x"));
        assertEquals(Integer.valueOf(1), hdrMap.get("y"));
        assertEquals(Integer.valueOf(2), hdrMap.get("z"));

        verify(spyParser, never()).nextRecord();
    }
}