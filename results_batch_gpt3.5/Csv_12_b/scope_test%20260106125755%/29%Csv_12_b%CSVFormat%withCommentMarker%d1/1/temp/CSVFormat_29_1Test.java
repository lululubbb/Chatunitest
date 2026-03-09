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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_29_1Test {

    private CSVFormat baseFormat;

    @BeforeEach
    public void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validCharacter() {
        char commentMarker = '#';
        CSVFormat newFormat = baseFormat.withCommentMarker(commentMarker);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        // Original format should remain unchanged
        assertNull(baseFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_nullCharacter() {
        CSVFormat newFormat = baseFormat.withCommentMarker((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_char() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test line break chars recognized by isLineBreak(char)
        char[] lineBreaks = {'\r', '\n'};
        for (char c : lineBreaks) {
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(result, "Expected line break for character: " + (int)c);
        }

        // Test a non-line break char
        Boolean result = (Boolean) isLineBreakMethod.invoke(null, 'a');
        assertFalse(result, "Expected non-line break for character: 'a'");
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_Character() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test line break chars recognized by isLineBreak(Character)
        Character[] lineBreaks = {'\r', '\n'};
        for (Character c : lineBreaks) {
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(result, "Expected line break for character: " + (int)c.charValue());
        }

        // Test null returns false
        Boolean resultNull = (Boolean) isLineBreakMethod.invoke(null, (Character) null);
        assertFalse(resultNull, "Expected false for null character");

        // Test a non-line break char
        Boolean result = (Boolean) isLineBreakMethod.invoke(null, 'a');
        assertFalse(result, "Expected non-line break for character: 'a'");
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakCharacterThrows() {
        char[] lineBreaks = {'\r', '\n'};
        for (char c : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withCommentMarker(c);
            }, "Expected IllegalArgumentException for commentMarker: " + (int)c);
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }
}