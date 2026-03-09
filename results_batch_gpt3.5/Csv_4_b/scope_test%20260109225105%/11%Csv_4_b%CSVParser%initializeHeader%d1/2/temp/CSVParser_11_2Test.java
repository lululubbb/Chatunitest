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
import org.mockito.Mockito;

class CSVParser_11_2Test {

    private CSVParser csvParser;
    private CSVFormat formatMock;
    private CSVRecord recordMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        recordMock = mock(CSVRecord.class);

        // Create a CSVParser instance with a dummy reader and mocked format
        csvParser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Spy on csvParser to mock nextRecord()
        csvParser = Mockito.spy(csvParser);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNull(result);
        verify(formatMock).getHeader();
        verify(csvParser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmptyRecordNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        doReturn(null).when(csvParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNull(result);
        verify(formatMock).getHeader();
        verify(csvParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmptyRecordHasValues() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        when(recordMock.values()).thenReturn(new String[] {"a", "b", "c"});
        doReturn(recordMock).when(csvParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));
        assertEquals(Integer.valueOf(2), result.get("c"));

        verify(formatMock).getHeader();
        verify(csvParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmptySkipHeaderTrue() throws Exception {
        String[] header = new String[] {"x", "y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        doReturn(recordMock).when(csvParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(csvParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmptySkipHeaderFalse() throws Exception {
        String[] header = new String[] {"one"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(0), result.get("one"));

        verify(formatMock).getHeader();
        verify(formatMock).getSkipHeaderRecord();
        verify(csvParser, never()).nextRecord();
    }
}