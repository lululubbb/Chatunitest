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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Constructor;

class CSVFormat_5_4Test {

    private Constructor<CSVFormat> constructor;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testCSVFormat_AllParameters() throws Exception {
        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[] {"comment1", "comment2"};
        String[] header = new String[] {"col1", "col2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;

        CSVFormat format = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments,
                header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);

        assertEquals(delimiter, format.getDelimiter());
        assertEquals(quoteChar, format.getQuoteCharacter());
        assertEquals(quoteMode, format.getQuoteMode());
        assertEquals(commentStart, format.getCommentMarker());
        assertEquals(escape, format.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, format.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, format.getIgnoreEmptyLines());
        assertEquals(recordSeparator, format.getRecordSeparator());
        assertEquals(nullString, format.getNullString());
        assertArrayEquals(header, format.getHeader());
        assertArrayEquals(new String[] {"comment1", "comment2"}, format.getHeaderComments());
        assertEquals(skipHeaderRecord, format.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, format.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testCSVFormat_NullHeaderAndHeaderComments() throws Exception {
        CSVFormat format = constructor.newInstance(',', null, null, null, null,
                false, true, "\r\n", null, null,
                null, false, false, false);

        assertEquals(',', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
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
    void testCSVFormat_ValidateCalled() throws Exception {
        // This test mainly ensures no exception thrown from validate() in constructor
        CSVFormat format = constructor.newInstance(',', '"', QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null,
                null, false, false, false);
        assertNotNull(format);
    }

    @Test
    @Timeout(8000)
    void testCSVFormat_HeaderCloneIndependence() throws Exception {
        String[] header = new String[] {"a", "b"};
        CSVFormat format = constructor.newInstance(',', null, null, null, null,
                false, true, "\r\n", null, null,
                header, false, false, false);
        String[] retrievedHeader = format.getHeader();
        assertArrayEquals(header, retrievedHeader);
        // Modify original header array after construction
        header[0] = "modified";
        // Retrieved header should not be affected (clone)
        assertNotEquals(header[0], format.getHeader()[0]);
    }

    @Test
    @Timeout(8000)
    void testCSVFormat_HeaderCommentsCloneIndependence() throws Exception {
        Object[] comments = new Object[] {"c1", "c2"};
        CSVFormat format = constructor.newInstance(',', null, null, null, null,
                false, true, "\r\n", null, comments,
                null, false, false, false);
        String[] retrievedComments = format.getHeaderComments();
        assertArrayEquals(new String[] {"c1", "c2"}, retrievedComments);
        comments[0] = "modified";
        assertNotEquals(comments[0], format.getHeaderComments()[0]);
    }

    @Test
    @Timeout(8000)
    void testCSVFormat_NullRecordSeparator() throws Exception {
        CSVFormat format = constructor.newInstance(',', null, null, null, null,
                false, true, null, null, null,
                null, false, false, false);
        assertNull(format.getRecordSeparator());
    }

}