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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_71_5Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterTrue() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withTrailingDelimiter(true);

        assertNotSame(base, result);
        assertTrue(result.getTrailingDelimiter());
        // Other properties unchanged
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterFalse() {
        CSVFormat base = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat result = base.withTrailingDelimiter(false);

        assertNotSame(base, result);
        assertFalse(result.getTrailingDelimiter());
        // Other properties unchanged
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterOnCustomFormat() {
        CSVFormat custom = CSVFormat.newFormat(';')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.ALL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("A", "B")
                .withHeaderComments("headerComment")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(false);

        CSVFormat result = custom.withTrailingDelimiter(true);

        assertNotSame(custom, result);
        assertTrue(result.getTrailingDelimiter());
        assertEquals(';', result.getDelimiter());
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, result.getQuoteMode());
        assertEquals(Character.valueOf('#'), result.getCommentMarker());
        assertEquals(Character.valueOf('\\'), result.getEscapeCharacter());
        assertTrue(result.getIgnoreSurroundingSpaces());
        assertFalse(result.getIgnoreEmptyLines());
        assertEquals("\n", result.getRecordSeparator());
        assertEquals("NULL", result.getNullString());
        assertArrayEquals(new String[]{"headerComment"}, result.getHeaderComments());
        assertArrayEquals(new String[]{"A", "B"}, result.getHeader());
        assertTrue(result.getSkipHeaderRecord());
        assertTrue(result.getAllowMissingColumnNames());
        assertTrue(result.getIgnoreHeaderCase());
        assertTrue(result.getTrim());
    }
}