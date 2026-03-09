package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CSVParser_4_1Test {

    @Test
    @Timeout(8000)
    void testParse_withValidReaderAndFormat_shouldCreateCSVParser() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT; // Use a real CSVFormat instance instead of a mock
        String csvData = "a,b,c\n1,2,3";
        Reader reader = new StringReader(csvData);

        CSVParser parser = CSVParser.parse(reader, format);

        assertNotNull(parser);
        assertFalse(parser.isClosed());
        assertEquals(format, getField(parser, "format"));

        Object lexer = getField(parser, "lexer");
        try {
            Class<?> lexerClass = lexer.getClass();
            Field[] fields = lexerClass.getDeclaredFields();
            Reader lexerReader = null;
            for (Field field : fields) {
                if (Reader.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    lexerReader = (Reader) field.get(lexer);
                    break;
                }
            }
            assertSame(reader, lexerReader);
        } catch (Exception e) {
            fail("Reflection error: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_shouldThrowNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT; // Use a real CSVFormat instance instead of a mock
        assertThrows(NullPointerException.class, () -> CSVParser.parse((Reader) null, format));
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_shouldThrowNullPointerException() {
        String csvData = "a,b,c\n1,2,3";
        Reader reader = new StringReader(csvData);
        assertThrows(NullPointerException.class, () -> CSVParser.parse(reader, null));
    }

    private Object getField(Object instance, String fieldName) {
        try {
            Class<?> clazz = instance.getClass();
            while (clazz != null) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field.get(instance);
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            throw new RuntimeException("Field '" + fieldName + "' not found in class hierarchy.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}