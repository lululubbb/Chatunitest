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

class CSVParser_2_1Test {

    @Test
    @Timeout(8000)
    void testParse_withValidInputStreamAndCharsetAndFormat() throws IOException {
        // Arrange
        String csvContent = "header1,header2\nvalue1,value2\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader();

        // Act
        try (CSVParser result = CSVParser.parse(inputStream, StandardCharsets.UTF_8, format)) {

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getRecords().size());
            assertEquals("value1", result.getRecords().get(0).get("header1"));
            assertEquals("value2", result.getRecords().get(0).get("header2"));
        }
    }
}