package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CSVParser_5_3Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsCSVParser() throws IOException {
        String csvData = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(csvData, format);

        assertNotNull(parser);
        assertEquals(format, getField(parser, "format"));
        assertFalse(parser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullString_throwsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;
        NullPointerException exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((String) null, format));
        assertEquals("string", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsNullPointerException() {
        String csvData = "header1,header2\nvalue1,value2";
        NullPointerException exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(csvData, (CSVFormat) null));
        assertEquals("format", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_internalConstructorCalledWithStringReader() throws IOException {
        String csvData = "a,b,c\n1,2,3";
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(csvData, format);
        assertNotNull(parser);
        assertFalse(parser.isClosed());
    }

    // Utility method to get private fields via reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object instance, String fieldName) {
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (T) field.get(instance);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                fail("Reflection error accessing field: " + fieldName);
                return null;
            }
        }
        fail("Field not found: " + fieldName);
        return null;
    }
}