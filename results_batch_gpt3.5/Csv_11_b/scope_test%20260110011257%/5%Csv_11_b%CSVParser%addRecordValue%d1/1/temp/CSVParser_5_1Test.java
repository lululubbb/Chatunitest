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
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_5_1Test {

    private CSVParser parser;
    private CSVFormat formatMock;
    private Token token;
    private List<String> recordList;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        formatMock = mock(CSVFormat.class);

        // Create parser instance with dummy Reader and mocked format
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Use reflection to access private fields and set them up
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordList = new ArrayList<>();
        recordField.set(parser, recordList);

        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        token = (Token) reusableTokenField.get(parser);

        // Clear token content before each test
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(token);
        content.setLength(0);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Setup format.getNullString() to return null
        when(formatMock.getNullString()).thenReturn(null);

        // Set token content
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(token);
        content.setLength(0);
        content.append("someValue");

        // Invoke private method addRecordValue
        Method addRecordValue = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValue.setAccessible(true);
        addRecordValue.invoke(parser);

        // Verify record contains input string
        assertEquals(1, recordList.size());
        assertEquals("someValue", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNotNull_inputEqualsNullString_addsNull() throws Exception {
        // Setup format.getNullString() to return "NULL"
        when(formatMock.getNullString()).thenReturn("NULL");

        // Set token content equal to nullString ignoring case
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(token);
        content.setLength(0);
        content.append("null");

        // Invoke private method addRecordValue
        Method addRecordValue = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValue.setAccessible(true);
        addRecordValue.invoke(parser);

        // Verify record contains null
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNotNull_inputNotEqualsNullString_addsInput() throws Exception {
        // Setup format.getNullString() to return "NULL"
        when(formatMock.getNullString()).thenReturn("NULL");

        // Set token content different from nullString
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(token);
        content.setLength(0);
        content.append("value");

        // Invoke private method addRecordValue
        Method addRecordValue = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValue.setAccessible(true);
        addRecordValue.invoke(parser);

        // Verify record contains input string
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }
}