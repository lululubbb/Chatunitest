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

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscapeValidChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscape());
        // Ensure other properties are unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getCommentStart(), newFormat.getCommentStart());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeNullChar() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat newFormat = format.withEscape((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getEscape());
        // Ensure other properties are unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getCommentStart(), newFormat.getCommentStart());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeThrowsOnLineBreakChar_CR() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\r';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withEscape(escapeChar));
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeThrowsOnLineBreakChar_LF() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\n';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withEscape(escapeChar));
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        // Test line break chars
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));

        // Test non-line break chars
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
        assertFalse((Boolean) isLineBreakChar.invoke(null, '\\'));
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacterMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        // Test null input returns false
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));

        // Test line break chars
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));

        // Test non-line break chars
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\\')));
    }
}