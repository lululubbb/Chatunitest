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

public class CSVFormat_30_6Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_validEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscape());
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
    public void testWithEscape_nullEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = null;

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertNull(newFormat.getEscape());
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
    public void testWithEscape_lineBreakEscapeChar_throwsIllegalArgumentException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access private static isLineBreak(Character) method
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with CR character
        Character cr = '\r';
        Boolean isCrLineBreak = (Boolean) isLineBreakMethod.invoke(null, cr);
        assertTrue(isCrLineBreak);

        // Test with LF character
        Character lf = '\n';
        Boolean isLfLineBreak = (Boolean) isLineBreakMethod.invoke(null, lf);
        assertTrue(isLfLineBreak);

        // Test with null (should be false)
        Boolean isNullLineBreak = (Boolean) isLineBreakMethod.invoke(null, (Character) null);
        assertFalse(isNullLineBreak);

        // Now test that withEscape throws IllegalArgumentException with line break char
        Character[] lineBreaks = new Character[] { '\r', '\n' };

        for (Character lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> format.withEscape(lb.charValue()));
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }
}