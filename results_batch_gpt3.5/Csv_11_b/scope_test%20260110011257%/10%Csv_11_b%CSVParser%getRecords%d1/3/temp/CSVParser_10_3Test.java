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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_10_3Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Create a spy of CSVParser with a dummy Reader and CSVFormat to allow partial mocking
        parser = spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_emptyList() throws IOException {
        // Because getRecords() calls getRecords(List), which is generic, we test getRecords() returns a list (likely empty)
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        // The list can be empty or contain records depending on input, but for empty input it should be empty
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withReflectionInvokeGetRecordsList() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private <T> T getRecords(T records) method
        Method getRecordsListMethod = CSVParser.class.getDeclaredMethod("getRecords", Object.class);
        getRecordsListMethod.setAccessible(true);

        List<CSVRecord> inputList = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<CSVRecord> records = (List<CSVRecord>) getRecordsListMethod.invoke(parser, inputList);

        assertNotNull(records);
        assertSame(inputList, records);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_verifyInteraction() throws IOException {
        // Spy on parser and verify getRecords(List) is called once when getRecords() is called
        CSVParser spyParser = spy(parser);

        spyParser.getRecords();

        verify(spyParser, times(1)).getRecords(any());
    }
}