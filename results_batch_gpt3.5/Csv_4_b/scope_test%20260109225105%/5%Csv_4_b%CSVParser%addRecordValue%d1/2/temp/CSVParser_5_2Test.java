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

class CSVParser_5_2Test {

    private CSVParser csvParser;
    private CSVFormat mockFormat;
    private Token mockToken;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        mockFormat = mock(CSVFormat.class);

        // Create CSVParser instance with Reader and mocked CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), mockFormat);

        // Set reusableToken to a fresh Token instance
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        mockToken = new Token();
        reusableTokenField.set(csvParser, mockToken);

        // Clear record list
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(csvParser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Arrange
        when(mockFormat.getNullString()).thenReturn(null);
        setReusableTokenContent("value1");

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("value1", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_inputEqualsNullStringCaseInsensitive_addsNull() throws Exception {
        // Arrange
        when(mockFormat.getNullString()).thenReturn("NULL");
        setReusableTokenContent("null");

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_inputNotEqualsNullString_addsInput() throws Exception {
        // Arrange
        when(mockFormat.getNullString()).thenReturn("NULL");
        setReusableTokenContent("value2");

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("value2", record.get(0));
    }

    private void setReusableTokenContent(String content) throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        // Clear existing content and append new content instead of replacing StringBuilder instance
        StringBuilder sb = (StringBuilder) contentField.get(mockToken);
        sb.setLength(0);
        sb.append(content);
    }

    private void invokeAddRecordValue() throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        // Before invoking, clear the record list to avoid accumulation from previous tests
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(csvParser);
        recordList.clear();

        method.invoke(csvParser);
    }

    private List<String> getRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        return (List<String>) recordField.get(csvParser);
    }
}