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
import java.util.Collection;
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

    @BeforeEach
    void setUp() throws Exception {
        mockFormat = mock(CSVFormat.class);
        // Create CSVParser instance with a dummy Reader and mocked format
        csvParser = new CSVParser(new java.io.StringReader(""), mockFormat);

        // Setup reusableToken field with a real Token instance
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        // Token is a package-private class, so instantiate via reflection
        Class<?> tokenClass = Class.forName("org.apache.commons.csv.Token");
        Object token = tokenClass.getDeclaredConstructor().newInstance();
        reusableTokenField.set(csvParser, token);

        // Setup record field to empty ArrayList
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(csvParser, new java.util.ArrayList<String>());
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Arrange
        when(mockFormat.getNullString()).thenReturn(null);

        // Set reusableToken.content to a StringBuilder with some content
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object token = reusableTokenField.get(csvParser);

        Field contentField = token.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(token, new StringBuilder("testValue"));

        // Clear record before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> record = (List<String>) recordField.get(csvParser);
        record.clear();

        // Act
        Method addRecordValue = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValue.setAccessible(true);
        addRecordValue.invoke(csvParser);

        // Assert
        record = (List<String>) recordField.get(csvParser);
        assertEquals(1, record.size());
        assertEquals("testValue", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringMatches_addsNull() throws Exception {
        // Arrange
        when(mockFormat.getNullString()).thenReturn("NULL");

        // Set reusableToken.content to "NULL" (case insensitive match)
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object token = reusableTokenField.get(csvParser);

        Field contentField = token.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(token, new StringBuilder("NULL"));

        // Clear record before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> record = (List<String>) recordField.get(csvParser);
        record.clear();

        // Act
        Method addRecordValue = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValue.setAccessible(true);
        addRecordValue.invoke(csvParser);

        // Assert
        record = (List<String>) recordField.get(csvParser);
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringDoesNotMatch_addsInput() throws Exception {
        // Arrange
        when(mockFormat.getNullString()).thenReturn("NULL");

        // Set reusableToken.content to a different string
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object token = reusableTokenField.get(csvParser);

        Field contentField = token.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(token, new StringBuilder("value"));

        // Clear record before test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> record = (List<String>) recordField.get(csvParser);
        record.clear();

        // Act
        Method addRecordValue = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValue.setAccessible(true);
        addRecordValue.invoke(csvParser);

        // Assert
        record = (List<String>) recordField.get(csvParser);
        assertEquals(1, record.size());
        assertEquals("value", record.get(0));
    }
}