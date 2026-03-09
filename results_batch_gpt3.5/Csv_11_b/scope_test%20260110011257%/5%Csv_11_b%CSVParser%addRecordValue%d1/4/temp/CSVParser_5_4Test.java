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

class CSVParser_5_4Test {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        when(formatMock.getNullString()).thenReturn(null);

        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        // Clear record list before each test
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNull_addsInput() throws Exception {
        when(formatMock.getNullString()).thenReturn(null);

        setReusableTokenContent("value");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("value", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNotNull_inputEqualsNullString_addsNull() throws Exception {
        when(formatMock.getNullString()).thenReturn("NULL");

        setReusableTokenContent("NULL");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertNull(record.get(0));
    }

    @Test
    @Timeout(8000)
    void testAddRecordValue_nullStringIsNotNull_inputNotEqualsNullString_addsInput() throws Exception {
        when(formatMock.getNullString()).thenReturn("NULL");

        setReusableTokenContent("value");

        invokeAddRecordValue();

        List<String> record = getRecordList();
        assertEquals(1, record.size());
        assertEquals("value", record.get(0));
    }

    private void setReusableTokenContent(String content) throws Exception {
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        Object reusableToken = reusableTokenField.get(parser);

        Field contentField = reusableToken.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(reusableToken, new StringBuilder(content));
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