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

class CSVParser_5_4Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        when(formatMock.getNullString()).thenReturn(null);

        // Using the constructor CSVParser(Reader, CSVFormat)
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNull_addsInput() throws Exception {
        // Arrange
        setReusableTokenContent("someValue");
        clearRecordList();

        when(formatMock.getNullString()).thenReturn(null);

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("someValue", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNotNull_inputEqualsNullString_addsNull() throws Exception {
        // Arrange
        setReusableTokenContent("null");
        clearRecordList();

        when(formatMock.getNullString()).thenReturn("null");

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNotNull_inputNotEqualsNullString_addsInput() throws Exception {
        // Arrange
        setReusableTokenContent("value");
        clearRecordList();

        when(formatMock.getNullString()).thenReturn("null");

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("value", record.get(0));
    }

    private void setReusableTokenContent(String content) throws Exception {
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object token = reusableTokenField.get(parser);

        Field contentField = token.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        // Clear existing content and append new content instead of replacing the StringBuilder instance
        StringBuilder sb = (StringBuilder) contentField.get(token);
        sb.setLength(0);
        sb.append(content);
    }

    private void clearRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> record = (List<String>) recordField.get(parser);
        record.clear();
    }

    private List<String> getRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        return (List<String>) recordField.get(parser);
    }

    private void invokeAddRecordValue() throws Exception {
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);
    }
}