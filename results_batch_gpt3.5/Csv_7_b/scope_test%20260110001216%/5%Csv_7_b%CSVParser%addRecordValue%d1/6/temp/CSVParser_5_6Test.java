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

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_5_6Test {

    private CSVParser csvParser;
    private CSVFormat mockFormat;
    private Token reusableToken;

    @BeforeEach
    void setUp() throws Exception {
        mockFormat = mock(CSVFormat.class);
        when(mockFormat.getNullString()).thenReturn(null); // default behavior

        // Create CSVParser instance with a dummy Reader and mockFormat
        csvParser = new CSVParser(new java.io.StringReader(""), mockFormat);

        // Access reusableToken field and get the Token instance
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableToken = (Token) reusableTokenField.get(csvParser);

        // Access record field and clear it before each test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(csvParser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Setup reusableToken.content by clearing and appending new content
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("someValue");

        // format.getNullString() returns null
        when(mockFormat.getNullString()).thenReturn(null);

        // Invoke private method addRecordValue using reflection
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser);

        // Verify that record contains the input string "someValue"
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(csvParser);

        assertEquals(1, recordList.size());
        assertEquals("someValue", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringEqualsInput_addsNull() throws Exception {
        // Setup reusableToken.content by clearing and appending new content
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("NULLVALUE");

        // format.getNullString() returns "nullvalue" (case insensitive match)
        when(mockFormat.getNullString()).thenReturn("nullvalue");

        // Invoke private method addRecordValue using reflection
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser);

        // Verify that record contains null
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(csvParser);

        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNotEqualsInput_addsInput() throws Exception {
        // Setup reusableToken.content by clearing and appending new content
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("someOtherValue");

        // format.getNullString() returns "nullvalue" (no match)
        when(mockFormat.getNullString()).thenReturn("nullvalue");

        // Invoke private method addRecordValue using reflection
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser);

        // Verify that record contains the input string "someOtherValue"
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(csvParser);

        assertEquals(1, recordList.size());
        assertEquals("someOtherValue", recordList.get(0));
    }
}