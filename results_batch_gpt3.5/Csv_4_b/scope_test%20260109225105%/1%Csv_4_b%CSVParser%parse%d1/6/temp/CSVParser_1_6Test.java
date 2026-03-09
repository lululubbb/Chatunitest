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
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

public class CSVParser_1_6Test {

    @Test
    @Timeout(8000)
    void parse_NullFile_ThrowsNullPointerException() {
        CSVFormat format = mock(CSVFormat.class);
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, format);
        });
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        File file = mock(File.class);
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(file, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidFile_ReturnsCSVParser() throws IOException {
        // Instead of mocking File, create a temporary file with content
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("test", ".csv");
        java.nio.file.Files.write(tempFile, "header1,header2\nvalue1,value2".getBytes());
        File file = tempFile.toFile();
        file.deleteOnExit();

        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        CSVParser parser = CSVParser.parse(file, format);
        assertNotNull(parser);
        // Confirm that the parser was created with correct format
        assertSame(format, getField(parser, "format"));
    }

    // Helper method to access private fields using reflection
    private static Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}