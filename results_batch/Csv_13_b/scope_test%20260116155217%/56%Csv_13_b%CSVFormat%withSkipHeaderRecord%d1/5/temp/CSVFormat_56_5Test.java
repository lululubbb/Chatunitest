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

class CSVFormat_56_5Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withSkipHeaderRecord(true);

        assertNotSame(original, modified);
        assertTrue(modified.getSkipHeaderRecord());

        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(
            original.getHeaderComments() == null ? new String[0] : original.getHeaderComments(),
            modified.getHeaderComments() == null ? new String[0] : modified.getHeaderComments());
        assertArrayEquals(
            original.getHeader() == null ? new String[0] : original.getHeader(),
            modified.getHeader() == null ? new String[0] : modified.getHeader());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getNullString(), modified.getNullString());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat modified = original.withSkipHeaderRecord(false);

        assertNotSame(original, modified);
        assertFalse(modified.getSkipHeaderRecord());

        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(
            original.getHeaderComments() == null ? new String[0] : original.getHeaderComments(),
            modified.getHeaderComments() == null ? new String[0] : modified.getHeaderComments());
        assertArrayEquals(
            original.getHeader() == null ? new String[0] : original.getHeader(),
            modified.getHeader() == null ? new String[0] : modified.getHeader());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getNullString(), modified.getNullString());
    }

}