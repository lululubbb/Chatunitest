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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_35_2Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(true);

        assertNotNull(modified);
        assertTrue(modified.getIgnoreEmptyLines());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getNullString(), modified.getNullString());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesFalse() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(false);

        assertNotNull(modified);
        assertFalse(modified.getIgnoreEmptyLines());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getNullString(), modified.getNullString());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesDoesNotModifyOriginal() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(!original.getIgnoreEmptyLines());

        assertNotEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getNullString(), modified.getNullString());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());

        // original remains unchanged
        assertEquals(CSVFormat.DEFAULT.getIgnoreEmptyLines(), original.getIgnoreEmptyLines());
    }
}