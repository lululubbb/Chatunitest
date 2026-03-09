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
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserInitializeHeaderTest {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
    }

    private CSVParser createParserWithFormat(CSVFormat format) throws IOException {
        // We create a CSVParser instance with a Reader and the mocked format.
        // The Reader is never used in initializeHeader so can be a dummy.
        return new CSVParser(new java.io.StringReader(""), format);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        return (Map<String, Integer>) method.invoke(parser);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NullHeader() throws Exception {
        // format.getHeader() returns null -> initializeHeader returns null
        when(formatMock.getHeader()).thenReturn(null);
        parser = createParserWithFormat(formatMock);

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNull(result, "Header map should be null when format header is null");
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeader_ReadsFirstRecord() throws Exception {
        // format.getHeader() returns empty array -> read first record for header
        when(formatMock.getHeader()).thenReturn(new String[0]);
        parser = spy(createParserWithFormat(formatMock));

        // Mock nextRecord to return a CSVRecord with values
        CSVRecord recordMock = mock(CSVRecord.class);
        String[] headerValues = new String[] {"A", "B", "C"};
        when(recordMock.values()).thenReturn(headerValues);
        doReturn(recordMock).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("A"));
        assertEquals(Integer.valueOf(1), result.get("B"));
        assertEquals(Integer.valueOf(2), result.get("C"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeader_NextRecordReturnsNull() throws Exception {
        // format.getHeader() returns empty array but nextRecord returns null -> headerRecord null -> hdrMap empty
        when(formatMock.getHeader()).thenReturn(new String[0]);
        parser = spy(createParserWithFormat(formatMock));

        doReturn(null).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyHeader_SkipHeaderRecordFalse() throws Exception {
        String[] formatHeader = new String[] {"X", "Y"};
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);
        parser = spy(createParserWithFormat(formatMock));

        // nextRecord should NOT be called because skipHeaderRecord is false
        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("X"));
        assertEquals(Integer.valueOf(1), result.get("Y"));
        verify(parser, never()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_NonEmptyHeader_SkipHeaderRecordTrue() throws Exception {
        String[] formatHeader = new String[] {"X", "Y"};
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);
        parser = spy(createParserWithFormat(formatMock));

        // nextRecord should be called once because skipHeaderRecord is true
        doReturn(mock(CSVRecord.class)).when(parser).nextRecord();

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("X"));
        assertEquals(Integer.valueOf(1), result.get("Y"));
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_DuplicateHeader_ThrowsException() throws Exception {
        String[] formatHeader = new String[] {"A", "B", "A"};
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);
        parser = spy(createParserWithFormat(formatMock));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> invokeInitializeHeader(parser));

        assertTrue(thrown.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeader_IgnoreEmptyHeadersFalse_NoException() throws Exception {
        String[] formatHeader = new String[] {"A", " ", "B"};
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(false);
        parser = spy(createParserWithFormat(formatMock));

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("A"));
        assertEquals(Integer.valueOf(1), result.get(" "));
        assertEquals(Integer.valueOf(2), result.get("B"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_EmptyHeader_IgnoreEmptyHeadersTrue_DuplicateEmptyAllowed() throws Exception {
        String[] formatHeader = new String[] {"A", " ", " "};
        when(formatMock.getHeader()).thenReturn(formatHeader);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(formatMock.getIgnoreEmptyHeaders()).thenReturn(true);
        parser = spy(createParserWithFormat(formatMock));

        Map<String, Integer> result = invokeInitializeHeader(parser);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("A"));
        // Last duplicate empty header index should be retained
        assertEquals(Integer.valueOf(2), result.get(" "));
    }

}