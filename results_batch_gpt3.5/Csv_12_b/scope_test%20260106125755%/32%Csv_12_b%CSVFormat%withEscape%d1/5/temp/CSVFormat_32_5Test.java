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
import org.junit.jupiter.api.Test;

public class CSVFormat_32_5Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_NullEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withEscape((Character) null);
        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        // Original unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_ValidEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';
        CSVFormat result = format.withEscape(escapeChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // Original unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_LineBreakEscapeCharThrows() {
        CSVFormat format = CSVFormat.DEFAULT;

        char[] lineBreaks = new char[] { '\n', '\r' };

        for (char c : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withEscape(c);
            });
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateStaticChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        // Test line breaks
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));

        // Test non-line breaks
        assertFalse((Boolean) method.invoke(null, 'a'));
        assertFalse((Boolean) method.invoke(null, ','));
        assertFalse((Boolean) method.invoke(null, '\\'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateStaticCharacter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        // Test null returns false
        assertFalse((Boolean) method.invoke(null, (Object) null));

        // Test line breaks
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));

        // Test non-line breaks
        assertFalse((Boolean) method.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf('\\')));
    }
}