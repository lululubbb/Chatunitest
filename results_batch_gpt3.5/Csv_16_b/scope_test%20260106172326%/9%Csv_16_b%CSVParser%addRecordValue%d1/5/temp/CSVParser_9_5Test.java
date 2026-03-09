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

class CSVParser_9_5Test {

    private CSVParser parser;
    private CSVFormat formatMock;
    private Token token;
    private Field reusableTokenField;
    private Field recordListField;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        formatMock = mock(CSVFormat.class);
        when(formatMock.getTrim()).thenReturn(false);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);
        when(formatMock.getNullString()).thenReturn(null);

        // Create CSVParser instance using constructor with Reader and CSVFormat
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Access private reusableToken field and set its content
        reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        token = (Token) reusableTokenField.get(parser);

        // Access private recordList field
        recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);

        // Clear recordList before each test
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(parser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NormalValue() throws Exception {
        // Setup token content to a non-empty string
        token.content.setLength(0);
        token.content.append("value");

        // format.getTrim() returns false, so inputClean = input
        when(formatMock.getTrim()).thenReturn(false);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);
        when(formatMock.getNullString()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(parser, false);

        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(parser);
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_TrimTrue() throws Exception {
        token.content.setLength(0);
        token.content.append("  spaced  ");

        when(formatMock.getTrim()).thenReturn(true);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);
        when(formatMock.getNullString()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(parser, false);

        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(parser);
        assertEquals(1, recordList.size());
        assertEquals("spaced", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringMatches() throws Exception {
        token.content.setLength(0);
        token.content.append("NULL");

        when(formatMock.getTrim()).thenReturn(false);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);
        when(formatMock.getNullString()).thenReturn("NULL");

        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(parser, false);

        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(parser);
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_LastRecordEmptyInputTrailingDelimiterTrue() throws Exception {
        token.content.setLength(0);
        token.content.append("   ");

        when(formatMock.getTrim()).thenReturn(true);
        when(formatMock.getTrailingDelimiter()).thenReturn(true);
        when(formatMock.getNullString()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(parser, true);

        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(parser);
        // Should not add anything because lastRecord is true, inputClean empty, trailingDelimiter true
        assertTrue(recordList.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_LastRecordEmptyInputTrailingDelimiterFalse() throws Exception {
        token.content.setLength(0);
        token.content.append("   ");

        when(formatMock.getTrim()).thenReturn(true);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);
        when(formatMock.getNullString()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(parser, true);

        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordListField.get(parser);
        // Should add empty string because trailingDelimiter is false
        assertEquals(1, recordList.size());
        assertEquals("", recordList.get(0));
    }
}