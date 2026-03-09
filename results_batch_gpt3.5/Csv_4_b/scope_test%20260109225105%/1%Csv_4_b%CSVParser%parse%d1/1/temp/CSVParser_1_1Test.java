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
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_1_1Test {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = CSVFormat.DEFAULT.withHeader(); // Use a real CSVFormat instance instead of mock
    }

    @Test
    @Timeout(8000)
    void parse_withNullFile_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((File) null, format));
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullFormat_throwsNullPointerException() throws IOException {
        File file = File.createTempFile("test", ".csv");
        file.deleteOnExit();
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(file, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withValidFileAndFormat_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Create a real temporary file with some content to avoid FileNotFoundException
        java.io.File tempFile = java.io.File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("header1,header2\nvalue1,value2\n");
        }

        CSVFormat realFormat = CSVFormat.DEFAULT.withHeader();

        CSVParser parser = CSVParser.parse(tempFile, realFormat);
        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertEquals(realFormat, actualFormat);
    }
}