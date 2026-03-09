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

public class CSVParser_12_5Test {

    private CSVParser csvParser;
    private CSVFormat mockFormat;

    @BeforeEach
    public void setUp() throws IOException {
        mockFormat = mock(CSVFormat.class);
        // Create CSVParser with mocked format
        csvParser = Mockito.spy(new CSVParser(new java.io.StringReader(""), mockFormat));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNull() throws Exception {
        when(mockFormat.getHeader()).thenReturn(null);

        Map<String, Integer> result = invokeInitializeHeader();

        assertNull(result);
        verify(mockFormat).getHeader();
        verifyNoMoreInteractions(mockFormat);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderEmpty_nextRecordNull() throws Exception {
        when(mockFormat.getHeader()).thenReturn(new String[0]);
        doReturn(null).when(csvParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader();

        assertNull(result);
        verify(mockFormat).getHeader();
        verify(csvParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderEmpty_nextRecordWithValues() throws Exception {
        when(mockFormat.getHeader()).thenReturn(new String[0]);
        CSVRecord mockRecord = mock(CSVRecord.class);
        when(mockRecord.values()).thenReturn(new String[]{"a", "b", "c"});
        doReturn(mockRecord).when(csvParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get("a"));
        assertEquals(1, result.get("b"));
        assertEquals(2, result.get("c"));
        verify(mockFormat).getHeader();
        verify(csvParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNonEmpty_skipHeaderTrue() throws Exception {
        when(mockFormat.getHeader()).thenReturn(new String[]{"x", "y", "z"});
        when(mockFormat.getSkipHeaderRecord()).thenReturn(true);
        doReturn(null).when(csvParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get("x"));
        assertEquals(1, result.get("y"));
        assertEquals(2, result.get("z"));
        verify(mockFormat).getHeader();
        verify(mockFormat).getSkipHeaderRecord();
        verify(csvParser).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNonEmpty_skipHeaderFalse() throws Exception {
        when(mockFormat.getHeader()).thenReturn(new String[]{"x", "y"});
        when(mockFormat.getSkipHeaderRecord()).thenReturn(false);

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0, result.get("x"));
        assertEquals(1, result.get("y"));
        verify(mockFormat).getHeader();
        verify(mockFormat).getSkipHeaderRecord();
        verify(csvParser, never()).nextRecord();
    }

    private Map<String, Integer> invokeInitializeHeader() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);
        return result;
    }
}