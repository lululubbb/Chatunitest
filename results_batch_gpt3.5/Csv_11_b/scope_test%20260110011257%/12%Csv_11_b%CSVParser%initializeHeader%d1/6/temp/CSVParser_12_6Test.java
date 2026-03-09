package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
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
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_initializeHeader_Test {

    private CSVParser parser;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullFormatHeader() throws Exception {
        when(format.getHeader()).thenReturn(null);
        parser = createParserWithFormat(format);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyFormatHeader_ReadsFromNextRecord() throws Exception {
        when(format.getHeader()).thenReturn(new String[0]);
        parser = spy(createParserWithFormat(format));

        CSVRecord record = mock(CSVRecord.class);
        when(record.values()).thenReturn(new String[] { "A", "B" });
        doReturn(record).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("A"));
        assertEquals(Integer.valueOf(1), result.get("B"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderFalse() throws Exception {
        String[] headers = { "H1", "H2" };
        when(format.getHeader()).thenReturn(headers);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        parser = spy(createParserWithFormat(format));

        Map<String, Integer> result = invokeInitializeHeader(parser);

        verify(parser, never()).nextRecord();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("H1"));
        assertEquals(Integer.valueOf(1), result.get("H2"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyFormatHeader_SkipHeaderTrue() throws Exception {
        String[] headers = { "H1", "H2" };
        when(format.getHeader()).thenReturn(headers);
        when(format.getSkipHeaderRecord()).thenReturn(true);
        parser = spy(createParserWithFormat(format));

        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        verify(parser).nextRecord();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("H1"));
        assertEquals(Integer.valueOf(1), result.get("H2"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateHeader_ThrowsIllegalArgumentException() throws Exception {
        String[] headers = { "H1", "H1" };
        when(format.getHeader()).thenReturn(headers);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getIgnoreEmptyHeaders()).thenReturn(false);
        parser = spy(createParserWithFormat(format));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            invokeInitializeHeader(parser);
        });

        assertTrue(thrown.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateEmptyHeader_IgnoreEmptyHeadersFalse_Throws() throws Exception {
        String[] headers = { " ", " " };
        when(format.getHeader()).thenReturn(headers);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getIgnoreEmptyHeaders()).thenReturn(false);
        parser = spy(createParserWithFormat(format));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            invokeInitializeHeader(parser);
        });
        assertTrue(thrown.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateEmptyHeader_IgnoreEmptyHeadersTrue_NoThrow() throws Exception {
        String[] headers = { " ", " " };
        when(format.getHeader()).thenReturn(headers);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getIgnoreEmptyHeaders()).thenReturn(true);
        parser = spy(createParserWithFormat(format));

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(" "));
    }

    // Helper to create CSVParser instance with the mocked format
    private CSVParser createParserWithFormat(CSVFormat format) throws IOException {
        return new CSVParser(new StringReader(""), format);
    }

    // Use reflection to invoke private initializeHeader method
    private Map<String, Integer> invokeInitializeHeader(CSVParser parser)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> result = (Map<String, Integer>) method.invoke(parser);
        return result;
    }
}