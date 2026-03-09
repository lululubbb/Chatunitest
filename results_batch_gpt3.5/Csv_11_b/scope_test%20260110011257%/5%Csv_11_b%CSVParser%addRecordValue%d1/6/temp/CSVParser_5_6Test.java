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

class CSVParser_5_6Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setup() throws Exception {
        formatMock = mock(CSVFormat.class);
        when(formatMock.getNullString()).thenReturn(null);
        parser = new CSVParser(new java.io.StringReader(""), formatMock);
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNull_addsInput() throws Exception {
        // Arrange
        String testInput = "testValue";
        setReusableTokenContent(testInput);
        when(formatMock.getNullString()).thenReturn(null);

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals(testInput, record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringNotNull_inputEqualsNullString_addsNull() throws Exception {
        // Arrange
        String nullString = "NULL";
        String testInput = "null"; // case insensitive match
        setReusableTokenContent(testInput);
        when(formatMock.getNullString()).thenReturn(nullString);

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
        String nullString = "NULL";
        String testInput = "someValue";
        setReusableTokenContent(testInput);
        when(formatMock.getNullString()).thenReturn(nullString);

        // Act
        invokeAddRecordValue();

        // Assert
        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals(testInput, record.get(0));
    }

    private void setReusableTokenContent(String content) throws Exception {
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);

        // Clear the existing StringBuilder and set new content
        StringBuilder contentBuilder = (StringBuilder) contentField.get(reusableToken);
        contentBuilder.setLength(0);
        contentBuilder.append(content);
    }

    private void invokeAddRecordValue() throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);
    }

    @SuppressWarnings("unchecked")
    private List<String> getRecordList() throws Exception {
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> record = (List<String>) recordField.get(parser);
        return record;
    }
}