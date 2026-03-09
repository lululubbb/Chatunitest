package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_8_6Test {

    private CSVFormat formatMock;
    private Reader readerMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withValidParameters_initializesFields() throws Exception {
        // Use a StringReader with some CSV content for realistic test
        String csvContent = "header1,header2\nvalue1,value2\n";
        Reader stringReader = new StringReader(csvContent);
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        // Create instance using constructor under test
        CSVParser parser = new CSVParser(stringReader, format, 10L, 5L);

        // Assert fields via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertEquals(format, formatField.get(parser));

        Field characterOffsetField = CSVParser.class.getDeclaredField("characterOffset");
        characterOffsetField.setAccessible(true);
        assertEquals(10L, characterOffsetField.getLong(parser));

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        // recordNumber is set to recordNumber - 1
        assertEquals(4L, recordNumberField.getLong(parser));

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);
        assertTrue(headerMap.containsKey("header1"));
        assertTrue(headerMap.containsKey("header2"));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Object lexer = lexerField.get(parser);
        assertNotNull(lexer);
        // Lexer class is package-private, so just check class simple name
        assertEquals("Lexer", lexer.getClass().getSimpleName());

        // recordList should be empty initially
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.List<?> recordList = (java.util.List<?>) recordListField.get(parser);
        assertNotNull(recordList);
        assertTrue(recordList.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullReader_throwsException() {
        CSVFormat format = CSVFormat.DEFAULT;
        Reader nullReader = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CSVParser(nullReader, format, 0L, 1L));
        assertEquals("reader", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        Reader reader = new StringReader("a,b\n1,2\n");
        CSVFormat nullFormat = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CSVParser(reader, nullFormat, 0L, 1L));
        assertEquals("format", exception.getMessage());
    }
}