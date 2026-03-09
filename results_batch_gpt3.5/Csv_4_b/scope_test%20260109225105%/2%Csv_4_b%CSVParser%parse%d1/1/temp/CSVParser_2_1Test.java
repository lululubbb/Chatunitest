package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class CSVParser_2_1Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_shouldCreateParser() throws IOException {
        // Arrange
        String csvData = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader(); // Use a real CSVFormat instead of mock

        // Act
        CSVParser parser = CSVParser.parse(csvData, format);

        // Assert
        assertNotNull(parser);
        assertFalse(parser.isClosed());
        assertEquals(format, getField(parser, "format"));
        assertNotNull(getField(parser, "lexer"));
        assertEquals(0L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullString_shouldThrowException() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            // Disambiguate parse(String, CSVFormat) by reflection
            Method parseMethod = CSVParser.class.getMethod("parse", String.class, CSVFormat.class);
            try {
                parseMethod.invoke(null, (String) null, format);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
        assertEquals("string", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_shouldThrowException() {
        String csvData = "a,b";
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            // Disambiguate parse(String, CSVFormat) by reflection
            Method parseMethod = CSVParser.class.getMethod("parse", String.class, CSVFormat.class);
            try {
                parseMethod.invoke(null, csvData, null);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
        assertEquals("format", ex.getMessage());
    }

    // Helper method to access private fields via reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object instance, String fieldName) {
        try {
            java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}