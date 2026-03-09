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

class CSVFormatTrimTest {

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
    public void testTrim_withString_trimmed() throws Exception {
        String input = "  abc  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrim_withString_noTrimNeeded() throws Exception {
        String input = "abc";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrim_withNonStringCharSequence_trimBothEnds() throws Exception {
        CharSequence input = new CharSequence() {
            private final String value = "\t abc \n";
            @Override
            public int length() {
                return value.length();
            }
            @Override
            public char charAt(int index) {
                return value.charAt(index);
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return value.subSequence(start, end);
            }
            @Override
            public String toString() {
                return value;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertFalse(result instanceof String);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrim_withNonStringCharSequence_noTrimNeeded() throws Exception {
        CharSequence input = new CharSequence() {
            private final String value = "abc";
            @Override
            public int length() {
                return value.length();
            }
            @Override
            public char charAt(int index) {
                return value.charAt(index);
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return value.subSequence(start, end);
            }
            @Override
            public String toString() {
                return value;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    public void testTrim_withNonStringCharSequence_trimStartOnly() throws Exception {
        CharSequence input = new CharSequence() {
            private final String value = "  abc";
            @Override
            public int length() {
                return value.length();
            }
            @Override
            public char charAt(int index) {
                return value.charAt(index);
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return value.subSequence(start, end);
            }
            @Override
            public String toString() {
                return value;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrim_withNonStringCharSequence_trimEndOnly() throws Exception {
        CharSequence input = new CharSequence() {
            private final String value = "abc  ";
            @Override
            public int length() {
                return value.length();
            }
            @Override
            public char charAt(int index) {
                return value.charAt(index);
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return value.subSequence(start, end);
            }
            @Override
            public String toString() {
                return value;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrim_withEmptyString() throws Exception {
        String input = "";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrim_withWhitespaceOnly() throws Exception {
        String input = "   \t\n  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
    }
}