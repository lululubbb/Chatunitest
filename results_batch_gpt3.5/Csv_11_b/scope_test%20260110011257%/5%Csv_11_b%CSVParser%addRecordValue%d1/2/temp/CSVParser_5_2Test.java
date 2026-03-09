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

public class CSVParser_5_2Test {

    private CSVParser parser;
    private CSVFormat formatMock;
    private Token token;

    @BeforeEach
    public void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        when(formatMock.getNullString()).thenReturn(null);

        // Create CSVParser instance with Reader and CSVFormat constructor
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Access reusableToken field and Token class
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        token = (Token) reusableTokenField.get(parser);

        // Clear record list before each test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_NullStringIsNull_AddsInput() throws Exception {
        // Setup token content to a string
        setTokenContent("value");

        // nullString is null
        when(formatMock.getNullString()).thenReturn(null);

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("value", record.get(0));
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_NullStringIsNotNull_InputEqualsNullString_AddsNull() throws Exception {
        setTokenContent("NULL");

        when(formatMock.getNullString()).thenReturn("NULL");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_NullStringIsNotNull_InputNotEqualsNullString_AddsInput() throws Exception {
        setTokenContent("something");

        when(formatMock.getNullString()).thenReturn("NULL");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("something", record.get(0));
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_NullStringIsNotNull_InputEqualsNullStringIgnoreCase_AddsNull() throws Exception {
        setTokenContent("null");

        when(formatMock.getNullString()).thenReturn("NULL");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    // Helper to set reusableToken.content to given string
    private void setTokenContent(String content) throws Exception {
        // reusableToken.content is a StringBuilder
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder sb = (StringBuilder) contentField.get(token);
        sb.setLength(0);
        sb.append(content);
    }

    // Helper to invoke private addRecordValue method
    private void invokeAddRecordValue() throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);
    }

    // Helper to get record list
    private List<String> getRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        return (List<String>) recordField.get(parser);
    }
}