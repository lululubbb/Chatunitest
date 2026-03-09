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

    @BeforeEach
    void setUp() throws Exception {
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testTrim_StringWithLeadingAndTrailingSpaces() throws Exception {
        String input = "  abc  ";
        Object result = trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertTrue(result instanceof CharSequence);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrim_StringWithoutSpaces() throws Exception {
        String input = "abc";
        Object result = trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertTrue(result instanceof CharSequence);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrim_StringAllSpaces() throws Exception {
        String input = "    ";
        Object result = trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertTrue(result instanceof CharSequence);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceWithLeadingAndTrailingSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String str = "  abc  ";
            @Override
            public int length() { return str.length(); }
            @Override
            public char charAt(int index) { return str.charAt(index); }
            @Override
            public CharSequence subSequence(int start, int end) { return str.subSequence(start, end); }
            @Override
            public String toString() { return str; }
        };
        Object result = trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertTrue(result instanceof CharSequence);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceWithoutSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String str = "abc";
            @Override
            public int length() { return str.length(); }
            @Override
            public char charAt(int index) { return str.charAt(index); }
            @Override
            public CharSequence subSequence(int start, int end) { return str.subSequence(start, end); }
            @Override
            public String toString() { return str; }
        };
        Object result = trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertTrue(result instanceof CharSequence);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceAllSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String str = "    ";
            @Override
            public int length() { return str.length(); }
            @Override
            public char charAt(int index) { return str.charAt(index); }
            @Override
            public CharSequence subSequence(int start, int end) { return str.subSequence(start, end); }
            @Override
            public String toString() { return str; }
        };
        Object result = trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertTrue(result instanceof CharSequence);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrim_EmptyString() throws Exception {
        String input = "";
        Object result = trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertTrue(result instanceof CharSequence);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrim_EmptyCharSequence() throws Exception {
        CharSequence input = new CharSequence() {
            @Override
            public int length() { return 0; }
            @Override
            public char charAt(int index) { throw new IndexOutOfBoundsException(); }
            @Override
            public CharSequence subSequence(int start, int end) { return ""; }
            @Override
            public String toString() { return ""; }
        };
        Object result = trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertTrue(result instanceof CharSequence);
        assertSame(input, result);
    }
}