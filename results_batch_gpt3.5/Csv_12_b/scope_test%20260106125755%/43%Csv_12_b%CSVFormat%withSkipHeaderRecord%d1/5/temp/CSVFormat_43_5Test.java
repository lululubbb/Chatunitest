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

class CSVFormat_43_5Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withSkipHeaderRecord(true);

        assertNotSame(original, modified, "withSkipHeaderRecord should return a new instance");
        assertTrue(modified.getSkipHeaderRecord(), "skipHeaderRecord should be true");
        // Other fields remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat modified = original.withSkipHeaderRecord(false);

        assertNotSame(original, modified, "withSkipHeaderRecord should return a new instance");
        assertFalse(modified.getSkipHeaderRecord(), "skipHeaderRecord should be false");
        // Other fields remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordOnCustomFormat() {
        CSVFormat custom = CSVFormat.newFormat(';')
                .withQuote('\'')
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("A", "B", "C")
                .withAllowMissingColumnNames(true);

        CSVFormat modified = custom.withSkipHeaderRecord(true);

        assertNotSame(custom, modified, "withSkipHeaderRecord should return a new instance");
        assertTrue(modified.getSkipHeaderRecord());
        assertEquals(';', modified.getDelimiter());
        assertEquals(Character.valueOf('\''), modified.getQuoteCharacter());
        assertEquals(Character.valueOf('#'), modified.getCommentMarker());
        assertEquals(Character.valueOf('\\'), modified.getEscapeCharacter());
        assertTrue(modified.getIgnoreSurroundingSpaces());
        assertFalse(modified.getIgnoreEmptyLines());
        assertEquals("\n", modified.getRecordSeparator());
        assertEquals("NULL", modified.getNullString());
        assertArrayEquals(new String[]{"A", "B", "C"}, modified.getHeader());
        assertTrue(modified.getAllowMissingColumnNames());
    }
}