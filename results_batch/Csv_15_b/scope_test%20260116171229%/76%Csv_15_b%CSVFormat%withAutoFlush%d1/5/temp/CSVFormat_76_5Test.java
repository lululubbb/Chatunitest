package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_76_5Test {

    @Test
    @Timeout(8000)
    void testWithAutoFlushTrueCreatesNewInstanceWithAutoFlushTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withAutoFlush(true);

        assertNotSame(original, updated);
        assertTrue(updated.getAutoFlush());
        // All other properties should remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), updated.getTrim());
        assertEquals(original.getTrailingDelimiter(), updated.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithAutoFlushFalseCreatesNewInstanceWithAutoFlushFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withAutoFlush(true);
        CSVFormat updated = original.withAutoFlush(false);

        assertNotSame(original, updated);
        assertFalse(updated.getAutoFlush());
        // All other properties should remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), updated.getTrim());
        assertEquals(original.getTrailingDelimiter(), updated.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithAutoFlushOnCustomInstance() {
        CSVFormat custom = CSVFormat.DEFAULT.withDelimiter('|')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.ALL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("a", "b")
                .withHeaderComments("header1")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(true)
                .withAutoFlush(false);

        CSVFormat updated = custom.withAutoFlush(true);

        assertNotSame(custom, updated);
        assertTrue(updated.getAutoFlush());

        assertEquals('|', updated.getDelimiter());
        assertEquals(Character.valueOf('\''), updated.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, updated.getQuoteMode());
        assertEquals(Character.valueOf('#'), updated.getCommentMarker());
        assertEquals(Character.valueOf('\\'), updated.getEscapeCharacter());
        assertTrue(updated.getIgnoreSurroundingSpaces());
        assertFalse(updated.getIgnoreEmptyLines());
        assertEquals("\n", updated.getRecordSeparator());
        assertEquals("NULL", updated.getNullString());
        assertArrayEquals(new String[] {"header1"}, updated.getHeaderComments());
        assertArrayEquals(new String[] {"a", "b"}, updated.getHeader());
        assertTrue(updated.getSkipHeaderRecord());
        assertTrue(updated.getAllowMissingColumnNames());
        assertTrue(updated.getIgnoreHeaderCase());
        assertTrue(updated.getTrim());
        assertTrue(updated.getTrailingDelimiter());
    }
}