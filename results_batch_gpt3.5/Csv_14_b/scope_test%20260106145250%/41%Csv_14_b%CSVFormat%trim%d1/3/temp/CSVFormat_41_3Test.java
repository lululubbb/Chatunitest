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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatTrimTest {

    private CSVFormat csvFormat;
    private Method trimMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT;
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringContainingSpaces() throws InvocationTargetException, IllegalAccessException {
        String input = "  abc  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringNoSpaces() throws InvocationTargetException, IllegalAccessException {
        String input = "abc";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithStringOnlySpaces() throws InvocationTargetException, IllegalAccessException {
        String input = "   ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertTrue(result instanceof String);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithNonStringCharSequenceLeadingAndTrailingSpaces() throws InvocationTargetException, IllegalAccessException {
        CharSequence input = new CharSequence() {
            private final String delegate = "\t abc \n";

            @Override
            public int length() {
                return delegate.length();
            }

            @Override
            public char charAt(int index) {
                return delegate.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return delegate.subSequence(start, end);
            }

            @Override
            public String toString() {
                return delegate;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertNotSame(input, result);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithNonStringCharSequenceNoSpaces() throws InvocationTargetException, IllegalAccessException {
        CharSequence input = new CharSequence() {
            private final String delegate = "abc";

            @Override
            public int length() {
                return delegate.length();
            }

            @Override
            public char charAt(int index) {
                return delegate.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return delegate.subSequence(start, end);
            }

            @Override
            public String toString() {
                return delegate;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithNonStringCharSequenceOnlySpaces() throws InvocationTargetException, IllegalAccessException {
        CharSequence input = new CharSequence() {
            private final String delegate = "   ";

            @Override
            public int length() {
                return delegate.length();
            }

            @Override
            public char charAt(int index) {
                return delegate.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return delegate.subSequence(start, end);
            }

            @Override
            public String toString() {
                return delegate;
            }
        };
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertNotSame(input, result);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimWithEmptyCharSequence() throws InvocationTargetException, IllegalAccessException {
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
        assertEquals(0, result.length());
    }
}