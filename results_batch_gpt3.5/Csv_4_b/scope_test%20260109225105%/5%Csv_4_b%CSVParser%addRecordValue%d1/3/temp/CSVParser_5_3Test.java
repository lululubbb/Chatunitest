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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_5_3Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    public void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        when(formatMock.getNullString()).thenReturn(null);
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Arrange
        setReusableTokenContent("value");
        when(formatMock.getNullString()).thenReturn(null);

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("value", record.get(0));
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_nullStringIsNotNull_inputEqualsNullString_addsNull() throws Exception {
        // Arrange
        setReusableTokenContent("null");
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
    public void testAddRecordValue_nullStringIsNotNull_inputNotEqualsNullString_addsInput() throws Exception {
        // Arrange
        setReusableTokenContent("someValue");
        when(formatMock.getNullString()).thenReturn("null");

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("someValue", record.get(0));
    }

    private void setReusableTokenContent(String content) throws Exception {
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Token reusableToken = (Token) reusableTokenField.get(parser);

        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        // Clear existing content and append new content to the StringBuilder
        StringBuilder contentBuilder = (StringBuilder) contentField.get(reusableToken);
        contentBuilder.setLength(0);
        contentBuilder.append(content);
    }

    private void invokeAddRecordValue() throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);

        // Clear record list before invocation to avoid accumulation from previous tests
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> record = (List<String>) recordField.get(parser);
        record.clear();

        method.invoke(parser);
    }

    @SuppressWarnings("unchecked")
    private List<String> getRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        return (List<String>) recordField.get(parser);
    }
}