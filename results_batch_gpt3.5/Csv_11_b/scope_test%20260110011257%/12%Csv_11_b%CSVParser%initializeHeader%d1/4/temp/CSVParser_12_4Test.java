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
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_12_4Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    public void setUp() {
        formatMock = mock(CSVFormat.class);
    }

    /**
     * Helper method to invoke private initializeHeader via reflection.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        try {
            return (Map<String, Integer>) method.invoke(parser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // unwrap the underlying exception
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else if (cause instanceof IOException) {
                throw (IOException) cause;
            } else {
                throw e;
            }
        }
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_NullHeader() throws Exception {
        // format.getHeader() returns null -> method returns null
        when(formatMock.getHeader()).thenReturn(null);
        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNull(result);
        verify(formatMock).getHeader();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_EmptyHeader_ReadsFirstRecord() throws Exception {
        // format.getHeader() returns empty array -> read header from first record
        when(formatMock.getHeader()).thenReturn(new String[0]);
        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));

        // Mock nextRecord() to return a CSVRecord with header values
        CSVRecord recordMock = mock(CSVRecord.class);
        when(recordMock.values()).thenReturn(new String[] {"col1", "col2", "col3"});
        doReturn(recordMock).when(parser).nextRecord();

        Map<String, Integer> hdrMap = invokeInitializeHeader(parser);

        assertNotNull(hdrMap);
        assertEquals(3, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("col1"));
        assertEquals(Integer.valueOf(1), hdrMap.get("col2"));
        assertEquals(Integer.valueOf(2), hdrMap.get("col3"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_EmptyHeader_NextRecordNull() throws Exception {
        // format.getHeader() returns empty array -> read header from first record returns null
        when(formatMock.getHeader()).thenReturn(new String[0]);
        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));

        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> hdrMap = invokeInitializeHeader(parser);

        assertNotNull(hdrMap);
        assertTrue(hdrMap.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_NonEmptyHeader_SkipHeaderFalse() throws Exception {
        String[] header = new String[] {"A", "B", "C"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));

        // nextRecord should NOT be called
        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> hdrMap = invokeInitializeHeader(parser);

        assertNotNull(hdrMap);
        assertEquals(3, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("A"));
        assertEquals(Integer.valueOf(1), hdrMap.get("B"));
        assertEquals(Integer.valueOf(2), hdrMap.get("C"));
        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_NonEmptyHeader_SkipHeaderTrue() throws Exception {
        String[] header = new String[] {"A", "B"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));

        // nextRecord should be called once and ignored
        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> hdrMap = invokeInitializeHeader(parser);

        assertNotNull(hdrMap);
        assertEquals(2, hdrMap.size());
        assertEquals(Integer.valueOf(0), hdrMap.get("A"));
        assertEquals(Integer.valueOf(1), hdrMap.get("B"));
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_DuplicateHeader_ThrowsException() throws Exception {
        String[] header = new String[] {"A", "B", "A"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            invokeInitializeHeader(parser);
        });

        assertTrue(thrown.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_EmptyHeader_IgnoreEmptyHeadersTrue_AllowsDuplicateEmpty() throws Exception {
        String[] header = new String[] {"", " ", ""};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(true);

        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));

        Map<String, Integer> hdrMap = invokeInitializeHeader(parser);

        assertNotNull(hdrMap);
        assertEquals(3, hdrMap.size());
        assertEquals(Integer.valueOf(2), hdrMap.get(""));
        assertEquals(Integer.valueOf(1), hdrMap.get(" "));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_EmptyHeader_IgnoreEmptyHeadersFalse_DuplicateEmptyThrows() throws Exception {
        String[] header = new String[] {"", " ", ""};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);

        parser = spy(new CSVParser(new java.io.StringReader(""), formatMock));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            invokeInitializeHeader(parser);
        });

        assertTrue(thrown.getMessage().contains("duplicate name"));
    }
}