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

class CSVParser_8_4Test {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullReader_ThrowsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, format, 0L, 1L);
        });
        assertEquals("reader", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullFormat_ThrowsException() {
        Reader reader = new StringReader("a,b,c");
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVParser(reader, null, 0L, 1L);
        });
        assertEquals("format", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_InitializesFields() throws Exception {
        String csvData = "header1,header2\nvalue1,value2";
        Reader reader = new StringReader(csvData);

        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT, 5L, 10L);

        // Check fields via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertNotNull(formatField.get(parser));

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);
        assertTrue(headerMap.containsKey("header1"));
        assertTrue(headerMap.containsKey("header2"));

        Field characterOffsetField = CSVParser.class.getDeclaredField("characterOffset");
        characterOffsetField.setAccessible(true);
        long characterOffset = (long) characterOffsetField.get(parser);
        assertEquals(5L, characterOffset);

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        long recordNumber = (long) recordNumberField.get(parser);
        assertEquals(9L, recordNumber); // 10L - 1

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Object lexer = lexerField.get(parser);
        assertNotNull(lexer);
    }
}