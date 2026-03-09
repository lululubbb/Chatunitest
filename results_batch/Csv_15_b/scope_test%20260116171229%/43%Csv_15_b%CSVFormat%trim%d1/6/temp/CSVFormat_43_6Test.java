package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatTrimTest {

    private Method trimMethod;
    private CSVFormat csvFormatInstance;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormatInstance = CSVFormat.DEFAULT;
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
    }

    private CharSequence invokeTrim(CharSequence input) {
        try {
            return (CharSequence) trimMethod.invoke(csvFormatInstance, input);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            // Unwrap and rethrow the cause if it's a RuntimeException or Error
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringInput() {
        String input = "  abc  ";
        CharSequence result = invokeTrim(input);
        assertTrue(result instanceof String);
        assertEquals("abc", result);
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringBuilderInput() {
        StringBuilder input = new StringBuilder("  abc  ");
        CharSequence result = invokeTrim(input);
        assertFalse(result instanceof String);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringBuilderOnlyLeadingSpaces() {
        StringBuilder input = new StringBuilder("  abc");
        CharSequence result = invokeTrim(input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringBuilderOnlyTrailingSpaces() {
        StringBuilder input = new StringBuilder("abc  ");
        CharSequence result = invokeTrim(input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringBuilderNoSpaces() {
        StringBuilder input = new StringBuilder("abc");
        CharSequence result = invokeTrim(input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    void testTrimWithEmptyString() {
        String input = "";
        CharSequence result = invokeTrim(input);
        assertTrue(result instanceof String);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testTrimWithOnlySpaces() {
        StringBuilder input = new StringBuilder("   ");
        CharSequence result = invokeTrim(input);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithControlCharacters() {
        StringBuilder input = new StringBuilder("\n\t abc \r");
        CharSequence result = invokeTrim(input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithSingleCharSpace() {
        StringBuilder input = new StringBuilder(" ");
        CharSequence result = invokeTrim(input);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithNullCharSequence() {
        assertThrows(NullPointerException.class, () -> invokeTrim(null));
    }
}