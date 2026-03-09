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
import org.junit.jupiter.api.Test;

class CSVFormat_64_4Test {

    @Test
    @Timeout(8000)
    void testWithQuote_withValidQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat result = format.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), result.getTrim());
        assertEquals(format.getTrailingDelimiter(), result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withNullQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat result = format.withQuote((Character) null);

        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), result.getTrim());
        assertEquals(format.getTrailingDelimiter(), result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withLineBreakChar_throwsIllegalArgumentException() {
        CSVFormat format = CSVFormat.DEFAULT;
        char lineBreakChar = '\n'; // LF is a line break

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            format.withQuote(Character.valueOf(lineBreakChar));
        });

        assertEquals("The quoteChar cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withLineBreakCharCR_throwsIllegalArgumentException() {
        CSVFormat format = CSVFormat.DEFAULT;
        char lineBreakChar = '\r'; // CR is a line break

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            format.withQuote(Character.valueOf(lineBreakChar));
        });

        assertEquals("The quoteChar cannot be a line break", exception.getMessage());
    }
}