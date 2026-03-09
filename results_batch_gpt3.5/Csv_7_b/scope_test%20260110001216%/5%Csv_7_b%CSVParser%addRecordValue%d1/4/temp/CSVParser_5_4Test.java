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
import static org.junit.jupiter.api.Assertions.*;

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
        // Using a dummy Reader and CSVFormat to create CSVParser instance
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        // Arrange
        when(formatMock.getNullString()).thenReturn(null);

        // Set reusableToken.content to a StringBuilder with some content
        setReusableTokenContent("testValue");

        // Clear record list before test
        clearRecordList();

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("testValue", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNotNull_inputEqualsNullString_addsNull() throws Exception {
        // Arrange
        when(formatMock.getNullString()).thenReturn("NULL");

        setReusableTokenContent("NULL"); // case insensitive match

        clearRecordList();

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
        when(formatMock.getNullString()).thenReturn("NULL");

        setReusableTokenContent("someValue");

        clearRecordList();

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("someValue", record.get(0));
    }

    // Helper methods for reflection

    private void setReusableTokenContent(String content) throws Exception {
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(reusableToken, new StringBuilder(content));
    }

    @SuppressWarnings("unchecked")
    private List<String> getRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        return (List<String>) recordField.get(parser);
    }

    private void clearRecordList() throws Exception {
        List<String> record = getRecordList();
        record.clear();
    }

    private void invokeAddRecordValue() throws Exception {
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);
    }
}