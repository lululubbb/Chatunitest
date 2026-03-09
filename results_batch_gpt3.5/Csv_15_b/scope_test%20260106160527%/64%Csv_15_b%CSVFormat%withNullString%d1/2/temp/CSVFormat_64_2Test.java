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

class CSVFormatWithNullStringTest {

    @Test
    @Timeout(8000)
    void testWithNullString_NewInstanceHasCorrectNullString() {
        CSVFormat original = CSVFormat.DEFAULT;
        String nullStr = "NULL";

        CSVFormat updated = original.withNullString(nullStr);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(nullStr, updated.getNullString());

        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertArrayEquals(
                original.getHeaderComments() == null ? new String[0] : original.getHeaderComments(),
                updated.getHeaderComments() == null ? new String[0] : updated.getHeaderComments());
        assertArrayEquals(
                original.getHeader() == null ? new String[0] : original.getHeader(),
                updated.getHeader() == null ? new String[0] : updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), updated.getTrim());
        assertEquals(original.getTrailingDelimiter(), updated.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), updated.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithNullString_NullValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withNullString(null);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getNullString());
    }

    @Test
    @Timeout(8000)
    void testWithNullString_EmptyString() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withNullString("");

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals("", updated.getNullString());
    }

    @Test
    @Timeout(8000)
    void testWithNullString_UsingCustomFormat() {
        CSVFormat custom = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withHeader("A", "B")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(true)
                .withAutoFlush(true);

        String nullStr = "NULLVAL";
        CSVFormat updated = custom.withNullString(nullStr);

        assertNotNull(updated);
        assertNotSame(custom, updated);
        assertEquals(nullStr, updated.getNullString());

        // Verify other properties remain unchanged
        assertEquals(custom.getDelimiter(), updated.getDelimiter());
        assertEquals(custom.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(custom.getQuoteMode(), updated.getQuoteMode());
        assertEquals(custom.getCommentMarker(), updated.getCommentMarker());
        assertEquals(custom.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(custom.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(custom.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(custom.getRecordSeparator(), updated.getRecordSeparator());
        assertArrayEquals(
                custom.getHeaderComments() == null ? new String[0] : custom.getHeaderComments(),
                updated.getHeaderComments() == null ? new String[0] : updated.getHeaderComments());
        assertArrayEquals(
                custom.getHeader() == null ? new String[0] : custom.getHeader(),
                updated.getHeader() == null ? new String[0] : updated.getHeader());
        assertEquals(custom.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(custom.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(custom.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
        assertEquals(custom.getTrim(), updated.getTrim());
        assertEquals(custom.getTrailingDelimiter(), updated.getTrailingDelimiter());
        assertEquals(custom.getAutoFlush(), updated.getAutoFlush());
    }
}