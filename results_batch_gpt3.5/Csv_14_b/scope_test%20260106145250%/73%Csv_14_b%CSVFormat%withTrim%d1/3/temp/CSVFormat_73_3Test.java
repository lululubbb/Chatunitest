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

import org.junit.jupiter.api.Test;

class CSVFormat_73_3Test {

    @Test
    @Timeout(8000)
    void testWithTrimTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat withTrimTrue = original.withTrim(true);

        assertNotNull(withTrimTrue);
        assertTrue(withTrimTrue.getTrim());
        // Original should remain unchanged
        assertFalse(original.getTrim());
        // All other properties should be equal
        assertEquals(original.getDelimiter(), withTrimTrue.getDelimiter());
        assertEquals(original.getQuoteCharacter(), withTrimTrue.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), withTrimTrue.getQuoteMode());
        assertEquals(original.getCommentMarker(), withTrimTrue.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), withTrimTrue.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), withTrimTrue.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), withTrimTrue.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), withTrimTrue.getRecordSeparator());
        assertEquals(original.getNullString(), withTrimTrue.getNullString());
        assertArrayEquals(original.getHeaderComments(), withTrimTrue.getHeaderComments());
        assertArrayEquals(original.getHeader(), withTrimTrue.getHeader());
        assertEquals(original.getSkipHeaderRecord(), withTrimTrue.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), withTrimTrue.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), withTrimTrue.getIgnoreHeaderCase());
        assertEquals(original.getTrailingDelimiter(), withTrimTrue.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrimFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrim(true);
        CSVFormat withTrimFalse = original.withTrim(false);

        assertNotNull(withTrimFalse);
        assertFalse(withTrimFalse.getTrim());
        // Original should remain unchanged
        assertTrue(original.getTrim());
        // All other properties should be equal
        assertEquals(original.getDelimiter(), withTrimFalse.getDelimiter());
        assertEquals(original.getQuoteCharacter(), withTrimFalse.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), withTrimFalse.getQuoteMode());
        assertEquals(original.getCommentMarker(), withTrimFalse.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), withTrimFalse.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), withTrimFalse.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), withTrimFalse.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), withTrimFalse.getRecordSeparator());
        assertEquals(original.getNullString(), withTrimFalse.getNullString());
        assertArrayEquals(original.getHeaderComments(), withTrimFalse.getHeaderComments());
        assertArrayEquals(original.getHeader(), withTrimFalse.getHeader());
        assertEquals(original.getSkipHeaderRecord(), withTrimFalse.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), withTrimFalse.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), withTrimFalse.getIgnoreHeaderCase());
        assertEquals(original.getTrailingDelimiter(), withTrimFalse.getTrailingDelimiter());
    }
}