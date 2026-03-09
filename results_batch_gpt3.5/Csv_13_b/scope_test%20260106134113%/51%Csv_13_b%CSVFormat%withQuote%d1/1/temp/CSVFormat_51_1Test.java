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

public class CSVFormat_51_1Test {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_withValidQuoteChar() {
        char quoteChar = '"';
        CSVFormat newFormat = defaultFormat.withQuote(quoteChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(quoteChar), newFormat.getQuoteCharacter());
        // Other fields remain unchanged
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
    public void testWithQuote_withNullQuoteChar() {
        Character quoteChar = null;
        CSVFormat newFormat = defaultFormat.withQuote(quoteChar);
        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_withLineBreakQuoteChar_throwsException() throws Exception {
        // Use reflection to access private static isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters that cause exception
        char[] lineBreaks = { '\n', '\r' };

        for (char lineBreak : lineBreaks) {
            assertTrue((Boolean) isLineBreakMethod.invoke(null, lineBreak));

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                defaultFormat.withQuote(lineBreak);
            });
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }
}