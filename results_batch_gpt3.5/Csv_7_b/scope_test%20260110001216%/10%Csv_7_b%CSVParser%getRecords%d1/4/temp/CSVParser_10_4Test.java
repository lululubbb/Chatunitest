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
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_10_4Test {

    private CSVFormat mockFormat;
    private Reader reader;
    private CSVParser parser;

    @BeforeEach
    public void setUp() throws IOException {
        mockFormat = mock(CSVFormat.class);
        reader = new StringReader("header1,header2\nvalue1,value2");
        parser = new CSVParser(reader, mockFormat);
    }

    @Test
    @Timeout(8000)
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records instanceof List);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordsWithListParameter() throws IOException {
        List<CSVRecord> inputList = new ArrayList<>();
        // Use reflection to access the generic getRecords(List<CSVRecord>) method
        try {
            Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
            getRecordsMethod.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, inputList);
            assertSame(inputList, result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection invocation failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testPrivateGetRecordsViaReflection() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        List<CSVRecord> inputList = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, inputList);

        assertSame(inputList, result);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeaderViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method initializeHeader = CSVParser.class.getDeclaredMethod("initializeHeader");
        initializeHeader.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) initializeHeader.invoke(parser);

        assertNotNull(headerMap);
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValueViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addRecordValue = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValue.setAccessible(true);

        addRecordValue.invoke(parser);
    }

    @Test
    @Timeout(8000)
    public void testIsClosed() {
        boolean closed = parser.isClosed();
        assertFalse(closed);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap() {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber() {
        long recordNumber = parser.getRecordNumber();
        assertEquals(0L, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testGetCurrentLineNumber() {
        long lineNumber = parser.getCurrentLineNumber();
        assertTrue(lineNumber >= 0);
    }

    @Test
    @Timeout(8000)
    public void testClose() throws IOException {
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    @Timeout(8000)
    public void testIteratorAndNextRecord() throws IOException {
        Iterator<CSVRecord> iterator = parser.iterator();
        assertNotNull(iterator);
        try {
            CSVRecord record = parser.nextRecord();
            assertNotNull(record);
        } catch (NoSuchElementException e) {
            // Acceptable if no record available
        }
    }
}