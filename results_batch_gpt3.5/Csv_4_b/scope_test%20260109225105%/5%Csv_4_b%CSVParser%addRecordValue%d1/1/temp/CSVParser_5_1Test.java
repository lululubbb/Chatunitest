package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_5_1Test {

    private CSVParser parser;
    private CSVFormat formatMock;
    private Token token;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        formatMock = mock(CSVFormat.class);

        // Create CSVParser instance via constructor with dummy Reader and mocked format
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Access reusableToken field and get the Token instance
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        token = (Token) reusableTokenField.get(parser);

        // Clear record list before each test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Inject mocked format into parser to ensure format is the mock
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(parser, formatMock);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Setup reusableToken content to "testValue"
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = new StringBuilder("testValue");
        contentField.set(token, content);

        // format.getNullString() returns null
        when(formatMock.getNullString()).thenReturn(null);

        // Invoke private addRecordValue method
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Verify that record list contains "testValue"
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);

        assertEquals(1, recordList.size());
        assertEquals("testValue", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_inputEqualsNullString_addsNull() throws Exception {
        // Setup reusableToken content to "null"
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = new StringBuilder("null");
        contentField.set(token, content);

        // format.getNullString() returns "null"
        when(formatMock.getNullString()).thenReturn("null");

        // Invoke private addRecordValue method
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Verify that record list contains null
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);

        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_inputNotEqualsNullString_addsInput() throws Exception {
        // Setup reusableToken content to "value"
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = new StringBuilder("value");
        contentField.set(token, content);

        // format.getNullString() returns "null"
        when(formatMock.getNullString()).thenReturn("null");

        // Invoke private addRecordValue method
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Verify that record list contains "value"
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);

        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }
}