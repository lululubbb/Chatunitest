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
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class CSVParser_2_5Test {

    @Test
    @Timeout(8000)
    void parse_InputStreamCharsetFormat_ShouldReturnCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Arrange
        String csvContent = "header1,header2\nvalue1,value2\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        Charset charset = StandardCharsets.UTF_8;
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        // Act
        CSVParser parser = CSVParser.parse(inputStream, charset, format);

        // Assert
        assertNotNull(parser);
        // Use reflection to access private field 'format'
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertArrayEquals(format.getHeader(), actualFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), actualFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void parse_InputStreamNullInputStream_ShouldThrowNullPointerException() {
        Charset charset = StandardCharsets.UTF_8;
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((InputStream) null, charset, format);
        });
        assertTrue(exception.getMessage().contains("inputStream"));
    }

    @Test
    @Timeout(8000)
    void parse_InputStreamNullFormat_ShouldThrowNullPointerException() {
        String csvContent = "header1,header2\nvalue1,value2\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        Charset charset = StandardCharsets.UTF_8;

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(inputStream, charset, null);
        });
        assertTrue(exception.getMessage().contains("format"));
    }
}