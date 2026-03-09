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

import org.junit.jupiter.api.Test;

class CSVParserParseFileTest {

    @Test
    @Timeout(8000)
    void testParseFile_NullFile_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, CSVFormat.DEFAULT);
        });
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseFile_NullFormat_ThrowsException() throws IOException {
        File tempFile = File.createTempFile("csvparser", ".csv");
        tempFile.deleteOnExit();

        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(tempFile, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseFile_Success() throws IOException {
        // Create a temp file with valid CSV content to avoid FileReader error
        File tempFile = File.createTempFile("csvparser", ".csv");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), "header1,header2\nvalue1,value2\n".getBytes(StandardCharsets.UTF_8));

        CSVParser parser = CSVParser.parse(tempFile, CSVFormat.DEFAULT.withHeader());
        assertNotNull(parser);
        // We cannot verify internals here but ensure no exceptions thrown
    }
}