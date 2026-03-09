package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class CSVParserParseFileTest {

    @Test
    @Timeout(8000)
    void parse_nullFile_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((File) null, CSVFormat.DEFAULT));
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_nullFormat_throwsNullPointerException() throws IOException {
        Path tempFile = Files.createTempFile("csvparser-test", ".csv");
        try {
            NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(tempFile.toFile(), null));
            assertEquals("format", ex.getMessage());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @Timeout(8000)
    void parse_validFileAndFormat_returnsCSVParser() throws IOException {
        Path tempFile = Files.createTempFile("csvparser-test", ".csv");
        Files.write(tempFile, "header1,header2\nvalue1,value2".getBytes(StandardCharsets.UTF_8));
        try {
            CSVParser parser = CSVParser.parse(tempFile.toFile(), CSVFormat.DEFAULT.withHeader());
            assertNotNull(parser);
            assertTrue(parser instanceof CSVParser);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}