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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_5_2Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_andValidate() throws Exception {
        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[]{"comment1", "comment2"};
        String[] header = new String[]{"col1", "col2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;

        // Construct instance
        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);

        // Verify fields via public getters
        assertEquals(delimiter, csvFormat.getDelimiter());
        assertEquals(quoteChar, csvFormat.getQuoteCharacter());
        assertEquals(quoteMode, csvFormat.getQuoteMode());
        assertEquals(commentStart, csvFormat.getCommentMarker());
        assertEquals(escape, csvFormat.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, csvFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, csvFormat.getIgnoreEmptyLines());
        assertEquals(recordSeparator, csvFormat.getRecordSeparator());
        assertEquals(nullString, csvFormat.getNullString());
        assertArrayEquals(header, csvFormat.getHeader());
        assertTrue(csvFormat.getAllowMissingColumnNames());
        assertEquals(skipHeaderRecord, csvFormat.getSkipHeaderRecord());
        assertEquals(ignoreHeaderCase, csvFormat.getIgnoreHeaderCase());
        assertNotNull(csvFormat.getHeaderComments());
        assertEquals(2, csvFormat.getHeaderComments().length);
        assertEquals("comment1", csvFormat.getHeaderComments()[0]);
        assertEquals("comment2", csvFormat.getHeaderComments()[1]);
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_nullHeaderAndNullHeaderComments() throws Exception {
        // Test with null header and null headerComments
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(',', null, null, null, null,
                false, true, "\r\n", null, (Object[]) null,
                null, false, false, false);

        assertEquals(',', csvFormat.getDelimiter());
        assertNull(csvFormat.getQuoteCharacter());
        assertNull(csvFormat.getQuoteMode());
        assertNull(csvFormat.getCommentMarker());
        assertNull(csvFormat.getEscapeCharacter());
        assertFalse(csvFormat.getIgnoreSurroundingSpaces());
        assertTrue(csvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", csvFormat.getRecordSeparator());
        assertNull(csvFormat.getNullString());
        assertNull(csvFormat.getHeader());
        assertNull(csvFormat.getHeaderComments());
        assertFalse(csvFormat.getSkipHeaderRecord());
        assertFalse(csvFormat.getAllowMissingColumnNames());
        assertFalse(csvFormat.getIgnoreHeaderCase());
    }
}