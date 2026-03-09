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

public class CSVFormat_48_6Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCaseTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreHeaderCase(true);

        assertNotNull(modified);
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertTrue(modified.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCaseFalse() {
        CSVFormat formatWithTrue = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        CSVFormat formatWithFalse = formatWithTrue.withIgnoreHeaderCase(false);

        assertNotNull(formatWithFalse);
        assertEquals(formatWithTrue.getDelimiter(), formatWithFalse.getDelimiter());
        assertEquals(formatWithTrue.getQuoteCharacter(), formatWithFalse.getQuoteCharacter());
        assertEquals(formatWithTrue.getQuoteMode(), formatWithFalse.getQuoteMode());
        assertEquals(formatWithTrue.getCommentMarker(), formatWithFalse.getCommentMarker());
        assertEquals(formatWithTrue.getEscapeCharacter(), formatWithFalse.getEscapeCharacter());
        assertEquals(formatWithTrue.getIgnoreSurroundingSpaces(), formatWithFalse.getIgnoreSurroundingSpaces());
        assertEquals(formatWithTrue.getIgnoreEmptyLines(), formatWithFalse.getIgnoreEmptyLines());
        assertEquals(formatWithTrue.getRecordSeparator(), formatWithFalse.getRecordSeparator());
        assertEquals(formatWithTrue.getNullString(), formatWithFalse.getNullString());
        assertArrayEquals(formatWithTrue.getHeaderComments(), formatWithFalse.getHeaderComments());
        assertArrayEquals(formatWithTrue.getHeader(), formatWithFalse.getHeader());
        assertEquals(formatWithTrue.getSkipHeaderRecord(), formatWithFalse.getSkipHeaderRecord());
        assertEquals(formatWithTrue.getAllowMissingColumnNames(), formatWithFalse.getAllowMissingColumnNames());
        assertFalse(formatWithFalse.getIgnoreHeaderCase());
    }
}