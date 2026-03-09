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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_10_5Test {

    CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        parser = new CSVParser(new java.io.StringReader("header1,header2\nvalue1,value2"), CSVFormat.DEFAULT.withHeader());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_returnsList() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        CSVRecord firstRecord = records.get(0);
        assertEquals("value1", firstRecord.get(0));
        assertEquals("value2", firstRecord.get(1));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withEmptyRecordsList() throws Exception {
        List<CSVRecord> inputRecords = new ArrayList<>();

        // Use reflection to invoke private <T extends List<CSVRecord>> T getRecords(T)
        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, inputRecords);

        assertSame(inputRecords, result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_invokesPrivateGetRecords() throws Exception {
        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        List<CSVRecord> input = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, input);

        assertSame(input, result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_handlesIOException() throws Exception {
        CSVParser spyParser = spy(parser);

        doThrow(new IOException("Simulated IO error")).when(spyParser).nextRecord();

        IOException thrown = assertThrows(IOException.class, spyParser::getRecords);
        assertEquals("Simulated IO error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_iteratesAllRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
    }
}