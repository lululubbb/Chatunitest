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

import java.lang.reflect.Method;

public class CSVFormat_66_5Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_ValidQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = '\'';
        CSVFormat newFormat = format.withQuote(quoteChar);
        assertNotNull(newFormat);
        assertEquals(quoteChar, newFormat.getQuoteCharacter());
        // Verify other properties remain the same
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), newFormat.getTrim());
        assertEquals(format.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(format.getAutoFlush(), newFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_NullQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = null;
        CSVFormat newFormat = format.withQuote(quoteChar);
        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());
        // Verify other properties remain the same
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), newFormat.getTrim());
        assertEquals(format.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(format.getAutoFlush(), newFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_LineBreakQuoteChar_throwsException() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with '\n' (LF) which should be a line break
        Character lf = '\n';
        boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, lf);
        assertTrue(isLineBreak);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            format.withQuote(lf);
        });
        assertEquals("The quoteChar cannot be a line break", thrown.getMessage());

        // Test with '\r' (CR) which should be a line break
        Character cr = '\r';
        isLineBreak = (boolean) isLineBreakMethod.invoke(null, cr);
        assertTrue(isLineBreak);

        thrown = assertThrows(IllegalArgumentException.class, () -> {
            format.withQuote(cr);
        });
        assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
    }
}