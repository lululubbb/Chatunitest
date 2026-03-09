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

class CSVFormat_34_3Test {

    @Test
    @Timeout(8000)
    void testWithNullString() throws Exception {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;
        String nullString = "NULL";

        // Act
        CSVFormat result = original.withNullString(nullString);

        // Assert
        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(nullString, result.getNullString());

        // Verify other fields remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getEscape(), result.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithNullStringNull() throws Exception {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;
        String nullString = null;

        // Act
        CSVFormat result = original.withNullString(nullString);

        // Assert
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getNullString());

        // Verify other fields remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getEscape(), result.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }
}