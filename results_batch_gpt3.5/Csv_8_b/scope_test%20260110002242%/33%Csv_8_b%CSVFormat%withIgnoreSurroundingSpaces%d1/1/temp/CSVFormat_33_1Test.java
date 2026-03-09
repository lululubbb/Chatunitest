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

public class CSVFormat_33_1Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreSurroundingSpaces(true);

        assertNotNull(modified);
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertTrue(modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat modified = original.withIgnoreSurroundingSpaces(false);

        assertNotNull(modified);
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertFalse(modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesReturnsNewInstance() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreSurroundingSpaces(!original.getIgnoreSurroundingSpaces());

        assertNotSame(original, modified);
    }
}