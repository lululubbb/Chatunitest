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
import java.lang.reflect.Constructor;

class CSVFormat_5_5Test {

    @Test
    @Timeout(8000)
    void testCSVFormatConstructor_AllParameters() throws Exception {
        // Prepare parameters for the private constructor
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

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator,
                nullString, headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase);

        assertNotNull(csvFormat);
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
        assertArrayEquals(new String[]{"comment1", "comment2"}, csvFormat.getHeaderComments());
        assertEquals(skipHeaderRecord, csvFormat.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, csvFormat.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, csvFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testCSVFormatConstructor_NullHeaderAndComments() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(
                ',', '"', null, null, null,
                false, true, "\r\n",
                null, (Object[]) null, null, false,
                false, false);

        assertNotNull(csvFormat);
        assertEquals(',', csvFormat.getDelimiter());
        assertEquals(Character.valueOf('"'), csvFormat.getQuoteCharacter());
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

    @Test
    @Timeout(8000)
    void testCSVFormatConstructor_ValidateCalled() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Using valid parameters to avoid exceptions in validate()
        CSVFormat csvFormat = constructor.newInstance(
                ',', '"', null, null, null,
                false, true, "\r\n",
                null, new Object[0], new String[0], false,
                false, false);

        assertNotNull(csvFormat);
    }
}