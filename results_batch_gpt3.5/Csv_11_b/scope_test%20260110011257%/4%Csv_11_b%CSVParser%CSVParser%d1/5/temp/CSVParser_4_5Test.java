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
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVParser_4_5Test {

    private CSVFormat format;

    @BeforeEach
    public void setUp() {
        format = mock(CSVFormat.class);
        // Setup default behavior for format mock if needed
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullReader_throwsException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, format);
        });
        assertEquals("reader", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullFormat_throwsException() throws IOException {
        Reader reader = mock(Reader.class);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVParser(reader, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_initializesFields() throws Exception {
        File tempFile = createTempCsvFile();
        try (Reader reader = new FileReader(tempFile, StandardCharsets.UTF_8)) {
            CSVFormat realFormat = CSVFormat.DEFAULT;

            CSVParser parser = new CSVParser(reader, realFormat);

            // Verify format field
            Field formatField = CSVParser.class.getDeclaredField("format");
            formatField.setAccessible(true);
            assertSame(realFormat, formatField.get(parser));

            // Verify lexer field is not null
            Field lexerField = CSVParser.class.getDeclaredField("lexer");
            lexerField.setAccessible(true);
            Object lexer = lexerField.get(parser);
            assertNotNull(lexer);
            assertTrue(lexer instanceof Lexer);

            // Verify headerMap field is not null
            Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
            headerMapField.setAccessible(true);
            Object headerMap = headerMapField.get(parser);
            assertNotNull(headerMap);
            assertTrue(headerMap instanceof Map);
        }
    }

    private File createTempCsvFile() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();
        try (java.io.FileWriter writer = new java.io.FileWriter(tempFile, StandardCharsets.UTF_8)) {
            writer.write("header1,header2\nvalue1,value2\n");
        }
        return tempFile;
    }
}