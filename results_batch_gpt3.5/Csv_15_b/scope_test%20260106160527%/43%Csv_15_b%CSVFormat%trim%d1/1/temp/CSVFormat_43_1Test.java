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

class CSVFormatTrimTest {

    private CSVFormat csvFormat;
    private Method trimMethod;

    @BeforeEach
    void setUp() throws Exception {
        csvFormat = CSVFormat.DEFAULT;
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testTrim_StringInstance() throws Exception {
        String input = "  abc  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceWithLeadingAndTrailingSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String s = "\t abc \n";

            @Override
            public int length() {
                return s.length();
            }

            @Override
            public char charAt(int index) {
                return s.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return s.subSequence(start, end);
            }

            @Override
            public String toString() {
                return s;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
        assertNotSame(input, result);
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceNoTrimNeeded() throws Exception {
        CharSequence input = new CharSequence() {
            private final String s = "abc";

            @Override
            public int length() {
                return s.length();
            }

            @Override
            public char charAt(int index) {
                return s.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return s.subSequence(start, end);
            }

            @Override
            public String toString() {
                return s;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    void testTrim_EmptyString() throws Exception {
        String input = "    ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    void testTrim_CharSequenceAllSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final String s = " \t\n\r ";

            @Override
            public int length() {
                return s.length();
            }

            @Override
            public char charAt(int index) {
                return s.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return s.subSequence(start, end);
            }

            @Override
            public String toString() {
                return s;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
    }
}