package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

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
        formatMock = mock(CSVFormat.class);
        when(formatMock.getNullString()).thenReturn(null);

        // Create CSVParser instance with a dummy Reader and the mocked CSVFormat
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Clear record list for test isolation
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Reset reusableToken content
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);
        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringNull_AddsInput() throws Exception {
        // Setup reusableToken content to "value"
        setReusableTokenContent("value");

        // format.getNullString() returns null (already set in setUp)

        // Invoke private method addRecordValue
        invokeAddRecordValue();

        // Verify record contains "value"
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("value", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringNotNull_InputEqualsNullString_AddsNull() throws Exception {
        // Setup reusableToken content to "NULL"
        setReusableTokenContent("NULL");

        // format.getNullString() returns "null" (case insensitive)
        when(formatMock.getNullString()).thenReturn("null");

        // Invoke private method addRecordValue
        invokeAddRecordValue();

        // Verify record contains null
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringNotNull_InputNotEqualsNullString_AddsInput() throws Exception {
        // Setup reusableToken content to "data"
        setReusableTokenContent("data");

        // format.getNullString() returns "null"
        when(formatMock.getNullString()).thenReturn("null");

        // Invoke private method addRecordValue
        invokeAddRecordValue();

        // Verify record contains "data"
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("data", record.get(0));
    }

    private void setReusableTokenContent(String contentStr) throws Exception {
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);
        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append(contentStr);
    }

    private void invokeAddRecordValue() throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);
    }

    @SuppressWarnings("unchecked")
    private List<String> getRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        return (List<String>) recordField.get(parser);
    }
}