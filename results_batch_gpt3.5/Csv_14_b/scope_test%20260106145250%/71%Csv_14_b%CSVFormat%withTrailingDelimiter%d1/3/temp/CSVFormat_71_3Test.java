package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_71_3Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withTrailingDelimiter(true);

        assertNotNull(newFormat);
        assertTrue(newFormat.getTrailingDelimiter());
        // Original instance remains unchanged
        assertFalse(format.getTrailingDelimiter());
        // Other fields remain the same
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());

        assertArrayEqualsSafe(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEqualsSafe(format.getHeader(), newFormat.getHeader());

        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), newFormat.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        assertTrue(format.getTrailingDelimiter());

        CSVFormat newFormat = format.withTrailingDelimiter(false);
        assertNotNull(newFormat);
        assertFalse(newFormat.getTrailingDelimiter());
        // Original instance remains unchanged
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterDoesNotAffectOtherFields() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.ALL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeaderComments("comment1", "comment2")
                .withHeader("h1", "h2")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(false);

        CSVFormat newFormat = format.withTrailingDelimiter(true);

        assertTrue(newFormat.getTrailingDelimiter());
        assertEquals(';', newFormat.getDelimiter());
        assertEquals(Character.valueOf('\''), newFormat.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, newFormat.getQuoteMode());
        assertEquals(Character.valueOf('#'), newFormat.getCommentMarker());
        assertEquals(Character.valueOf('\\'), newFormat.getEscapeCharacter());
        assertTrue(newFormat.getIgnoreSurroundingSpaces());
        assertFalse(newFormat.getIgnoreEmptyLines());
        assertEquals("\n", newFormat.getRecordSeparator());
        assertEquals("NULL", newFormat.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, newFormat.getHeaderComments());
        assertArrayEquals(new String[]{"h1", "h2"}, newFormat.getHeader());
        assertTrue(newFormat.getSkipHeaderRecord());
        assertTrue(newFormat.getAllowMissingColumnNames());
        assertTrue(newFormat.getIgnoreHeaderCase());
        assertTrue(newFormat.getTrim());
    }

    private static void assertArrayEqualsSafe(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            fail("One of the arrays is null while the other is not");
        }
        if (!expected.getClass().isArray() || !actual.getClass().isArray()) {
            fail("One of the objects is not an array");
        }
        int expectedLength = Array.getLength(expected);
        int actualLength = Array.getLength(actual);
        assertEquals(expectedLength, actualLength, "Array lengths differ");
        for (int i = 0; i < expectedLength; i++) {
            Object expectedElement = Array.get(expected, i);
            Object actualElement = Array.get(actual, i);
            assertEquals(expectedElement, actualElement, "Array elements differ at index " + i);
        }
    }
}