package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_9_6Test {

    private CSVParser csvParser;
    private CSVFormat mockFormat;
    private Token reusableToken;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat
        mockFormat = mock(CSVFormat.class);
        when(mockFormat.getTrim()).thenReturn(false);
        when(mockFormat.getTrailingDelimiter()).thenReturn(false);
        when(mockFormat.getNullString()).thenReturn(null);

        // Create CSVParser instance using constructor with Reader and CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), mockFormat);

        // Access reusableToken field and set it up
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        reusableToken = (Token) tokenField.get(csvParser);

        // Clear recordList for clean state
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(csvParser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_normalInput_notLastRecord() throws Exception {
        // Modify reusableToken.content's internal StringBuilder content instead of reassigning
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("value1");

        // Mock format behavior
        when(mockFormat.getTrim()).thenReturn(true);
        when(mockFormat.getTrailingDelimiter()).thenReturn(false);
        when(mockFormat.getNullString()).thenReturn(null);

        // Invoke private method addRecordValue with lastRecord = false
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(csvParser, false);

        // Verify recordList contains trimmed "value1"
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(csvParser);

        assertEquals(1, recordList.size());
        assertEquals("value1", recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_trimTrue_andNullString() throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("  NULL  ");

        when(mockFormat.getTrim()).thenReturn(true);
        when(mockFormat.getTrailingDelimiter()).thenReturn(false);
        when(mockFormat.getNullString()).thenReturn("NULL");

        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(csvParser, false);

        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(csvParser);

        assertEquals(1, recordList.size());
        assertNull(recordList.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_emptyInput_lastRecordTrailingDelimiterTrue() throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("   ");

        when(mockFormat.getTrim()).thenReturn(true);
        when(mockFormat.getTrailingDelimiter()).thenReturn(true);
        when(mockFormat.getNullString()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(csvParser, true);

        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(csvParser);

        // Since lastRecord is true, inputClean is empty and trailingDelimiter is true, method returns early
        assertEquals(0, recordList.size());
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_emptyInput_lastRecordTrailingDelimiterFalse() throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder content = (StringBuilder) contentField.get(reusableToken);
        content.setLength(0);
        content.append("   ");

        when(mockFormat.getTrim()).thenReturn(true);
        when(mockFormat.getTrailingDelimiter()).thenReturn(false);
        when(mockFormat.getNullString()).thenReturn(null);

        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(csvParser, true);

        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(csvParser);

        // recordList should contain empty string because trailingDelimiter is false
        assertEquals(1, recordList.size());
        assertEquals("", recordList.get(0));
    }
}