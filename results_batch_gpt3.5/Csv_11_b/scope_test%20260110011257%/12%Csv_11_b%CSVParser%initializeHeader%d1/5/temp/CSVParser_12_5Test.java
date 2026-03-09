package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

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
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        // We need to create a CSVParser instance with a Reader and this formatMock.
        // Since constructor is public CSVParser(Reader, CSVFormat), we can pass a dummy Reader.
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullHeader() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);

        Map<String, Integer> result = invokeInitializeHeader();

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeader_ReadsFromNextRecord() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] { "a", "b", "c" });

        CSVParser spyParser = spy(parser);
        doReturn(recordMock).when(spyParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));
        assertEquals(Integer.valueOf(2), result.get("c"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeader_NextRecordNull() throws Exception {
        when(formatMock.getHeader()).thenReturn(new String[0]);

        CSVParser spyParser = spy(parser);
        doReturn(null).when(spyParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyHeader_SkipHeaderTrue() throws Exception {
        String[] header = new String[] { "x", "y" };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        CSVParser spyParser = spy(parser);
        doReturn(mock(CSVRecord.class)).when(spyParser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(spyParser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));

        verify(spyParser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyHeader_SkipHeaderFalse() throws Exception {
        String[] header = new String[] { "x", "y" };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateHeader_Throws() throws Exception {
        String[] header = new String[] { "a", "a" };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeInitializeHeader());
        assertTrue(ex.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateEmptyHeader_IgnoreEmptyHeadersFalse_Throws() throws Exception {
        String[] header = new String[] { " ", " " };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeInitializeHeader());
        assertTrue(ex.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateEmptyHeader_IgnoreEmptyHeadersTrue_Allows() throws Exception {
        String[] header = new String[] { " ", " " };
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(true);

        Map<String, Integer> result = invokeInitializeHeader();

        assertNotNull(result);
        // Because keys are the same string " ", last put overrides previous, so only one entry remains.
        // So map size is 1.
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.get(" "));
    }

    private Map<String, Integer> invokeInitializeHeader() throws Exception {
        return invokeInitializeHeader(this.parser);
    }

    private Map<String, Integer> invokeInitializeHeader(CSVParser parserInstance) throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Integer> result = (Map<String, Integer>) method.invoke(parserInstance);
            return result;
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            } else if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw e;
            }
        }
    }
}