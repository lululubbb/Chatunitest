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
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_initializeHeader_Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        // We create a partial mock of CSVParser because nextRecord() is called inside initializeHeader()
        parser = Mockito.spy(new CSVParser(new java.io.StringReader(""), formatMock));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);

        assertNull(result);
        verify(formatMock).getHeader();
        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        doReturn(null).when(parser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordHasValues() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[]{"col1", "col2"});
        doReturn(recordMock).when(parser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("col1"));
        assertEquals(Integer.valueOf(1), result.get("col2"));
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderTrue() throws Exception {
        String[] header = new String[]{"A", "B", "C"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        doReturn(mock(CSVRecord.class)).when(parser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("A"));
        assertEquals(Integer.valueOf(1), result.get("B"));
        assertEquals(Integer.valueOf(2), result.get("C"));
        verify(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderFalse() throws Exception {
        String[] header = new String[]{"X", "Y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("X"));
        assertEquals(Integer.valueOf(1), result.get("Y"));
        verify(parser, never()).nextRecord();
    }
}