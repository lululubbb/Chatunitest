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
import org.apache.commons.csv.CSVFormat;
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
    void testTrimWithStringContainingSpaces() throws Exception {
        String input = "  abc def  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("abc def", result);
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringNoSpaces() throws Exception {
        String input = "abcdef";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("abcdef", result);
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringOnlySpaces() throws Exception {
        String input = "     ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringEmpty() throws Exception {
        String input = "";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testTrimWithNonStringCharSequenceLeadingTrailingSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final char[] chars = "  abc def  ".toCharArray();
            @Override
            public int length() {
                return chars.length;
            }
            @Override
            public char charAt(int index) {
                return chars[index];
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return new CharSequence() {
                    private final char[] subChars = java.util.Arrays.copyOfRange(chars, start, end);
                    @Override
                    public int length() {
                        return subChars.length;
                    }
                    @Override
                    public char charAt(int index) {
                        return subChars[index];
                    }
                    @Override
                    public CharSequence subSequence(int s, int e) {
                        return new String(subChars, s, e - s);
                    }
                    @Override
                    public String toString() {
                        return new String(subChars);
                    }
                };
            }
            @Override
            public String toString() {
                return new String(chars);
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertFalse(result instanceof String);
        assertEquals("abc def", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithNonStringCharSequenceNoSpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final char[] chars = "abcdef".toCharArray();
            @Override
            public int length() {
                return chars.length;
            }
            @Override
            public char charAt(int index) {
                return chars[index];
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return new CharSequence() {
                    private final char[] subChars = java.util.Arrays.copyOfRange(chars, start, end);
                    @Override
                    public int length() {
                        return subChars.length;
                    }
                    @Override
                    public char charAt(int index) {
                        return subChars[index];
                    }
                    @Override
                    public CharSequence subSequence(int s, int e) {
                        return new String(subChars, s, e - s);
                    }
                    @Override
                    public String toString() {
                        return new String(subChars);
                    }
                };
            }
            @Override
            public String toString() {
                return new String(chars);
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    void testTrimWithNonStringCharSequenceOnlySpaces() throws Exception {
        CharSequence input = new CharSequence() {
            private final char[] chars = "     ".toCharArray();
            @Override
            public int length() {
                return chars.length;
            }
            @Override
            public char charAt(int index) {
                return chars[index];
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return new CharSequence() {
                    private final char[] subChars = java.util.Arrays.copyOfRange(chars, start, end);
                    @Override
                    public int length() {
                        return subChars.length;
                    }
                    @Override
                    public char charAt(int index) {
                        return subChars[index];
                    }
                    @Override
                    public CharSequence subSequence(int s, int e) {
                        return new String(subChars, s, e - s);
                    }
                    @Override
                    public String toString() {
                        return new String(subChars);
                    }
                };
            }
            @Override
            public String toString() {
                return new String(chars);
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertFalse(result instanceof String);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithNonStringCharSequenceEmpty() throws Exception {
        CharSequence input = new CharSequence() {
            @Override
            public int length() {
                return 0;
            }
            @Override
            public char charAt(int index) {
                throw new IndexOutOfBoundsException();
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return "";
            }
            @Override
            public String toString() {
                return "";
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }
}