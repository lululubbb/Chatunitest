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
import java.util.Collection;
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
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        mockFormat = mock(CSVFormat.class);

        // Create CSVParser instance using constructor with Reader and CSVFormat
        parser = new CSVParser(new java.io.StringReader(""), mockFormat);

        // Use reflection to set reusableToken field with a Token instance
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Token token = new Token();
        reusableTokenField.set(parser, token);

        // Use reflection to set format field to mockFormat (since format is final)
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(parser, mockFormat);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Setup reusableToken.content to "testValue"
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Token token = (Token) reusableTokenField.get(parser);

        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        // Set content to StringBuilder with "testValue"
        contentField.set(token, new StringBuilder("testValue"));

        // Setup format.getNullString() to return null
        when(mockFormat.getNullString()).thenReturn(null);

        // Clear record list before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Invoke private addRecordValue method
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Assert record contains the input string
        assertEquals(1, recordList.size());
        assertEquals("testValue", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringMatchesInput_addsNull() throws Exception {
        // Setup reusableToken.content to "NULL"
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Token token = (Token) reusableTokenField.get(parser);

        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        // Set content to StringBuilder with "NULL"
        contentField.set(token, new StringBuilder("NULL"));

        // Setup format.getNullString() to return "null" (case insensitive match)
        when(mockFormat.getNullString()).thenReturn("null");

        // Clear record list before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Invoke private addRecordValue method
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Assert record contains null
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNotMatchesInput_addsInput() throws Exception {
        // Setup reusableToken.content to "value"
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Token token = (Token) reusableTokenField.get(parser);

        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        // Set content to StringBuilder with "value"
        contentField.set(token, new StringBuilder("value"));

        // Setup format.getNullString() to return "null" (does not match input)
        when(mockFormat.getNullString()).thenReturn("null");

        // Clear record list before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Invoke private addRecordValue method
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Assert record contains the input string
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }
}