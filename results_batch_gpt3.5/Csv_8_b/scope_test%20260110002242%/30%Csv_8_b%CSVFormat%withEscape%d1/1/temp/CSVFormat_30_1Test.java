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
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscape_validEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscape());
        // Other fields should remain unchanged
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
    void testWithEscape_nullEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = null;

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertNull(newFormat.getEscape());
        // Other fields remain unchanged
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
    void testWithEscape_lineBreakEscapeChar_throwsIllegalArgumentException() {
        CSVFormat format = CSVFormat.DEFAULT;

        Character[] lineBreaks = { '\n', '\r' };

        for (Character lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                if (lb == null) {
                    format.withEscape((Character) null);
                } else {
                    format.withEscape(lb.charValue());
                }
            });
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_char() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Line break chars should return true
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\r'));

        // Non line break chars should return false
        assertFalse((Boolean) isLineBreakMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, '\\'));
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_Character() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Null character returns false
        assertFalse((Boolean) isLineBreakMethod.invoke(null, (Character) null));

        // Line break chars should return true
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\r')));

        // Non line break chars should return false
        assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\\')));
    }
}