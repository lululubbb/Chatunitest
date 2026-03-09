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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class CSVFormatWithQuoteTest {

    @Test
    @Timeout(8000)
    void testWithQuote_NullQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withQuote((Character) null);
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        // The other fields should remain the same
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_ValidQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = '"';
        CSVFormat result = format.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
        // The other fields should remain the same
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_ThrowsIfLineBreak() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat format = CSVFormat.DEFAULT;
        // Access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test line break characters that should cause exception
        char[] lineBreakChars = new char[] {'\r', '\n'};

        for (char lbChar : lineBreakChars) {
            assertTrue((Boolean) isLineBreakMethod.invoke(null, lbChar));
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withQuote(lbChar);
            });
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithQuote_ValidNonLineBreakChars() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat format = CSVFormat.DEFAULT;
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        char[] nonLineBreakChars = new char[] {',', ';', 'a', '1', ' '};

        for (char c : nonLineBreakChars) {
            assertFalse((Boolean) isLineBreakMethod.invoke(null, c));
            CSVFormat result = format.withQuote(c);
            assertEquals(c, result.getQuoteCharacter());
        }
    }
}