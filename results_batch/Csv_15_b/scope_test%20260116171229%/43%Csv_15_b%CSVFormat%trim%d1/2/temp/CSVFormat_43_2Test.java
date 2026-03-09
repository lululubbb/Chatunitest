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

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatTrimTest {

    private Method trimMethod;
    private CSVFormat csvFormatInstance;

    @BeforeEach
    void setUp() throws Exception {
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
        // Use the public static DEFAULT instance instead of constructor
        csvFormatInstance = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testTrim_StringWithLeadingAndTrailingSpaces() throws Exception {
        String input = "  hello world  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormatInstance, input);
        assertEquals("hello world", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    void testTrim_StringWithoutSpaces() throws Exception {
        String input = "hello";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormatInstance, input);
        assertEquals("hello", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    void testTrim_StringOnlySpaces() throws Exception {
        String input = "     ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormatInstance, input);
        assertEquals("", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    void testTrim_StringEmpty() throws Exception {
        String input = "";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormatInstance, input);
        assertEquals("", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceWithLeadingAndTrailingSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String data = "\t\n  abc def  \r\n";

            @Override
            public int length() {
                return data.length();
            }

            @Override
            public char charAt(int index) {
                return data.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return data.subSequence(start, end);
            }

            @Override
            public String toString() {
                return data;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormatInstance, input);
        assertEquals("abc def", result.toString());
        assertFalse(result instanceof String);
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceNoSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String data = "abcdef";

            @Override
            public int length() {
                return data.length();
            }

            @Override
            public char charAt(int index) {
                return data.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return data.subSequence(start, end);
            }

            @Override
            public String toString() {
                return data;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormatInstance, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceOnlySpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String data = " \t\n\r ";

            @Override
            public int length() {
                return data.length();
            }

            @Override
            public char charAt(int index) {
                return data.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return data.subSequence(start, end);
            }

            @Override
            public String toString() {
                return data;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormatInstance, input);
        assertEquals("", result.toString());
        assertFalse(result instanceof String);
    }

    @Test
    @Timeout(8000)
    void testTrim_NullInput() {
        Exception exception = assertThrows(Exception.class, () -> trimMethod.invoke(csvFormatInstance, new Object[]{null}));
        assertNotNull(exception);
    }
}