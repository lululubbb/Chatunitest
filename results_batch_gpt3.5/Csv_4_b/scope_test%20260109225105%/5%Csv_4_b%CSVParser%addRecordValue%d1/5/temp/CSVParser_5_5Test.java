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

class CSVParser_5_5Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        formatMock = mock(CSVFormat.class);

        // Create CSVParser instance with dummy Reader and mocked format
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Clear the record list before each test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringIsNull_AddsInputAsIs() throws Exception {
        // Setup reusableToken.content.toString()
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("testValue");

        // format.getNullString() returns null
        when(formatMock.getNullString()).thenReturn(null);

        // Invoke private method addRecordValue
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Verify record contains the input string as is
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);

        assertEquals(1, recordList.size());
        assertEquals("testValue", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringMatchesInput_AddsNull() throws Exception {
        // Setup reusableToken.content.toString()
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("NULL");

        // format.getNullString() returns "null" (case insensitive)
        when(formatMock.getNullString()).thenReturn("null");

        // Invoke private method addRecordValue
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Verify record contains null
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);

        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringDoesNotMatchInput_AddsInput() throws Exception {
        // Setup reusableToken.content.toString()
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("value");

        // format.getNullString() returns "nullString" (different from input)
        when(formatMock.getNullString()).thenReturn("nullString");

        // Invoke private method addRecordValue
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Verify record contains the input string as is
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);

        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }
}