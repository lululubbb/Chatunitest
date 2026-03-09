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

public class CSVFormat_46_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getIgnoreSurroundingSpaces());

        CSVFormat updated = original.withIgnoreSurroundingSpaces(true);
        assertNotNull(updated);
        assertTrue(updated.getIgnoreSurroundingSpaces());

        // Original remains unchanged
        assertFalse(original.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(original.getIgnoreSurroundingSpaces());

        CSVFormat updated = original.withIgnoreSurroundingSpaces(false);
        assertNotNull(updated);
        assertFalse(updated.getIgnoreSurroundingSpaces());

        // Original remains unchanged
        assertTrue(original.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesDoesNotAffectOtherFields() {
        CSVFormat original = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.ALL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("A", "B")
                .withHeaderComments("header comment")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true);

        CSVFormat updated = original.withIgnoreSurroundingSpaces(true);

        // Check ignoreSurroundingSpaces is updated
        assertTrue(updated.getIgnoreSurroundingSpaces());

        // Check all other fields remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }
}