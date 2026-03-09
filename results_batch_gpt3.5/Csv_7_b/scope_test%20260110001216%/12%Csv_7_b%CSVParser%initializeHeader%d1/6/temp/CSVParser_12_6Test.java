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

class CSVParserInitializeHeaderTest {

    private CSVParser parser;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws Exception {
        format = mock(CSVFormat.class);
        // Create a CSVParser instance with a dummy Reader and mocked format
        parser = new CSVParser(new java.io.StringReader(""), format);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNull() throws Throwable {
        when(format.getHeader()).thenReturn(null);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNull(result);
        verify(format).getHeader();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordNull() throws Throwable {
        when(format.getHeader()).thenReturn(new String[0]);

        CSVParser spyParser = spy(parser);
        doReturn(null).when(spyParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNull(result);
        verify(spyParser).nextRecord();
        verify(format).getHeader();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordNotNull() throws Throwable {
        when(format.getHeader()).thenReturn(new String[0]);

        CSVRecord mockRecord = mock(CSVRecord.class);
        when(mockRecord.values()).thenReturn(new String[] {"a", "b", "c"});

        CSVParser spyParser = spy(parser);
        doReturn(mockRecord).when(spyParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));
        assertEquals(Integer.valueOf(2), result.get("c"));

        verify(spyParser).nextRecord();
        verify(format).getHeader();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderTrue() throws Throwable {
        String[] header = new String[] {"x", "y"};
        when(format.getHeader()).thenReturn(header);
        when(format.getSkipHeaderRecord()).thenReturn(true);

        CSVParser spyParser = spy(parser);
        doReturn(mock(CSVRecord.class)).when(spyParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));

        verify(spyParser).nextRecord();
        verify(format).getHeader();
        verify(format).getSkipHeaderRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderFalse() throws Throwable {
        String[] header = new String[] {"col1"};
        when(format.getHeader()).thenReturn(header);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVParser spyParser = spy(parser);

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(0), result.get("col1"));

        verify(spyParser, never()).nextRecord();
        verify(format).getHeader();
        verify(format).getSkipHeaderRecord();
    }

    private Map<String, Integer> invokeInitializeHeader(CSVParser parserInstance) throws Throwable {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Integer> result = (Map<String, Integer>) method.invoke(parserInstance);
            return result;
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}