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

class CSVFormat_35_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withIgnoreEmptyLines(true);
        assertNotNull(updated);
        assertTrue(updated.getIgnoreEmptyLines());
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat updated = original.withIgnoreEmptyLines(false);
        assertNotNull(updated);
        assertFalse(updated.getIgnoreEmptyLines());
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesCalledOnInstanceWithFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        CSVFormat updated = original.withIgnoreEmptyLines(true);
        assertTrue(updated.getIgnoreEmptyLines());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesCalledMultipleTimes() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated1 = original.withIgnoreEmptyLines(false);
        CSVFormat updated2 = updated1.withIgnoreEmptyLines(true);
        CSVFormat updated3 = updated2.withIgnoreEmptyLines(false);

        assertFalse(updated1.getIgnoreEmptyLines());
        assertTrue(updated2.getIgnoreEmptyLines());
        assertFalse(updated3.getIgnoreEmptyLines());

        assertNotSame(original, updated1);
        assertNotSame(updated1, updated2);
        assertNotSame(updated2, updated3);
    }
}