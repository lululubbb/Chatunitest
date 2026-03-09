package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_1Test {

    private CSVFormat formatMock;
    private Reader reader;
    private CSVParser parser;

    @BeforeEach
    public void setup() {
        formatMock = mock(CSVFormat.class);
        // Mock behavior for formatMock if needed to avoid NPE in CSVParser internals
        when(formatMock.getHeader()).thenReturn(null);
        reader = new StringReader("header1,header2\nvalue1,value2");
    }

    @Test
    @Timeout(8000)
    public void testConstructor_withValidReaderAndFormat() throws Exception {
        // Use the static parse method instead of the constructor to avoid IOException
        parser = CSVParser.parse(reader, formatMock);

        assertNotNull(parser);

        // Validate fields using reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(formatMock, formatField.get(parser));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        assertNotNull(lexerField.get(parser));

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Object headerMap = headerMapField.get(parser);
        assertNotNull(headerMap);
        assertTrue(headerMap instanceof Map);

        Field characterOffsetField = CSVParser.class.getDeclaredField("characterOffset");
        characterOffsetField.setAccessible(true);
        assertEquals(0L, characterOffsetField.getLong(parser));

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        // The recordNumber field is initialized as recordNumber - 1 = 0
        assertEquals(0L, recordNumberField.getLong(parser));
    }

    @Test
    @Timeout(8000)
    public void testConstructor_withValidReaderFormatOffsetRecordNumber() throws Exception {
        Reader reader2 = new StringReader("header1,header2\nvalue1,value2");

        Constructor<CSVParser> ctor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class, long.class, long.class);
        ctor.setAccessible(true);
        CSVParser parser2 = ctor.newInstance(reader2, formatMock, 10L, 5L);

        assertNotNull(parser2);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(formatMock, formatField.get(parser2));

        Field characterOffsetField = CSVParser.class.getDeclaredField("characterOffset");
        characterOffsetField.setAccessible(true);
        assertEquals(10L, characterOffsetField.getLong(parser2));

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        // recordNumber initialized to recordNumber - 1
        assertEquals(4L, recordNumberField.getLong(parser2));
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullReader_throwsException() {
        Exception ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((Reader) null, formatMock);
        });
        assertTrue(ex.getMessage().contains("reader"));
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullFormat_throwsException() {
        Exception ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(reader, null);
        });
        assertTrue(ex.getMessage().contains("format"));
    }
}