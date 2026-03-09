package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
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
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_12_4Test {

    private CSVParser csvParser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        Lexer lexerMock = mock(Lexer.class);

        // Create CSVParser instance with the constructor (public)
        csvParser = new CSVParser(new StringReader(""), formatMock);

        // Inject mocked lexer into csvParser using reflection
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.getHeader()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) method.invoke(csvParser);

        assertNull(headerMap);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordReturnsNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVParser spyParser = Mockito.spy(csvParser);
        doReturn(null).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) method.invoke(spyParser);

        assertNull(headerMap);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordReturnsValues() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] {"col1", "col2"});

        CSVParser spyParser = Mockito.spy(csvParser);
        doReturn(recordMock).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("col1"));
        assertEquals(Integer.valueOf(1), headerMap.get("col2"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordTrue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] formatHeader = new String[] {"h1", "h2"};
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        CSVParser spyParser = Mockito.spy(csvParser);
        doReturn(mock(CSVRecord.class)).when(spyParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("h1"));
        assertEquals(Integer.valueOf(1), headerMap.get("h2"));

        verify(spyParser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordFalse() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] formatHeader = new String[] {"h1", "h2"};
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVParser spyParser = Mockito.spy(csvParser);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) method.invoke(spyParser);

        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("h1"));
        assertEquals(Integer.valueOf(1), headerMap.get("h2"));

        verify(spyParser, never()).nextRecord();
    }
}