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

public class CSVFormat_73_6Test {

    @Test
    @Timeout(8000)
    public void testWithTrimTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat trimmed = original.withTrim(true);
        assertNotNull(trimmed);
        assertTrue(trimmed.getTrim());
        // Original should remain unchanged
        assertFalse(original.getTrim());
        // Check other fields remain the same
        assertEquals(original.getDelimiter(), trimmed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), trimmed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), trimmed.getQuoteMode());
        assertEquals(original.getCommentMarker(), trimmed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), trimmed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), trimmed.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), trimmed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), trimmed.getRecordSeparator());
        assertEquals(original.getNullString(), trimmed.getNullString());
        assertArrayEquals(original.getHeaderComments(), trimmed.getHeaderComments());
        assertArrayEquals(original.getHeader(), trimmed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), trimmed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), trimmed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), trimmed.getIgnoreHeaderCase());
        assertEquals(original.getTrailingDelimiter(), trimmed.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithTrimFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrim(true);
        CSVFormat notTrimmed = original.withTrim(false);
        assertNotNull(notTrimmed);
        assertFalse(notTrimmed.getTrim());
        // Original should remain unchanged
        assertTrue(original.getTrim());
        // Check other fields remain the same
        assertEquals(original.getDelimiter(), notTrimmed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), notTrimmed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), notTrimmed.getQuoteMode());
        assertEquals(original.getCommentMarker(), notTrimmed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), notTrimmed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), notTrimmed.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), notTrimmed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), notTrimmed.getRecordSeparator());
        assertEquals(original.getNullString(), notTrimmed.getNullString());
        assertArrayEquals(original.getHeaderComments(), notTrimmed.getHeaderComments());
        assertArrayEquals(original.getHeader(), notTrimmed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), notTrimmed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), notTrimmed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), notTrimmed.getIgnoreHeaderCase());
        assertEquals(original.getTrailingDelimiter(), notTrimmed.getTrailingDelimiter());
    }
}