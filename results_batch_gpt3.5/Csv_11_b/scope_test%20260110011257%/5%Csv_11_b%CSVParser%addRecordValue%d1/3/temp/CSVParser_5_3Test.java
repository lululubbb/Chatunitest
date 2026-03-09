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

class CSVParser_5_3Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        formatMock = mock(CSVFormat.class);

        // Create a CSVParser instance with a dummy Reader and mocked format
        parser = new CSVParser(new java.io.StringReader("dummy"), formatMock);

        // Set headerMap to empty map to avoid NPE in other code
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, java.util.Collections.emptyMap());

        // Clear record list
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Reset recordNumber
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        // Set format field to mocked CSVFormat
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(parser, formatMock);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringIsNull_AddsInput() throws Exception {
        // Arrange
        when(formatMock.getNullString()).thenReturn(null);

        // Set reusableToken.content to some string "value"
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        // Clear existing content and append "value"
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("value");

        // Clear record list before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Act
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Assert
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringMatchesInput_AddsNull() throws Exception {
        // Arrange
        when(formatMock.getNullString()).thenReturn("NULL");

        // Set reusableToken.content to "NULL" (case insensitive match)
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        // Clear existing content and append "NULL"
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("NULL");

        // Clear record list before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Act
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Assert
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringDoesNotMatchInput_AddsInput() throws Exception {
        // Arrange
        when(formatMock.getNullString()).thenReturn("NULL");

        // Set reusableToken.content to "value" which does not match nullString
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        // Clear existing content and append "value"
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("value");

        // Clear record list before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Act
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);

        // Assert
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }
}