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

public class CSVFormat_56_2Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withSkipHeaderRecord(true);
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertTrue(updated.getSkipHeaderRecord());
        // Original should remain unchanged
        assertFalse(original.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(original.getSkipHeaderRecord());
        CSVFormat updated = original.withSkipHeaderRecord(false);
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertFalse(updated.getSkipHeaderRecord());
        // Original remains unchanged
        assertTrue(original.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordDoesNotChangeOtherFields() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("a", "b")
                .withHeaderComments("cmt1", "cmt2")
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true);

        CSVFormat updated = original.withSkipHeaderRecord(true);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertTrue(updated.getSkipHeaderRecord());

        // Check all other fields unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }
}