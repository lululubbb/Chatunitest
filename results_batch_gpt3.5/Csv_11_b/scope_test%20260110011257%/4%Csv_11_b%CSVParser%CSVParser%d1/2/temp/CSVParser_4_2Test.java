package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CSVParser_4_2Test {

    private CSVFormat format;

    @BeforeEach
    public void setup() {
        format = CSVFormat.DEFAULT.withHeader();  // Add withHeader() to ensure headerMap is initialized properly
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullReader_ThrowsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, format);
        });
        assertEquals("reader", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullFormat_ThrowsException() throws IOException {
        File tempFile = File.createTempFile("tmp", ".csv");
        tempFile.deleteOnExit();
        Reader reader = new FileReader(tempFile);
        try {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> {
                new CSVParser(reader, null);
            });
            assertEquals("format", exception.getMessage());
        } finally {
            reader.close();
        }
    }

    @Test
    @Timeout(8000)
    public void testConstructor_ValidReaderAndFormat_InitializesFields() throws Exception {
        String csvContent = "header1,header2\nvalue1,value2\n";
        Reader reader = new java.io.StringReader(csvContent);

        CSVParser parser = new CSVParser(reader, format);

        // Access private fields using reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(parser);
        assertNotNull(formatValue);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Object lexerValue = lexerField.get(parser);
        assertNotNull(lexerValue);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);
        assertTrue(headerMap.containsKey("header1"));
        assertTrue(headerMap.containsKey("header2"));
    }

    @Test
    @Timeout(8000)
    public void testConstructor_ReaderWithEmptyContent_HeaderMapEmpty() throws Exception {
        Reader reader = new java.io.StringReader("");
        CSVFormat formatWithoutHeader = CSVFormat.DEFAULT.withHeader(); // ensure headerMap initialization
        CSVParser parser = new CSVParser(reader, formatWithoutHeader);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);
        assertTrue(headerMap.isEmpty());
    }
}