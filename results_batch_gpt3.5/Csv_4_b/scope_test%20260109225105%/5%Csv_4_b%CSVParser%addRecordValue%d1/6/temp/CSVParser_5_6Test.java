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

class CSVParser_5_6Test {

    private CSVParser parser;
    private CSVFormat formatMock;
    private Token token;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        when(formatMock.getNullString()).thenReturn(null);

        // Create CSVParser instance with mocks and setup fields via reflection
        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Access private reusableToken field
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        token = (Token) reusableTokenField.get(parser);

        // Set token.content to a new StringBuilder
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(token, new StringBuilder());

        // Clear record list
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        when(formatMock.getNullString()).thenReturn(null);
        setTokenContent("someValue");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("someValue", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringMatchesInput_addsNull() throws Exception {
        when(formatMock.getNullString()).thenReturn("NULL");
        setTokenContent("NULL");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringDoesNotMatchInput_addsInput() throws Exception {
        when(formatMock.getNullString()).thenReturn("NULL");
        setTokenContent("notNull");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("notNull", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringCaseInsensitive() throws Exception {
        when(formatMock.getNullString()).thenReturn("Null");
        setTokenContent("NULL");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    private void setTokenContent(String content) throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(token, new StringBuilder(content));
    }

    private void invokeAddRecordValue() throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);
    }

    private List<String> getRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        return (List<String>) recordField.get(parser);
    }
}