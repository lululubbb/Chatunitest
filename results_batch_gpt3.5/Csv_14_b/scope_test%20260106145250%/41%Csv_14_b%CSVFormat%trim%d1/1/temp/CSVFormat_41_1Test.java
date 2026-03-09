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

public class CSVFormat_41_1Test {

    private CSVFormat csvFormat;
    private Method trimMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT;
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testTrim_StringInput() throws InvocationTargetException, IllegalAccessException {
        String input = "  abc  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
        assertTrue(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_CharSequenceWithLeadingAndTrailingSpaces() throws InvocationTargetException, IllegalAccessException {
        CharSequence input = new CharSequence() {
            private final String delegate = "\t  test string \n";

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
        assertEquals("test string", result.toString());
        assertFalse(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_CharSequence_NoLeadingOrTrailingSpaces() throws InvocationTargetException, IllegalAccessException {
        CharSequence input = new CharSequence() {
            private final String delegate = "noSpaces";

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
        // The trim method returns the original CharSequence when no trimming is needed
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    public void testTrim_CharSequence_AllWhitespace() throws InvocationTargetException, IllegalAccessException {
        CharSequence input = new CharSequence() {
            private final String delegate = " \t\n\r ";

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
        assertEquals("", result.toString());
        assertFalse(result instanceof String);
    }

    @Test
    @Timeout(8000)
    public void testTrim_CharSequence_Empty() throws InvocationTargetException, IllegalAccessException {
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