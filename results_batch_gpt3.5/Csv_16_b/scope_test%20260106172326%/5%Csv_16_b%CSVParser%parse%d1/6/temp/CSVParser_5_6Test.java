package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CSVParser_5_6Test {

    @Test
    @Timeout(8000)
    void testParse_NullString_ThrowsException() {
        CSVFormat format = mock(CSVFormat.class);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_NullFormat_ThrowsException() {
        String input = "a,b,c";
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(input, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_ValidString_ReturnsParser() throws IOException {
        String input = "a,b,c\n1,2,3";
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(input, format);

        assertNotNull(parser);
        // Verify internal state by reflection
        Object lexer = getField(parser, "lexer");
        assertNotNull(lexer);
        Field readerField;
        try {
            readerField = lexer.getClass().getDeclaredField("reader");
            readerField.setAccessible(true);
            Object reader = readerField.get(lexer);
            assertNotNull(reader);
            assertTrue(reader instanceof StringReader);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed: " + e.getMessage());
        }

        // Check format field is set correctly
        CSVFormat actualFormat = (CSVFormat) getField(parser, "format");
        assertSame(format, actualFormat);
    }

    private Object getField(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                fail("Reflection failed: " + e.getMessage());
                return null;
            }
        }
        fail("Field '" + fieldName + "' not found.");
        return null;
    }
}