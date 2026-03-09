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

class CSVFormatWithIgnoreEmptyLinesTest {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withIgnoreEmptyLines(true);

        assertNotSame(original, updated);
        assertTrue(updated.getIgnoreEmptyLines());
        // Other properties should remain equal
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());

        // Use reflection safe array equals to avoid NullPointerException
        assertTrue(arrayEquals(original.getHeader(), updated.getHeader()));

        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withIgnoreEmptyLines(false);

        assertNotSame(original, updated);
        assertFalse(updated.getIgnoreEmptyLines());
        // Other properties should remain equal
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());

        // Use reflection safe array equals to avoid NullPointerException
        assertTrue(arrayEquals(original.getHeader(), updated.getHeader()));

        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
    }

    private boolean arrayEquals(String[] a, String[] b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (!java.util.Objects.equals(a[i], b[i])) {
                return false;
            }
        }
        return true;
    }
}