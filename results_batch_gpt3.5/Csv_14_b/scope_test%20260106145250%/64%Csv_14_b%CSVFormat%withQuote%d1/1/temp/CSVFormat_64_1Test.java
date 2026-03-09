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

import java.lang.reflect.Method;

public class CSVFormat_64_1Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_ValidChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char quoteChar = '"';

        CSVFormat result = baseFormat.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), result.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), result.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_NullQuoteChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character quoteChar = null;

        CSVFormat result = baseFormat.withQuote(quoteChar);

        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), result.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), result.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_LineBreakCharThrows() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Use reflection to access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Find a char that is a line break
        char[] candidates = {'\n', '\r'};
        Character lineBreakChar = null;
        for (char c : candidates) {
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, c);
            if (result != null && result) {
                lineBreakChar = c;
                break;
            }
        }
        assertNotNull(lineBreakChar, "No line break character found for testing");

        char finalLineBreakChar = lineBreakChar;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            baseFormat.withQuote(finalLineBreakChar);
        });
        assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
    }
}