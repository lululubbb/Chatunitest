package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithQuoteTest {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_ValidQuoteChar() {
        char quoteChar = '\'';
        CSVFormat newFormat = defaultFormat.withQuote(quoteChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(quoteChar), newFormat.getQuoteCharacter());
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(defaultFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(defaultFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), newFormat.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_NullQuoteChar() {
        CSVFormat newFormat = defaultFormat.withQuote((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_ThrowsOnLineBreaks() throws Exception {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters that should cause exception
        char[] lineBreakChars = { '\n', '\r' };

        for (char c : lineBreakChars) {
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(isLineBreak);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                defaultFormat.withQuote(c);
            });
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }

        // Also test with a character that is not a line break
        char normalChar = 'q';
        boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, normalChar);
        assertFalse(isLineBreak);
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_ThrowsOnLineBreakUsingPrivateCharMethod() throws Exception {
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        char[] lineBreakChars = { '\n', '\r' };
        for (char c : lineBreakChars) {
            boolean result = (boolean) isLineBreakCharMethod.invoke(null, c);
            assertTrue(result);
        }
        char normalChar = 'a';
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, normalChar);
        assertFalse(result);
    }
}