package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_10_2Test {

    private CSVParser parser;
    private CSVFormat formatMock;
    private Reader reader;

    @BeforeEach
    void setUp() throws IOException {
        formatMock = mock(CSVFormat.class);
        // Mock behavior to avoid NullPointerException inside CSVParser constructor or methods
        when(formatMock.getHeader()).thenReturn(null);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        reader = new StringReader("header1,header2\nvalue1,value2\nvalue3,value4");
        parser = new CSVParser(reader, formatMock);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_noArgument_returnsListOfCSVRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        assertEquals(2, records.size());
        assertEquals("value1", records.get(0).get(0));
        assertEquals("value2", records.get(0).get(1));
        assertEquals("value3", records.get(1).get(0));
        assertEquals("value4", records.get(1).get(1));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withEmptyList_argumentReturnsSameList() throws Exception {
        List<CSVRecord> inputList = new ArrayList<>();
        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, inputList);

        assertSame(inputList, result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withPrePopulatedList_appendsRecords() throws Exception {
        List<CSVRecord> inputList = new ArrayList<>();
        // Use reflection to find the correct constructor of CSVRecord
        // Constructor: CSVRecord(String[] values, Map<String,Integer> headerMap, CSVFormat format, long recordNumber)
        CSVRecord existingRecord = new CSVRecord(new String[]{"existing1", "existing2"}, null, null, 0L);
        inputList.add(existingRecord);

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, inputList);

        assertSame(inputList, result);
        assertEquals(3, result.size());
        assertEquals("existing1", result.get(0).get(0));
        assertEquals("value1", result.get(1).get(0));
        assertEquals("value3", result.get(2).get(0));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_handlesIOException() throws Exception {
        Reader failingReader = mock(Reader.class);
        when(failingReader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("read error"));
        CSVParser failingParser = new CSVParser(failingReader, formatMock);

        IOException thrown = assertThrows(IOException.class, failingParser::getRecords);
        assertEquals("read error", thrown.getMessage());
    }
}