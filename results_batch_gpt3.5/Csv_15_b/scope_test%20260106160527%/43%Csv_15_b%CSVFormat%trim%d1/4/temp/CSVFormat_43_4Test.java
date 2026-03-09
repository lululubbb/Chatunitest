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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatTrimTest {

    private Method trimMethod;
    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormat = CSVFormat.DEFAULT;
        trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testTrimWithStringWithSpaces() throws Exception {
        String input = "  abc  ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrimWithStringNoSpaces() throws Exception {
        String input = "abc";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    public void testTrimWithStringAllSpaces() throws Exception {
        String input = "     ";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
    }

    private static class CharSequenceImpl implements CharSequence {
        private final char[] chars;

        CharSequenceImpl(String s) {
            this.chars = s.toCharArray();
        }

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
            return new String(chars, start, end - start);
        }

        @Override
        public String toString() {
            return new String(chars);
        }
    }

    @Test
    @Timeout(8000)
    public void testTrimWithCharSequenceLeadingAndTrailingSpaces() throws Exception {
        CharSequence input = new CharSequenceImpl("  abc  ");
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrimWithCharSequenceNoSpaces() throws Exception {
        CharSequence input = new CharSequenceImpl("abc");
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    public void testTrimWithCharSequenceAllSpaces() throws Exception {
        CharSequence input = new CharSequenceImpl("     ");
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrimWithCharSequenceOnlyLeadingSpaces() throws Exception {
        CharSequence input = new CharSequenceImpl("   abc");
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrimWithCharSequenceOnlyTrailingSpaces() throws Exception {
        CharSequence input = new CharSequenceImpl("abc   ");
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("abc", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testTrimWithEmptyString() throws Exception {
        String input = "";
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }

    @Test
    @Timeout(8000)
    public void testTrimWithEmptyCharSequence() throws Exception {
        CharSequence input = new CharSequenceImpl("");
        CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertSame(input, result);
    }
}