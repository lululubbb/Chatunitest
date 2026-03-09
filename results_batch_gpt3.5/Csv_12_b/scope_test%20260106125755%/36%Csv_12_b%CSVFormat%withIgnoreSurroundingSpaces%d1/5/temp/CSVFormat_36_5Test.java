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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_36_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getIgnoreSurroundingSpaces());

        CSVFormat modified = original.withIgnoreSurroundingSpaces(true);
        assertNotNull(modified);
        assertTrue(modified.getIgnoreSurroundingSpaces());

        // Ensure other properties remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(original.getIgnoreSurroundingSpaces());

        CSVFormat modified = original.withIgnoreSurroundingSpaces(false);
        assertNotNull(modified);
        assertFalse(modified.getIgnoreSurroundingSpaces());

        // Ensure other properties remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesDoesNotModifyOriginal() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreSurroundingSpaces(true);

        // Original remains unchanged
        assertFalse(original.getIgnoreSurroundingSpaces());
        // Modified has new value
        assertTrue(modified.getIgnoreSurroundingSpaces());
    }
}