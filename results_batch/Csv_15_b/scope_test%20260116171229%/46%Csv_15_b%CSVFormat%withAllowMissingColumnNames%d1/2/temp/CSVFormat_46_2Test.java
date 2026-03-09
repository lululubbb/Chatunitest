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
import org.junit.jupiter.api.Test;

class CSVFormatWithAllowMissingColumnNamesTest {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withAllowMissingColumnNames(true);

        assertNotSame(original, modified);
        assertTrue(modified.getAllowMissingColumnNames());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(
                original.getHeaderComments() == null ? new String[0] : original.getHeaderComments(),
                modified.getHeaderComments() == null ? new String[0] : modified.getHeaderComments());
        assertArrayEquals(
                original.getHeader() == null ? new String[0] : original.getHeader(),
                modified.getHeader() == null ? new String[0] : modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), modified.getTrim());
        assertEquals(original.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), modified.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat modified = original.withAllowMissingColumnNames(false);

        assertNotSame(original, modified);
        assertFalse(modified.getAllowMissingColumnNames());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(
                original.getHeaderComments() == null ? new String[0] : original.getHeaderComments(),
                modified.getHeaderComments() == null ? new String[0] : modified.getHeaderComments());
        assertArrayEquals(
                original.getHeader() == null ? new String[0] : original.getHeader(),
                modified.getHeader() == null ? new String[0] : modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), modified.getTrim());
        assertEquals(original.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), modified.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesDoesNotAffectOtherProperties() {
        CSVFormat original = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuote('\'')
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("null")
                .withHeader("A", "B")
                .withHeaderComments("comment1", "comment2")
                .withSkipHeaderRecord(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(true)
                .withAutoFlush(true)
                .withAllowMissingColumnNames(false);

        CSVFormat modified = original.withAllowMissingColumnNames(true);

        assertNotSame(original, modified);
        assertTrue(modified.getAllowMissingColumnNames());
        assertEquals(';', modified.getDelimiter());
        assertEquals(Character.valueOf('\''), modified.getQuoteCharacter());
        assertEquals(Character.valueOf('#'), modified.getCommentMarker());
        assertEquals(Character.valueOf('\\'), modified.getEscapeCharacter());
        assertTrue(modified.getIgnoreSurroundingSpaces());
        assertFalse(modified.getIgnoreEmptyLines());
        assertEquals("\n", modified.getRecordSeparator());
        assertEquals("null", modified.getNullString());
        assertArrayEquals(new String[] { "comment1", "comment2" }, modified.getHeaderComments());
        assertArrayEquals(new String[] { "A", "B" }, modified.getHeader());
        assertTrue(modified.getSkipHeaderRecord());
        assertTrue(modified.getIgnoreHeaderCase());
        assertTrue(modified.getTrim());
        assertTrue(modified.getTrailingDelimiter());
        assertTrue(modified.getAutoFlush());
    }
}