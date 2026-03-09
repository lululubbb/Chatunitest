package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class CSVParser_2_4Test {

    @Test
    @Timeout(8000)
    void testParse_withValidInputStreamAndCharsetAndFormat() throws IOException {
        String csvContent = "header1,header2\nvalue1,value2\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        CSVParser result = CSVParser.parse(inputStream, StandardCharsets.UTF_8, format);

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testParse_withNullInputStream_throwsException() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        InputStream inputStream = null;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            CSVParser.parse(inputStream, StandardCharsets.UTF_8, format);
        });
        assertTrue(thrown.getMessage().contains("inputStream"));
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsException() {
        String csvContent = "a,b\n1,2\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        CSVFormat format = null;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            CSVParser.parse(inputStream, StandardCharsets.UTF_8, format);
        });
        assertTrue(thrown.getMessage().contains("format"));
    }
}