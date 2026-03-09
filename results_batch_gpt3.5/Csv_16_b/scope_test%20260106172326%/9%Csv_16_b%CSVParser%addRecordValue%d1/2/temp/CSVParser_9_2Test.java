package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_9_2Test {

    private CSVParser parser;
    private CSVFormat formatMock;
    private Token token;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        when(formatMock.getTrim()).thenReturn(false);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);
        when(formatMock.getNullString()).thenReturn(null);

        // Create CSVParser instance using constructor (Reader and CSVFormat)
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Access private field reusableToken and set a new Token instance
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        token = new Token();

        // Replace the reusableToken instance in parser with our token
        reusableTokenField.set(parser, token);

        // Clear recordList to start fresh
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(parser);
        recordList.clear();
    }

    private void setTokenContent(String content) throws Exception {
        // Modify the existing StringBuilder inside token.content instead of reassigning it
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder sb = (StringBuilder) contentField.get(token);
        sb.setLength(0);
        sb.append(content);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NormalInput() throws Exception {
        setTokenContent("value1");

        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);

        addRecordValueMethod.invoke(parser, false);

        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(parser);

        assertEquals(1, recordList.size());
        assertEquals("value1", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_TrimInput() throws Exception {
        when(formatMock.getTrim()).thenReturn(true);

        // Re-create parser to apply new mock behavior to format field
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(parser, token);

        // Clear recordList again
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(parser);
        recordList.clear();

        setTokenContent("  value2  ");

        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);

        addRecordValueMethod.invoke(parser, false);

        recordList = (List<String>) recordListField.get(parser);

        assertEquals(1, recordList.size());
        assertEquals("value2", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullString() throws Exception {
        when(formatMock.getNullString()).thenReturn("NULL");

        // Re-create parser to apply new mock behavior to format field
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(parser, token);

        // Clear recordList again
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(parser);
        recordList.clear();

        setTokenContent("NULL");

        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);

        addRecordValueMethod.invoke(parser, false);

        recordList = (List<String>) recordListField.get(parser);

        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_LastRecordEmptyInputTrailingDelimiter() throws Exception {
        when(formatMock.getTrim()).thenReturn(false);
        when(formatMock.getTrailingDelimiter()).thenReturn(true);

        // Re-create parser to apply new mock behavior to format field
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(parser, token);

        // Clear recordList again
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(parser);
        recordList.clear();

        setTokenContent("");

        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);

        addRecordValueMethod.invoke(parser, true);

        recordList = (List<String>) recordListField.get(parser);

        // Should not add anything
        assertTrue(recordList.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_LastRecordEmptyInputNoTrailingDelimiter() throws Exception {
        when(formatMock.getTrim()).thenReturn(false);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);

        // Re-create parser to apply new mock behavior to format field
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(parser, token);

        // Clear recordList again
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(parser);
        recordList.clear();

        setTokenContent("");

        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);

        addRecordValueMethod.invoke(parser, true);

        recordList = (List<String>) recordListField.get(parser);

        // Should add empty string
        assertEquals(1, recordList.size());
        assertEquals("", recordList.get(0));
    }
}