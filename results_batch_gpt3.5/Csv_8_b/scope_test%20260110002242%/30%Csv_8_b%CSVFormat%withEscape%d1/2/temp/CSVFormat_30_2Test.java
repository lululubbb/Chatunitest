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

public class CSVFormat_30_2Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_withValidEscapeChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscape());
        // Other fields remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_withNullEscapeChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character escapeChar = null;

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertNull(result.getEscape());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_withLineBreakEscapeChar_throwsException() {
        CSVFormat original = CSVFormat.DEFAULT;

        Character[] lineBreaks = { '\n', '\r' };

        for (Character lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                original.withEscape(lb);
            });
            assertEquals("The escape character cannot be a line break", ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_char() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        // Line break chars
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));

        // Non line break chars
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_Character() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        // Null returns false
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));

        // Line break chars
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\r'));

        // Non line break chars
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, ','));
    }
}