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

class CSVFormatWithCommentStartTest {

    @Test
    @Timeout(8000)
    void testWithCommentStartValidCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentChar = '#';
        CSVFormat newFormat = format.withCommentStart(commentChar.charValue());
        assertNotNull(newFormat);
        assertEquals(commentChar, newFormat.getCommentStart());
        // Ensure other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getEscape(), newFormat.getEscape());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withCommentStart((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getCommentStart());
        // Ensure other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getEscape(), newFormat.getEscape());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakCharactersThrows() {
        CSVFormat format = CSVFormat.DEFAULT;
        char[] lineBreaks = {'\r', '\n'};
        for (char lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentStart(lb);
            });
            assertEquals("The comment start character cannot be a line break", ex.getMessage());
        }
        // Test with Character objects wrapping line breaks
        for (char lb : lineBreaks) {
            Character lbChar = lb;
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentStart(lbChar);
            });
            assertEquals("The comment start character cannot be a line break", ex.getMessage());
        }
    }
}