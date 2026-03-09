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

class CSVFormat_44_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(true);

        assertNotSame(original, modified);
        assertTrue(modified.getIgnoreEmptyLines());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());

        // headerComments and header can be null, so handle null safely
        String[] originalHeaderComments = original.getHeaderComments();
        String[] modifiedHeaderComments = modified.getHeaderComments();
        if (originalHeaderComments == null && modifiedHeaderComments == null) {
            // both null - ok
        } else {
            assertArrayEquals(originalHeaderComments, modifiedHeaderComments);
        }

        String[] originalHeader = original.getHeader();
        String[] modifiedHeader = modified.getHeader();
        if (originalHeader == null && modifiedHeader == null) {
            // both null - ok
        } else {
            assertArrayEquals(originalHeader, modifiedHeader);
        }

        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        CSVFormat modified = original.withIgnoreEmptyLines(false);

        assertNotSame(original, modified);
        assertFalse(modified.getIgnoreEmptyLines());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());

        // headerComments and header can be null, so handle null safely
        String[] originalHeaderComments = original.getHeaderComments();
        String[] modifiedHeaderComments = modified.getHeaderComments();
        if (originalHeaderComments == null && modifiedHeaderComments == null) {
            // both null - ok
        } else {
            assertArrayEquals(originalHeaderComments, modifiedHeaderComments);
        }

        String[] originalHeader = original.getHeader();
        String[] modifiedHeader = modified.getHeader();
        if (originalHeader == null && modifiedHeader == null) {
            // both null - ok
        } else {
            assertArrayEquals(originalHeader, modifiedHeader);
        }

        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }
}