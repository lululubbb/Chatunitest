package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
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

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_NullString_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, mockFormat);
        });
        assertEquals("string", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse("some string", null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidInputs_ReturnsCSVParser() throws IOException {
        String input = "a,b,c\n1,2,3";
        CSVParser parser = CSVParser.parse(input, mockFormat);
        assertNotNull(parser);
        assertEquals(mockFormat, getField(parser, "format"));
        // Verify that the reader inside lexer is a StringReader with the input string
        assertEquals(input, readFieldStringReader(parser, "lexer"));
    }

    // Helper method to get private field value via reflection, traversing superclasses if needed
    private Object getField(Object instance, String fieldName) {
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(instance);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Field '" + fieldName + "' not found in class hierarchy.");
    }

    // Helper method to read the input string from the Lexer (via its Reader)
    private String readFieldStringReader(CSVParser parser, String lexerFieldName) {
        try {
            Object lexer = getField(parser, lexerFieldName);
            Class<?> lexerClass = lexer.getClass();
            Field readerField = null;
            Class<?> current = lexerClass;
            while (current != null) {
                try {
                    readerField = current.getDeclaredField("reader");
                    readerField.setAccessible(true);
                    break;
                } catch (NoSuchFieldException e) {
                    current = current.getSuperclass();
                }
            }
            if (readerField == null) {
                throw new RuntimeException("Field 'reader' not found in lexer class hierarchy.");
            }
            Object reader = readerField.get(lexer);
            if (reader instanceof StringReader) {
                // StringReader does not expose the string directly, so use reflection on StringReader
                Class<?> srClass = StringReader.class;
                Field strField = null;
                Class<?> srCurrent = srClass;
                while (srCurrent != null) {
                    try {
                        strField = srCurrent.getDeclaredField("str");
                        strField.setAccessible(true);
                        break;
                    } catch (NoSuchFieldException e) {
                        srCurrent = srCurrent.getSuperclass();
                    }
                }
                if (strField == null) {
                    throw new RuntimeException("Field 'str' not found in StringReader class hierarchy.");
                }
                return (String) strField.get(reader);
            }
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}