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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_64_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuote() {
        // Given
        Character quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withDelimiter('|')
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\r\n")
                .withNullString("null")
                .withHeaderComments("header")
                .withHeader("header")
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(false)
                .withIgnoreHeaderCase(false)
                .withTrim(false)
                .withTrailingDelimiter(false);

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        assertEquals('|', result.getDelimiter());
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertNull(result.getQuoteMode());
        assertEquals('#', result.getCommentMarker());
        assertEquals('\\', result.getEscapeCharacter());
        assertTrue(result.getIgnoreSurroundingSpaces());
        assertFalse(result.getIgnoreEmptyLines());
        assertEquals("\r\n", result.getRecordSeparator());
        assertEquals("null", result.getNullString());
        assertArrayEquals(new Object[]{"header"}, result.getHeaderComments());
        assertArrayEquals(new String[]{"header"}, result.getHeader());
        assertFalse(result.getSkipHeaderRecord());
        assertFalse(result.getAllowMissingColumnNames());
        assertFalse(result.getIgnoreHeaderCase());
        assertFalse(result.getTrim());
        assertFalse(result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_lineBreakQuoteChar_IllegalArgumentException() {
        // Given
        Character quoteChar = '\n';
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withDelimiter('|')
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\r\n")
                .withNullString("null")
                .withHeaderComments("header")
                .withHeader("header")
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(false)
                .withIgnoreHeaderCase(false)
                .withTrim(false)
                .withTrailingDelimiter(false);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withQuote(quoteChar));

        // Then
        assertEquals("The quoteChar cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testDefaultCSVFormat() {
        // Given
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Then
        assertEquals(',', defaultFormat.getDelimiter());
        assertEquals('"', defaultFormat.getQuoteCharacter());
        assertNull(defaultFormat.getQuoteMode());
        assertNull(defaultFormat.getCommentMarker());
        assertNull(defaultFormat.getEscapeCharacter());
        assertFalse(defaultFormat.getIgnoreSurroundingSpaces());
        assertTrue(defaultFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", defaultFormat.getRecordSeparator());
        assertNull(defaultFormat.getNullString());
        assertNull(defaultFormat.getHeaderComments());
        assertNull(defaultFormat.getHeader());
        assertFalse(defaultFormat.getSkipHeaderRecord());
        assertFalse(defaultFormat.getAllowMissingColumnNames());
        assertFalse(defaultFormat.getIgnoreHeaderCase());
        assertFalse(defaultFormat.getTrim());
        assertFalse(defaultFormat.getTrailingDelimiter());
    }
}