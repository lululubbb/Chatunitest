package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

import java.io.File;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_1_4Test {

    private File file;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        file = mock(File.class);
        charset = Charset.defaultCharset();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testParse_file_success() throws Exception {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("test", ".csv");
        java.nio.file.Files.write(tempFile, "header1,header2\nvalue1,value2\n".getBytes(charset));

        File realFile = tempFile.toFile();

        CSVFormat realFormat = CSVFormat.DEFAULT.withHeader();

        CSVParser parser = CSVParser.parse(realFile, charset, realFormat);
        assertNotNull(parser);
        assertNotNull(parser.getHeaderMap());

        parser.close();

        java.nio.file.Files.deleteIfExists(tempFile);
    }

    @Test
    @Timeout(8000)
    void testParse_file_nullFile() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((File)null, charset, format));
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_file_nullFormat() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(file, charset, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_file_nullCharset() throws Exception {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("test", ".csv");
        java.nio.file.Files.write(tempFile, "header1,header2\nvalue1,value2\n".getBytes(Charset.defaultCharset()));
        File realFile = tempFile.toFile();

        CSVFormat realFormat = CSVFormat.DEFAULT.withHeader();

        // Using reflection to call the parse method to avoid compile-time null charset check
        java.lang.reflect.Method parseMethod = CSVParser.class.getDeclaredMethod("parse", File.class, Charset.class, CSVFormat.class);
        parseMethod.setAccessible(true);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            try {
                parseMethod.invoke(null, realFile, null, realFormat);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
        assertNotNull(ex);

        java.nio.file.Files.deleteIfExists(tempFile);
    }
}