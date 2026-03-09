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

public class CSVFormat_66_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_NullQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withQuote(null);
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
        assertEquals(format.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_NormalQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = '\'';
        CSVFormat result = format.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
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
        assertEquals(format.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_ThrowsOnLineBreak() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character lineBreakCharLF = '\n'; // LF line break
        IllegalArgumentException thrownLF = assertThrows(IllegalArgumentException.class, () -> {
            format.withQuote(lineBreakCharLF);
        });
        assertEquals("The quoteChar cannot be a line break", thrownLF.getMessage());

        Character lineBreakCharCR = '\r'; // CR line break
        IllegalArgumentException thrownCR = assertThrows(IllegalArgumentException.class, () -> {
            format.withQuote(lineBreakCharCR);
        });
        assertEquals("The quoteChar cannot be a line break", thrownCR.getMessage());
    }
}