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
import org.junit.jupiter.api.BeforeEach;

public class CSVFormat_61_2Test {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCaseTrue() {
        CSVFormat newFormat = defaultFormat.withIgnoreHeaderCase(true);
        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreHeaderCase());
        // Other properties remain unchanged
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(defaultFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(defaultFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(defaultFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), newFormat.getNullString());

        // headerComments and header can be null, so handle null safely for assertArrayEquals
        if (defaultFormat.getHeaderComments() == null && newFormat.getHeaderComments() == null) {
            // both null, OK
        } else {
            assertArrayEquals(defaultFormat.getHeaderComments(), newFormat.getHeaderComments());
        }

        if (defaultFormat.getHeader() == null && newFormat.getHeader() == null) {
            // both null, OK
        } else {
            assertArrayEquals(defaultFormat.getHeader(), newFormat.getHeader());
        }

        assertEquals(defaultFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getTrim(), newFormat.getTrim());
        assertEquals(defaultFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(defaultFormat.getAutoFlush(), newFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCaseFalse() {
        // Create a format with ignoreHeaderCase = true first
        CSVFormat formatWithTrue = defaultFormat.withIgnoreHeaderCase(true);
        assertTrue(formatWithTrue.getIgnoreHeaderCase());

        // Then switch to false
        CSVFormat formatWithFalse = formatWithTrue.withIgnoreHeaderCase(false);
        assertNotNull(formatWithFalse);
        assertFalse(formatWithFalse.getIgnoreHeaderCase());

        // Other properties remain unchanged
        assertEquals(formatWithTrue.getDelimiter(), formatWithFalse.getDelimiter());
        assertEquals(formatWithTrue.getQuoteCharacter(), formatWithFalse.getQuoteCharacter());
        assertEquals(formatWithTrue.getQuoteMode(), formatWithFalse.getQuoteMode());
        assertEquals(formatWithTrue.getCommentMarker(), formatWithFalse.getCommentMarker());
        assertEquals(formatWithTrue.getEscapeCharacter(), formatWithFalse.getEscapeCharacter());
        assertEquals(formatWithTrue.getIgnoreSurroundingSpaces(), formatWithFalse.getIgnoreSurroundingSpaces());
        assertEquals(formatWithTrue.getIgnoreEmptyLines(), formatWithFalse.getIgnoreEmptyLines());
        assertEquals(formatWithTrue.getRecordSeparator(), formatWithFalse.getRecordSeparator());
        assertEquals(formatWithTrue.getNullString(), formatWithFalse.getNullString());

        if (formatWithTrue.getHeaderComments() == null && formatWithFalse.getHeaderComments() == null) {
            // both null, OK
        } else {
            assertArrayEquals(formatWithTrue.getHeaderComments(), formatWithFalse.getHeaderComments());
        }

        if (formatWithTrue.getHeader() == null && formatWithFalse.getHeader() == null) {
            // both null, OK
        } else {
            assertArrayEquals(formatWithTrue.getHeader(), formatWithFalse.getHeader());
        }

        assertEquals(formatWithTrue.getSkipHeaderRecord(), formatWithFalse.getSkipHeaderRecord());
        assertEquals(formatWithTrue.getAllowMissingColumnNames(), formatWithFalse.getAllowMissingColumnNames());
        assertEquals(formatWithTrue.getTrim(), formatWithFalse.getTrim());
        assertEquals(formatWithTrue.getTrailingDelimiter(), formatWithFalse.getTrailingDelimiter());
        assertEquals(formatWithTrue.getAutoFlush(), formatWithFalse.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCaseDoesNotModifyOriginal() {
        CSVFormat newFormat = defaultFormat.withIgnoreHeaderCase(true);
        assertNotSame(defaultFormat, newFormat);
        assertFalse(defaultFormat.getIgnoreHeaderCase());
        assertTrue(newFormat.getIgnoreHeaderCase());
    }
}