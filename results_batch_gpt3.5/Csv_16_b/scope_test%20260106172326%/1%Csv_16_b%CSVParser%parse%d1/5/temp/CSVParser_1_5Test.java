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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    @AfterEach
    void tearDown() {
        // No static mocks to close since mockStatic is not used
    }

    @Test
    @Timeout(8000)
    void parse_withFileAndCharsetAndFormat_shouldReturnCSVParser() throws Exception {
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.DEFAULT;

        Path tempFile = Files.createTempFile("test", ".csv");
        Files.write(tempFile, "a,b\n1,2".getBytes(charset));
        File realFile = tempFile.toFile();

        CSVParser parser = CSVParser.parse(realFile, charset, format);
        assertNotNull(parser);

        Files.deleteIfExists(tempFile);
    }

    @Test
    @Timeout(8000)
    void parse_withInputStreamAndCharsetAndFormat_shouldReturnCSVParser() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("a,b\n1,2".getBytes(Charset.defaultCharset()));
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(inputStream, charset, format);
        assertNotNull(parser);
    }

    @Test
    @Timeout(8000)
    void parse_withPathAndCharsetAndFormat_shouldReturnCSVParser() throws Exception {
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.DEFAULT;

        Path tempFile = Files.createTempFile("test", ".csv");
        Files.write(tempFile, "a,b\n1,2".getBytes(charset));

        CSVParser parser = CSVParser.parse(tempFile, charset, format);
        assertNotNull(parser);

        Files.deleteIfExists(tempFile);
    }

    @Test
    @Timeout(8000)
    void parse_withReaderAndFormat_shouldReturnCSVParser() throws Exception {
        String csv = "a,b\n1,2";
        Reader reader = new StringReader(csv);
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(reader, format);
        assertNotNull(parser);
    }

    @Test
    @Timeout(8000)
    void parse_withStringAndFormat_shouldReturnCSVParser() throws Exception {
        String csv = "a,b\n1,2";
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(csv, format);
        assertNotNull(parser);
    }

    @Test
    @Timeout(8000)
    void parse_withUrlAndCharsetAndFormat_shouldReturnCSVParser() throws Exception {
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.DEFAULT;

        Path tempFile = Files.createTempFile("test", ".csv");
        Files.write(tempFile, "a,b\n1,2".getBytes(charset));
        URL url = tempFile.toUri().toURL();

        CSVParser parser = CSVParser.parse(url, charset, format);
        assertNotNull(parser);

        Files.deleteIfExists(tempFile);
    }
}