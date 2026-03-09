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
import org.apache.commons.csv.Constants;
import org.junit.jupiter.api.Test;

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscape_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscape());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getCommentStart(), newFormat.getCommentStart());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_nullEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = null;

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getEscape());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_lineBreakCharThrows() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Test with '\r' (CR)
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(Constants.CR);
        });
        assertEquals("The escape character cannot be a line break", ex1.getMessage());

        // Test with '\n' (LF)
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(Constants.LF);
        });
        assertEquals("The escape character cannot be a line break", ex2.getMessage());

        // Test with '\r\n' is a String, but withEscape takes Character; test '\n' and '\r' cover line breaks
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));

        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}