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
import java.util.Collection;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private File mockFile;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() throws Exception {
        mockFile = mock(File.class);
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_NullFile_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, mockFormat);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockFile, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidFileAndFormat_ReturnsCSVParser() throws IOException {
        // Create a real temp file with minimal CSV content
        java.io.File tempFile = java.io.File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("header1,header2\nvalue1,value2\n");
        }

        // Use real CSVFormat.DEFAULT instead of mock to avoid issues inside CSVParser constructor
        CSVParser parser = CSVParser.parse(tempFile, CSVFormat.DEFAULT);

        assertNotNull(parser);
        assertEquals(CSVFormat.DEFAULT, getField(parser, "format"));
    }

    private <T> T getField(Object instance, String fieldName) {
        try {
            Class<?> clazz = instance.getClass();
            while (clazz != null) {
                try {
                    java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    T value = (T) field.get(instance);
                    return value;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            throw new NoSuchFieldException(fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}