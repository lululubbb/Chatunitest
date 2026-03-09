package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @AfterEach
    void tearDown() {
        format = null;
    }

    @Test
    @Timeout(8000)
    void parseFile_NullFile_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((File) null, Charset.defaultCharset(), format));
        assertTrue(exception.getMessage().contains("file"));
    }

    @Test
    @Timeout(8000)
    void parseFile_NullFormat_ThrowsException() throws Exception {
        Path tempFile = Files.createTempFile("csvparser", ".csv");
        Files.write(tempFile, "a,b,c\n1,2,3".getBytes());
        File file = tempFile.toFile();
        try {
            Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(file, Charset.defaultCharset(), null));
            assertTrue(exception.getMessage().contains("format"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @Timeout(8000)
    void parseFile_ValidFile_CreatesParser() throws Exception {
        // Create a real temporary file with content instead of mocking File
        Path tempFile = Files.createTempFile("csvparser", ".csv");
        Files.write(tempFile, "a,b,c\n1,2,3".getBytes());
        File file = tempFile.toFile();
        Charset charset = Charset.defaultCharset();

        try {
            CSVParser parser = CSVParser.parse(file, charset, format);
            assertNotNull(parser);
            assertEqualsFormat(parser, format);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @Timeout(8000)
    void parseInputStream_NullInputStream_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((InputStream) null, Charset.defaultCharset(), format));
        assertTrue(exception.getMessage().contains("inputStream"));
    }

    @Test
    @Timeout(8000)
    void parseInputStream_NullFormat_ThrowsException() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(is, Charset.defaultCharset(), null));
        assertTrue(exception.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parseInputStream_ValidInputStream_CreatesParser() throws Exception {
        InputStream is = new ByteArrayInputStream("a,b,c\n1,2,3".getBytes());
        Charset charset = Charset.defaultCharset();

        CSVParser parser = CSVParser.parse(is, charset, format);
        assertNotNull(parser);
        assertEqualsFormat(parser, format);
    }

    @Test
    @Timeout(8000)
    void parsePath_NullPath_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((Path) null, Charset.defaultCharset(), format));
        assertTrue(exception.getMessage().contains("path"));
    }

    @Test
    @Timeout(8000)
    void parsePath_NullFormat_ThrowsException() throws Exception {
        Path tempFile = Files.createTempFile("csvparser", ".csv");
        try {
            Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(tempFile, Charset.defaultCharset(), null));
            assertTrue(exception.getMessage().contains("format"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @Timeout(8000)
    void parsePath_ValidPath_CreatesParser() throws Exception {
        // Create temporary file with contents
        Path tempFile = Files.createTempFile("csvparser", ".csv");
        Files.write(tempFile, "a,b,c\n1,2,3".getBytes());
        Charset charset = Charset.defaultCharset();

        try {
            CSVParser parser = CSVParser.parse(tempFile, charset, format);
            assertNotNull(parser);
            assertEqualsFormat(parser, format);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @Timeout(8000)
    void parseReader_NullReader_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((Reader) null, format));
        assertTrue(exception.getMessage().contains("reader"));
    }

    @Test
    @Timeout(8000)
    void parseReader_NullFormat_ThrowsException() {
        Reader reader = new StringReader("a,b,c\n1,2,3");
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(reader, null));
        assertTrue(exception.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parseReader_ValidReader_CreatesParser() throws Exception {
        Reader reader = new StringReader("a,b,c\n1,2,3");

        CSVParser parser = CSVParser.parse(reader, format);
        assertNotNull(parser);
        assertEqualsFormat(parser, format);
    }

    @Test
    @Timeout(8000)
    void parseString_NullString_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((String) null, format));
        assertTrue(exception.getMessage().contains("string"));
    }

    @Test
    @Timeout(8000)
    void parseString_NullFormat_ThrowsException() {
        String string = "a,b,c\n1,2,3";
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(string, null));
        assertTrue(exception.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parseString_ValidString_CreatesParser() throws Exception {
        String string = "a,b,c\n1,2,3";

        CSVParser parser = CSVParser.parse(string, format);
        assertNotNull(parser);
        assertEqualsFormat(parser, format);
    }

    @Test
    @Timeout(8000)
    void parseURL_NullURL_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((URL) null, Charset.defaultCharset(), format));
        assertTrue(exception.getMessage().contains("url"));
    }

    @Test
    @Timeout(8000)
    void parseURL_NullFormat_ThrowsException() throws Exception {
        URL url = mock(URL.class);
        Exception exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(url, Charset.defaultCharset(), null));
        assertTrue(exception.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parseURL_ValidURL_CreatesParser() throws Exception {
        URL url = mock(URL.class);
        Charset charset = Charset.defaultCharset();
        InputStream is = new ByteArrayInputStream("a,b,c\n1,2,3".getBytes());

        when(url.openStream()).thenReturn(is);

        CSVParser parser = CSVParser.parse(url, charset, format);
        assertNotNull(parser);
        assertEqualsFormat(parser, format);
    }

    private void assertEqualsFormat(CSVParser parser, CSVFormat expectedFormat) throws Exception {
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertEquals(expectedFormat, actualFormat);
    }
}