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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class CSVParser_11_1Test {

    private CSVFormat formatMock;
    private CSVParser parserSpy;

    @BeforeEach
    public void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        Lexer lexerMock = mock(Lexer.class);
        // Create a partial mock (spy) of CSVParser to mock nextRecord() method
        parserSpy = Mockito.spy(new CSVParser(new java.io.StringReader(""), formatMock));
        // Inject lexerMock into parserSpy using reflection
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parserSpy, lexerMock);
        // Inject formatMock into parserSpy's 'format' field via reflection to ensure correct format is used
        java.lang.reflect.Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(parserSpy, formatMock);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.getHeader()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parserSpy);

        assertNull(result);
        verify(formatMock).getHeader();
        verify(parserSpy, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderEmpty_recordNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        // nextRecord returns null
        doReturn(null).when(parserSpy).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parserSpy);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(parserSpy).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderEmpty_recordHasValues() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[]{"a", "b", "c"});
        doReturn(recordMock).when(parserSpy).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parserSpy);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));
        assertEquals(Integer.valueOf(2), result.get("c"));
        verify(parserSpy).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNonEmpty_skipHeaderFalse() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] header = new String[]{"x", "y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parserSpy);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));
        verify(formatMock).getSkipHeaderRecord();
        verify(parserSpy, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNonEmpty_skipHeaderTrue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] header = new String[]{"x", "y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        doReturn(mock(CSVRecord.class)).when(parserSpy).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parserSpy);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));
        verify(formatMock).getSkipHeaderRecord();
        verify(parserSpy).nextRecord();
    }
}