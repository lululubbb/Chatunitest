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

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_71_2Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withTrailingDelimiter(true);

        assertNotNull(result);
        assertTrue(result.getTrailingDelimiter());
        // Ensure all other properties remain the same
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), result.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat result = original.withTrailingDelimiter(false);

        assertNotNull(result);
        assertFalse(result.getTrailingDelimiter());
        // Ensure all other properties remain the same
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), result.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterOnCustomFormat() {
        CSVFormat custom = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuote('\'')
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("A", "B")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(false);

        CSVFormat updated = custom.withTrailingDelimiter(true);

        assertNotNull(updated);
        assertTrue(updated.getTrailingDelimiter());
        assertEquals(';', updated.getDelimiter());
        assertEquals(Character.valueOf('\''), updated.getQuoteCharacter());
        assertEquals(false, updated.getIgnoreEmptyLines());
        assertEquals("\n", updated.getRecordSeparator());
        assertEquals("NULL", updated.getNullString());
        assertArrayEquals(new String[]{"A", "B"}, updated.getHeader());
        assertEquals(true, updated.getSkipHeaderRecord());
        assertEquals(true, updated.getAllowMissingColumnNames());
        assertEquals(true, updated.getIgnoreHeaderCase());
        assertEquals(true, updated.getTrim());
    }
}