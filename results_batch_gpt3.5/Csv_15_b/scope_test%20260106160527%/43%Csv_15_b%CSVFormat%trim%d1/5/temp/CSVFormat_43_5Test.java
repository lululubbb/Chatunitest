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

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_43_5Test {

    private CSVFormat csvFormat;
    private Method trimMethod;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormat = CSVFormat.DEFAULT;
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testTrim_StringWithLeadingAndTrailingSpaces() throws Exception {
        String input = "  abc  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_StringWithNoSpaces() throws Exception {
        String input = "abc";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_CharSequenceWithLeadingAndTrailingSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String value = "\t abc \n";
            @Override public int length() { return value.length(); }
            @Override public char charAt(int index) { return value.charAt(index); }
            @Override public CharSequence subSequence(int start, int end) { return value.subSequence(start, end); }
            @Override public String toString() { return value; }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
        assertFalse(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_CharSequenceNoLeadingOrTrailingSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String value = "abc";
            @Override public int length() { return value.length(); }
            @Override public char charAt(int index) { return value.charAt(index); }
            @Override public CharSequence subSequence(int start, int end) { return value.subSequence(start, end); }
            @Override public String toString() { return value; }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    public void testTrim_EmptyString() throws Exception {
        String input = "";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_WhitespaceOnly() throws Exception {
        String input = " \t\n\r ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_SingleCharWhitespace() throws Exception {
        CharSequence input = new CharSequence() {
            private final String value = " ";
            @Override public int length() { return value.length(); }
            @Override public char charAt(int index) { return value.charAt(index); }
            @Override public CharSequence subSequence(int start, int end) { return value.subSequence(start, end); }
            @Override public String toString() { return value; }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
        assertFalse(result instanceof String);
    }
}