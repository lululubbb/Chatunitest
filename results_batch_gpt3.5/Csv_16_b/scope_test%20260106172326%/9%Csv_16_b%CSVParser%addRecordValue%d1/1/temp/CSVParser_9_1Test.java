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

class CSVParser_9_1Test {

    private CSVParser csvParser;
    private CSVFormat mockFormat;
    private Token reusableToken;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        mockFormat = mock(CSVFormat.class);
        when(mockFormat.getTrim()).thenReturn(false);
        when(mockFormat.getTrailingDelimiter()).thenReturn(false);
        when(mockFormat.getNullString()).thenReturn(null);

        // Create CSVParser instance using constructor
        csvParser = new CSVParser(new java.io.StringReader(""), mockFormat);

        // Access reusableToken field and set a new Token instance
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableToken = (Token) reusableTokenField.get(csvParser);

        // Reset recordList
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(csvParser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_regularValue() throws Exception {
        // Setup reusableToken.content to non-empty value
        reusableToken.content.setLength(0);
        reusableToken.content.append("value");

        // Mock format.getTrim() to true
        when(mockFormat.getTrim()).thenReturn(true);
        when(mockFormat.getNullString()).thenReturn(null);
        when(mockFormat.getTrailingDelimiter()).thenReturn(false);

        // Invoke private method addRecordValue with lastRecord = false
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser, false);

        // Verify recordList contains trimmed "value"
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(csvParser);

        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringValue() throws Exception {
        // Setup reusableToken.content to "NULL"
        reusableToken.content.setLength(0);
        reusableToken.content.append("NULL");

        // Mock format.getNullString() to "NULL"
        when(mockFormat.getNullString()).thenReturn("NULL");
        when(mockFormat.getTrim()).thenReturn(false);
        when(mockFormat.getTrailingDelimiter()).thenReturn(false);

        // Invoke private method addRecordValue with lastRecord = false
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser, false);

        // Verify recordList contains null
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(csvParser);

        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_lastRecordEmptyInputTrailingDelimiter() throws Exception {
        // Setup reusableToken.content to empty string
        reusableToken.content.setLength(0);

        // Mock format.getTrailingDelimiter() to true and getTrim() true
        when(mockFormat.getTrailingDelimiter()).thenReturn(true);
        when(mockFormat.getTrim()).thenReturn(true);
        when(mockFormat.getNullString()).thenReturn(null);

        // Invoke private method addRecordValue with lastRecord = true
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser, true);

        // Verify recordList remains empty (no addition)
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(csvParser);

        assertTrue(recordList.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_lastRecordEmptyInputNoTrailingDelimiter() throws Exception {
        // Setup reusableToken.content to empty string
        reusableToken.content.setLength(0);

        // Mock format.getTrailingDelimiter() to false and getTrim() true
        when(mockFormat.getTrailingDelimiter()).thenReturn(false);
        when(mockFormat.getTrim()).thenReturn(true);
        when(mockFormat.getNullString()).thenReturn(null);

        // Invoke private method addRecordValue with lastRecord = true
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser, true);

        // Verify recordList contains empty string
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(csvParser);

        assertEquals(1, recordList.size());
        assertEquals("", recordList.get(0));
    }
}