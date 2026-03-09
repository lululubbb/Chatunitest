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

class CSVParser_5_1Test {

    private CSVParser csvParser;
    private CSVFormat mockFormat;
    private Token mockToken;
    private List<String> recordList;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        mockFormat = mock(CSVFormat.class);

        // Create CSVParser instance with a dummy Reader and mocked CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), mockFormat);

        // Access and replace reusableToken with a mocked Token
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        mockToken = mock(Token.class);
        reusableTokenField.set(csvParser, mockToken);

        // Access the record list
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        //noinspection unchecked
        recordList = (List<String>) recordField.get(csvParser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringIsNull_AddsInputDirectly() throws Exception {
        // Setup reusableToken.content.toString() to return "value"
        Token.Content mockContent = mock(Token.Content.class);
        when(mockToken.content).thenReturn(mockContent);
        when(mockContent.toString()).thenReturn("value");

        // Setup format.getNullString() to return null
        when(mockFormat.getNullString()).thenReturn(null);

        // Invoke private method addRecordValue via reflection
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser);

        // Verify record list contains "value"
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringIsNotNull_InputEqualsNullString_AddsNull() throws Exception {
        // Setup reusableToken.content.toString() to return "null"
        Token.Content mockContent = mock(Token.Content.class);
        when(mockToken.content).thenReturn(mockContent);
        when(mockContent.toString()).thenReturn("null");

        // Setup format.getNullString() to return "null"
        when(mockFormat.getNullString()).thenReturn("null");

        // Invoke private method addRecordValue via reflection
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser);

        // Verify record list contains null
        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_NullStringIsNotNull_InputNotEqualsNullString_AddsInput() throws Exception {
        // Setup reusableToken.content.toString() to return "value"
        Token.Content mockContent = mock(Token.Content.class);
        when(mockToken.content).thenReturn(mockContent);
        when(mockContent.toString()).thenReturn("value");

        // Setup format.getNullString() to return "null"
        when(mockFormat.getNullString()).thenReturn("null");

        // Invoke private method addRecordValue via reflection
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(csvParser);

        // Verify record list contains "value"
        assertEquals(1, recordList.size());
        assertEquals("value", recordList.get(0));
    }
}