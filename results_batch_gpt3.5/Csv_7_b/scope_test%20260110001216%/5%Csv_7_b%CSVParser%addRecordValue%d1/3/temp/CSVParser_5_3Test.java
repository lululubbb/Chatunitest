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

public class CSVParser_5_3Test {

    private CSVParser parser;
    private CSVFormat mockFormat;
    private Token mockToken;
    private List<String> recordList;

    @BeforeEach
    public void setUp() throws Exception {
        // Mock CSVFormat
        mockFormat = mock(CSVFormat.class);

        // Create parser instance with a dummy Reader and mocked format
        parser = new CSVParser(new java.io.StringReader(""), mockFormat);

        // Access private final field 'reusableToken' via reflection
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        mockToken = (Token) tokenField.get(parser);

        // Access private final field 'record' via reflection
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordList = (List<String>) recordField.get(parser);

        // Clear record list before each test
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Setup reusableToken.content to return "testValue"
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = new StringBuilder("testValue");
        contentField.set(mockToken, content);

        // format.getNullString() returns null
        when(mockFormat.getNullString()).thenReturn(null);

        // Invoke private method addRecordValue
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);

        // Verify record contains "testValue"
        assertEquals(1, recordList.size());
        assertEquals("testValue", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_nullStringNotNull_inputEqualsNullString_addsNull() throws Exception {
        // Setup reusableToken.content to "null"
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = new StringBuilder("null");
        contentField.set(mockToken, content);

        // format.getNullString() returns "null"
        when(mockFormat.getNullString()).thenReturn("null");

        // Invoke private method addRecordValue
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);

        // Verify record contains null (case insensitive match)
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    public void testAddRecordValue_nullStringNotNull_inputNotEqualsNullString_addsInput() throws Exception {
        // Setup reusableToken.content to "value"
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = new StringBuilder("value");
        contentField.set(mockToken, content);

        // format.getNullString() returns "null"
        when(mockFormat.getNullString()).thenReturn("null");

        // Invoke private method addRecordValue
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);

        // Verify record contains "value"
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }
}