package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private CSVFormat mockFormat;
    private Charset charset;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
        charset = Charset.defaultCharset();
    }

    @Test
    @Timeout(8000)
    void parse_Path_NullPath_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((Path) null, charset, mockFormat));
        assertTrue(ex.getMessage().contains("path"));
    }

    @Test
    @Timeout(8000)
    void parse_Path_NullFormat_Throws() throws IOException {
        Path mockPath = mock(Path.class);
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(mockPath, charset, null));
        assertTrue(ex.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parse_Path_DelegatesToReader() throws Exception {
        Path tempFile = Files.createTempFile("csvparser_test", ".csv");
        try {
            Files.write(tempFile, "a,b,c\n1,2,3\n".getBytes(charset));

            CSVParser actual = CSVParser.parse(tempFile, charset, CSVFormat.DEFAULT);
            assertNotNull(actual);
            actual.close();
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @Timeout(8000)
    void parse_File_NullFile_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((File) null, charset, mockFormat));
        assertTrue(ex.getMessage().contains("file"));
    }

    @Test
    @Timeout(8000)
    void parse_File_NullFormat_Throws() {
        File file = mock(File.class);
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(file, charset, null));
        assertTrue(ex.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parse_InputStream_NullInputStream_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((InputStream) null, charset, mockFormat));
        assertTrue(ex.getMessage().contains("inputStream"));
    }

    @Test
    @Timeout(8000)
    void parse_InputStream_NullFormat_Throws() {
        InputStream is = mock(InputStream.class);
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(is, charset, null));
        assertTrue(ex.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parse_Reader_NullReader_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((Reader) null, mockFormat));
        assertTrue(ex.getMessage().contains("reader"));
    }

    @Test
    @Timeout(8000)
    void parse_Reader_NullFormat_Throws() {
        Reader reader = mock(Reader.class);
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(reader, null));
        assertTrue(ex.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parse_String_NullString_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((String) null, mockFormat));
        assertTrue(ex.getMessage().contains("string"));
    }

    @Test
    @Timeout(8000)
    void parse_String_NullFormat_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse("a,b,c", null));
        assertTrue(ex.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void parse_URL_NullURL_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((URL) null, charset, mockFormat));
        assertTrue(ex.getMessage().contains("url"));
    }

    @Test
    @Timeout(8000)
    void parse_URL_NullFormat_Throws() {
        URL url = mock(URL.class);
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(url, charset, null));
        assertTrue(ex.getMessage().contains("format"));
    }

}