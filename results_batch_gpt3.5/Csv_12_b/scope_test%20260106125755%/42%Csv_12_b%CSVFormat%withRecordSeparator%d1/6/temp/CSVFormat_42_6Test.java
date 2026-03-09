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

class CSVFormatWithRecordSeparatorTest {

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorStringCreatesNewInstanceWithGivenSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "\n";
        CSVFormat updated = original.withRecordSeparator(newSeparator);

        assertNotNull(updated);
        assertNotSame(original, updated, "withRecordSeparator should return a new instance");
        assertEquals(newSeparator, updated.getRecordSeparator(), "Record separator should be updated");
        // All other fields remain equal
        assertEquals(original.getDelimiter(), updated.getDelimiter(), "Delimiter should remain unchanged");
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter(), "Quote character should remain unchanged");
        assertEquals(original.getQuoteMode(), updated.getQuoteMode(), "Quote mode should remain unchanged");
        assertEquals(original.getCommentMarker(), updated.getCommentMarker(), "Comment marker should remain unchanged");
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter(), "Escape character should remain unchanged");
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces(), "Ignore surrounding spaces should remain unchanged");
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines(), "Ignore empty lines should remain unchanged");
        assertEquals(original.getNullString(), updated.getNullString(), "Null string should remain unchanged");
        assertArrayEquals(original.getHeader(), updated.getHeader(), "Header should remain unchanged");
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord(), "Skip header record should remain unchanged");
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames(), "Allow missing column names should remain unchanged");
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorStringNullValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withRecordSeparator((String) null);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getRecordSeparator(), "Record separator should be null when set to null");
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorStringEmptyValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withRecordSeparator("");

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals("", updated.getRecordSeparator(), "Record separator should be empty string when set to empty");
    }
}