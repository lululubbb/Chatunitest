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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_5_4Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorAndDefaults() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
            char.class, Character.class, QuoteMode.class, Character.class, Character.class,
            boolean.class, boolean.class, String.class, String.class, Object[].class,
            String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
            boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Normal construction
        CSVFormat format = constructor.newInstance(
            ',', '"', QuoteMode.ALL_NON_NULL, '#', '\\',
            true, false, "\n", "NULL", new Object[]{"comment1", "comment2"},
            new String[]{"header1", "header2"}, true, true, true, true,
            true, true);

        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL_NON_NULL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"header1", "header2"}, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());
        assertTrue(format.getTrim());
        assertTrue(format.getTrailingDelimiter());
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorNullsAndDefaults() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
            char.class, Character.class, QuoteMode.class, Character.class, Character.class,
            boolean.class, boolean.class, String.class, String.class, Object[].class,
            String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
            boolean.class, boolean.class);
        constructor.setAccessible(true);

        // null quoteChar, commentStart, escape, headerComments, header
        CSVFormat format = constructor.newInstance(
            ';', null, null, null, null,
            false, true, "\r\n", null, (Object[]) null,
            null, false, false, false, false,
            false, false);

        assertEquals(';', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
        assertNull(format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertTrue(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
        assertNull(format.getNullString());
        assertNull(format.getHeaderComments());
        assertNull(format.getHeader());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreHeaderCase());
        assertFalse(format.getTrim());
        assertFalse(format.getTrailingDelimiter());
        assertFalse(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorHeaderCommentsToStringArray() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
            char.class, Character.class, QuoteMode.class, Character.class, Character.class,
            boolean.class, boolean.class, String.class, String.class, Object[].class,
            String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
            boolean.class, boolean.class);
        constructor.setAccessible(true);

        Object[] headerCommentsMixed = new Object[] { "comment", 123, null };

        CSVFormat format = constructor.newInstance(
            ',', '"', null, null, null,
            false, true, "\n", null, headerCommentsMixed,
            null, false, false, false, false,
            false, false);

        // The private toStringArray converts each element to String or null
        String[] expected = new String[] { "comment", "123", null };
        assertArrayEquals(expected, format.getHeaderComments());
    }
}