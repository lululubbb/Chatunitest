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
import org.junit.jupiter.api.DisplayName;

class CSVFormat_40_2Test {

    @Test
    @Timeout(8000)
    @DisplayName("withSkipHeaderRecord should create new CSVFormat with given skipHeaderRecord value")
    void testWithSkipHeaderRecordTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withSkipHeaderRecord(true);

        assertNotSame(original, modified, "withSkipHeaderRecord should return a new CSVFormat instance");
        assertTrue(modified.getSkipHeaderRecord(), "skipHeaderRecord should be true");
        // All other properties should remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getNullString(), modified.getNullString());
    }

    @Test
    @Timeout(8000)
    @DisplayName("withSkipHeaderRecord should create new CSVFormat with skipHeaderRecord false")
    void testWithSkipHeaderRecordFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat modified = original.withSkipHeaderRecord(false);

        assertNotSame(original, modified, "withSkipHeaderRecord should return a new CSVFormat instance");
        assertFalse(modified.getSkipHeaderRecord(), "skipHeaderRecord should be false");
        // All other properties should remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getNullString(), modified.getNullString());
    }
}