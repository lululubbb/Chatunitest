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

public class CSVParser_11_5Test {

    private CSVParser csvParser;
    private CSVFormat formatMock;

    @BeforeEach
    public void setUp() {
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_withFormatHeaderNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));

        Map<String, Integer> result = invokeInitializeHeader(csvParser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_withEmptyFormatHeader_andNextRecordNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));
        doReturn(null).when(csvParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(csvParser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_withEmptyFormatHeader_andNextRecordWithValues() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));
        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] {"a", "b", "c"});
        doReturn(recordMock).when(csvParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(csvParser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));
        assertEquals(Integer.valueOf(2), result.get("c"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_withNonEmptyFormatHeader_andSkipHeaderRecordFalse() throws Exception {
        String[] header = new String[] {"x", "y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));
        // nextRecord() should not be called, so no stubbing needed

        Map<String, Integer> result = invokeInitializeHeader(csvParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));
        verify(csvParser, times(0)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_withNonEmptyFormatHeader_andSkipHeaderRecordTrue() throws Exception {
        String[] header = new String[] {"x", "y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), formatMock));
        doReturn(mock(CSVRecord.class)).when(csvParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(csvParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));
        verify(csvParser, times(1)).nextRecord();
    }

    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);
        return result;
    }
}