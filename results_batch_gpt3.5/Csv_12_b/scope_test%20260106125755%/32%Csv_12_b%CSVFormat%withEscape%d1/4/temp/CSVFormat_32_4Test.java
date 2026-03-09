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
import java.lang.reflect.Method;

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscapeValidCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';
        CSVFormat updated = original.withEscape(escapeChar);
        assertNotNull(updated);
        assertEquals(Character.valueOf(escapeChar), updated.getEscapeCharacter());
        // Original should be unchanged
        assertNull(original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeNullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withEscape((Character) null);
        assertNotNull(updated);
        assertNull(updated.getEscapeCharacter());
        // Original should be unchanged
        assertNull(original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeLineBreakCharacterThrows() {
        CSVFormat original = CSVFormat.DEFAULT;
        char[] lineBreaks = {'\n', '\r'};
        for (char lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                // use withEscape(char) to match signature that throws
                original.withEscape(lb);
            });
            assertEquals("The escape character cannot be a line break", ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakPrivateMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);
        // line breaks
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        // non line break
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacterObjectPrivateMethod() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);
        // line breaks
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        // null returns false
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
        // non line break
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf(',')));
    }
}