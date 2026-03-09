package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_41_2Test {

    private Method trimMethod;

    @BeforeEach
    public void setUp() throws Exception {
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testTrim_withString_trimmed() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim(true);
        String input = "  trimmed string  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("trimmed string", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_withString_noTrimNeeded() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim(true);
        String input = "noTrim";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals(input, result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrim_withNonString_trimNeeded() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim(true);
        CharSequence input = new CharSequence() {
            private final String value = "  abc  ";
            @Override public int length() { return value.length(); }
            @Override public char charAt(int index) { return value.charAt(index); }
            @Override public CharSequence subSequence(int start, int end) { return value.substring(start, end); }
            @Override public String toString() { return value; }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
        assertNotSame(input, result);
    }

    @Test
    @Timeout(8000)
    public void testTrim_withNonString_noTrimNeeded() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim(true);
        CharSequence input = new CharSequence() {
            private final String value = "abc";
            @Override public int length() { return value.length(); }
            @Override public char charAt(int index) { return value.charAt(index); }
            @Override public CharSequence subSequence(int start, int end) { return value.substring(start, end); }
            @Override public String toString() { return value; }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    public void testTrim_withEmptyString() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim(true);
        String input = "   ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_withEmptyNonString() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim(true);
        CharSequence input = new CharSequence() {
            @Override public int length() { return 0; }
            @Override public char charAt(int index) { throw new IndexOutOfBoundsException(); }
            @Override public CharSequence subSequence(int start, int end) { return ""; }
            @Override public String toString() { return ""; }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }
}