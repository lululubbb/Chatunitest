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

class CSVFormat_73_1Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withTrailingDelimiter(true);

        assertNotSame(original, updated, "withTrailingDelimiter should return a new CSVFormat instance");
        assertTrue(updated.getTrailingDelimiter(), "Trailing delimiter should be true in the new instance");
        // Original instance remains unchanged
        assertFalse(original.getTrailingDelimiter(), "Trailing delimiter should remain false in the original instance");
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat updated = original.withTrailingDelimiter(false);

        assertNotSame(original, updated, "withTrailingDelimiter should return a new CSVFormat instance");
        assertFalse(updated.getTrailingDelimiter(), "Trailing delimiter should be false in the new instance");
        // Original instance remains unchanged
        assertTrue(original.getTrailingDelimiter(), "Trailing delimiter should remain true in the original instance");
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterDoesNotAffectOtherFields() {
        CSVFormat original = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuote('?')
                .withQuoteMode(QuoteMode.ALL_NON_NULL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("a", "b")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withAutoFlush(true);

        CSVFormat updated = original.withTrailingDelimiter(true);

        assertNotSame(original, updated);
        assertTrue(updated.getTrailingDelimiter());
        assertEquals(';', updated.getDelimiter());
        assertEquals(Character.valueOf('?'), updated.getQuoteCharacter());
        assertEquals(QuoteMode.ALL_NON_NULL, updated.getQuoteMode());
        assertEquals(Character.valueOf('#'), updated.getCommentMarker());
        assertEquals(Character.valueOf('\\'), updated.getEscapeCharacter());
        assertTrue(updated.getIgnoreSurroundingSpaces());
        assertFalse(updated.getIgnoreEmptyLines());
        assertEquals("\n", updated.getRecordSeparator());
        assertEquals("NULL", updated.getNullString());
        assertArrayEquals(new String[] {"a", "b"}, updated.getHeader());
        assertTrue(updated.getSkipHeaderRecord());
        assertTrue(updated.getAllowMissingColumnNames());
        assertTrue(updated.getIgnoreHeaderCase());
        assertTrue(updated.getTrim());
        assertTrue(updated.getAutoFlush());

        // Original remains unchanged
        assertFalse(original.getTrailingDelimiter());
    }
}