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

class CSVParser_9_4Test {

    private CSVParser parser;
    private CSVFormat formatMock;
    private Token token;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        formatMock = mock(CSVFormat.class);

        // Default stubbing
        when(formatMock.getTrim()).thenReturn(false);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);
        when(formatMock.getNullString()).thenReturn(null);

        // Create CSVParser instance with dummy Reader and mocked format
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Access reusableToken field and set its content
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        token = (Token) reusableTokenField.get(parser);

        // Set token.content to a new StringBuilder
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(token, new StringBuilder());

        // Clear recordList before each test to avoid interference
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<?> recordList = (List<?>) recordListField.get(parser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_normalInput() throws Exception {
        // Set token content to "value"
        setTokenContent("value");

        // lastRecord = false
        invokeAddRecordValue(false);

        // recordList should contain "value"
        List<String> recordList = getRecordList();
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_trimTrue() throws Exception {
        // Setup format.getTrim() to true
        when(formatMock.getTrim()).thenReturn(true);

        // Set token content with spaces
        setTokenContent("  value  ");

        invokeAddRecordValue(false);

        List<String> recordList = getRecordList();
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0)); // trimmed
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_lastRecordTrailingDelimiterEmptyInput() throws Exception {
        // Setup format.getTrim() to true and getTrailingDelimiter to true
        when(formatMock.getTrim()).thenReturn(true);
        when(formatMock.getTrailingDelimiter()).thenReturn(true);

        // Set token content to spaces only (which will be trimmed to empty)
        setTokenContent("   ");

        // lastRecord = true
        invokeAddRecordValue(true);

        // recordList should remain empty (no added value)
        List<String> recordList = getRecordList();
        assertTrue(recordList.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringMatch() throws Exception {
        // Setup format.getNullString() to "NULL"
        when(formatMock.getNullString()).thenReturn("NULL");

        // Set token content to "NULL"
        setTokenContent("NULL");

        invokeAddRecordValue(false);

        List<String> recordList = getRecordList();
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0)); // Should be null because inputClean equals nullString
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNoMatch() throws Exception {
        // Setup format.getNullString() to "NULL"
        when(formatMock.getNullString()).thenReturn("NULL");

        // Set token content to "null" (different case, no trim)
        setTokenContent("null");

        invokeAddRecordValue(false);

        List<String> recordList = getRecordList();
        assertEquals(1, recordList.size());
        assertEquals("null", recordList.get(0));
    }

    // Helper method to set token.content value
    private void setTokenContent(String value) throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder sb = (StringBuilder) contentField.get(token);
        sb.setLength(0);
        sb.append(value);
    }

    // Helper method to invoke private addRecordValue(boolean) method
    private void invokeAddRecordValue(boolean lastRecord) throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(parser, lastRecord);
    }

    // Helper method to get recordList field
    @SuppressWarnings("unchecked")
    private List<String> getRecordList() throws Exception {
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        return (List<String>) recordListField.get(parser);
    }
}