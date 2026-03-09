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

class CSVParser_9_3Test {
    private CSVParser parser;
    private CSVFormat formatMock;
    private Token token;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        when(formatMock.getTrim()).thenReturn(false);
        when(formatMock.getTrailingDelimiter()).thenReturn(false);
        when(formatMock.getNullString()).thenReturn(null);

        // Use the public constructor with Reader and CSVFormat
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Access private reusableToken field and set content
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        token = (Token) reusableTokenField.get(parser);

        // Reset recordList to empty list
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<?> recordList = (List<?>) recordListField.get(parser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NormalInput() throws Exception {
        setTokenContent("value");
        setFormatTrim(false);
        setFormatTrailingDelimiter(false);
        setFormatNullString(null);

        invokeAddRecordValue(false);

        List<?> recordList = getRecordList();
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_TrimTrue() throws Exception {
        setTokenContent("  value  ");
        setFormatTrim(true);
        setFormatTrailingDelimiter(false);
        setFormatNullString(null);

        invokeAddRecordValue(false);

        List<?> recordList = getRecordList();
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_LastRecordTrailingDelimiterEmptyInput() throws Exception {
        setTokenContent("   ");
        setFormatTrim(true);
        setFormatTrailingDelimiter(true);
        setFormatNullString(null);

        invokeAddRecordValue(true);

        List<?> recordList = getRecordList();
        // Should not add any value
        assertEquals(0, recordList.size());
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullString() throws Exception {
        setTokenContent("NULL");
        setFormatTrim(false);
        setFormatTrailingDelimiter(false);
        setFormatNullString("NULL");

        invokeAddRecordValue(false);

        List<?> recordList = getRecordList();
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringWithTrim() throws Exception {
        setTokenContent("  NULL  ");
        setFormatTrim(true);
        setFormatTrailingDelimiter(false);
        setFormatNullString("NULL");

        invokeAddRecordValue(false);

        List<?> recordList = getRecordList();
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    // Helper methods

    private void setTokenContent(String content) throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        // Set the StringBuilder content instead of replacing the field
        StringBuilder sb = (StringBuilder) contentField.get(token);
        sb.setLength(0);
        sb.append(content);
    }

    private void setFormatTrim(boolean trim) {
        when(formatMock.getTrim()).thenReturn(trim);
    }

    private void setFormatTrailingDelimiter(boolean trailingDelimiter) {
        when(formatMock.getTrailingDelimiter()).thenReturn(trailingDelimiter);
    }

    private void setFormatNullString(String nullString) {
        when(formatMock.getNullString()).thenReturn(nullString);
    }

    private void invokeAddRecordValue(boolean lastRecord) throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(parser, lastRecord);
    }

    @SuppressWarnings("unchecked")
    private List<String> getRecordList() throws Exception {
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        return (List<String>) recordListField.get(parser);
    }
}