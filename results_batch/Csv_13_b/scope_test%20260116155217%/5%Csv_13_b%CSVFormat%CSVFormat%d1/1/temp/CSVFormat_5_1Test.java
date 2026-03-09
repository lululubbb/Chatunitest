package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_5_1Test {

    private Constructor<CSVFormat> constructor;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
    }

    private CSVFormat createInstance(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                     Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
                                     boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                     Object[] headerComments, String[] header, boolean skipHeaderRecord,
                                     boolean allowMissingColumnNames, boolean ignoreHeaderCase)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Wrap headerComments and header in Object[] and String[] respectively to avoid varargs confusion
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_Defaults() throws Exception {
        CSVFormat format = createInstance(',', '"', null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false);
        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertNull(format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertTrue(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
        assertNull(format.getNullString());
        assertNull(format.getHeader());
        assertNull(format.getHeaderComments());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_WithAllParameters() throws Exception {
        Object[] headerComments = new Object[] {"comment1", "comment2"};
        String[] header = new String[] {"h1", "h2"};
        CSVFormat format = createInstance(';', '\'', QuoteMode.ALL, '#', '\\',
                true, false, "\n", "NULL", headerComments, header,
                true, true, true);

        assertEquals(';', format.getDelimiter());
        assertEquals(Character.valueOf('\''), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[] {"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(header, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_HeaderCloneIndependence() throws Exception {
        String[] header = new String[] {"col1", "col2"};
        CSVFormat format = createInstance(',', '"', null, null, null,
                false, true, "\r\n", null, null, header,
                false, false, false);
        assertNotSame(header, format.getHeader());
        header[0] = "changed";
        assertNotEquals(header[0], format.getHeader()[0]);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_HeaderCommentsToStringArray() throws Exception {
        Object[] headerComments = new Object[] {1, "two", null};
        CSVFormat format = createInstance(',', '"', null, null, null,
                false, true, "\r\n", null, headerComments, null,
                false, false, false);
        String[] comments = format.getHeaderComments();
        assertEquals(3, comments.length);
        assertEquals("1", comments[0]);
        assertEquals("two", comments[1]);
        assertNull(comments[2]);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_ValidateCalled() throws Exception {
        // validate() is private void and no exception means success
        CSVFormat format = createInstance(',', '"', null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false);
        assertNotNull(format);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullRecordSeparator() throws Exception {
        // recordSeparator can be null, but validate might throw
        // We test with null and expect no exception (assuming validate allows null)
        CSVFormat format = createInstance(',', '"', null, null, null,
                false, true, null, null, null, null,
                false, false, false);
        assertNull(format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_EmptyHeader() throws Exception {
        String[] header = new String[0];
        CSVFormat format = createInstance(',', '"', null, null, null,
                false, true, "\r\n", null, null, header,
                false, false, false);
        assertNotNull(format.getHeader());
        assertEquals(0, format.getHeader().length);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullHeaderComments() throws Exception {
        CSVFormat format = createInstance(',', '"', null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false);
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_QuoteCharNull() throws Exception {
        CSVFormat format = createInstance(',', null, null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false);
        assertNull(format.getQuoteCharacter());
    }
}