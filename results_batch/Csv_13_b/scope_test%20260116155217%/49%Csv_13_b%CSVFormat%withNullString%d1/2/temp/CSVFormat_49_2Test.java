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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_49_2Test {

    @Test
    @Timeout(8000)
    void testWithNullString_NewInstanceWithCorrectNullString() {
        // Arrange: create original CSVFormat with known parameters
        CSVFormat original = CSVFormat.DEFAULT;

        // Act: call withNullString with a sample null string
        String nullStr = "NULL";
        CSVFormat updated = original.withNullString(nullStr);

        // Assert: new instance is not the same as original
        assertNotSame(original, updated);

        // Assert: nullString is set correctly in new instance
        assertEquals(nullStr, updated.getNullString());

        // Assert: all other fields remain equal
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());

        // headerComments and header might be null, so handle accordingly
        if (original.getHeaderComments() == null) {
            assertNull(updated.getHeaderComments());
        } else {
            assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        }
        if (original.getHeader() == null) {
            assertNull(updated.getHeader());
        } else {
            assertArrayEquals(original.getHeader(), updated.getHeader());
        }

        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithNullString_NullValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withNullString(null);

        assertNotSame(original, updated);
        assertNull(updated.getNullString());

        // Other fields unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());

        // headerComments and header might be null, so handle accordingly
        if (original.getHeaderComments() == null) {
            assertNull(updated.getHeaderComments());
        } else {
            assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        }
        if (original.getHeader() == null) {
            assertNull(updated.getHeader());
        } else {
            assertArrayEquals(original.getHeader(), updated.getHeader());
        }

        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }
}