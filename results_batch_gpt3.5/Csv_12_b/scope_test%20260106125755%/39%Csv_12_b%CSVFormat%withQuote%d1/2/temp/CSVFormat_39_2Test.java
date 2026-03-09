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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_39_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_withValidQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat newFormat = format.withQuote(quoteChar);

        assertNotNull(newFormat);
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(Character.valueOf(quoteChar), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_withNullQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = null;

        CSVFormat newFormat = format.withQuote(quoteChar);

        assertNotNull(newFormat);
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertNull(newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_withLineBreakQuoteChar_throwsException() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Access private static method isLineBreak(char) via reflection
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Find a line break character to test with
        char[] lineBreakChars = new char[] {'\n', '\r'};
        for (char lbChar : lineBreakChars) {
            // Confirm isLineBreak returns true for this char
            Boolean isLineBreak = (Boolean) isLineBreakMethod.invoke(null, lbChar);
            assertTrue(isLineBreak);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withQuote(lbChar);
            });
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }

}