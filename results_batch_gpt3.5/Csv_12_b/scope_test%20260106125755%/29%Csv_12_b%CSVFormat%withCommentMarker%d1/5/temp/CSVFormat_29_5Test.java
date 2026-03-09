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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormatWithCommentMarkerTest {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_ValidCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        // Check other fields unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
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
    void testWithCommentMarker_NullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = null;

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // Other fields unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
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
    void testWithCommentMarker_LineBreakCharacters() throws Exception {
        // Using reflection to invoke private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Line break characters to test
        Character[] lineBreakChars = { '\r', '\n' };

        for (Character c : lineBreakChars) {
            // Verify private method returns true for line break characters
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(result);

            CSVFormat format = CSVFormat.DEFAULT;
            char commentMarker = c;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> format.withCommentMarker(commentMarker));
            assertEquals("The comment start marker character cannot be a line break", exception.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_DelegatesToPrivateIsLineBreakChar() throws Exception {
        // Using reflection to invoke private static method isLineBreak(char)
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        // Test characters that are line breaks
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\r'));
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\n'));

        // Test characters that are not line breaks
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, '#'));
    }
}