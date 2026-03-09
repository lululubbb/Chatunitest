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
    void testWithEscapeWithValidEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';
        CSVFormat newFormat = format.withEscape(escapeChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        // Original should be unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeWithNullEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withEscape((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getEscapeCharacter());
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeThrowsExceptionForLineBreaks() {
        CSVFormat format = CSVFormat.DEFAULT;
        char[] lineBreaks = { '\n', '\r' };
        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> format.withEscape(lb));
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakPrivateMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));

        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}