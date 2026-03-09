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

class CSVFormatWithCommentStartTest {

    @Test
    @Timeout(8000)
    void testWithCommentStartValidChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat newFormat = format.withCommentStart(commentChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentStart());
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getEscape(), newFormat.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartNull() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat newFormat = format.withCommentStart((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentStart());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakCR() {
        CSVFormat format = CSVFormat.DEFAULT;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentStart('\r'));
        assertEquals("The comment start character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakLF() {
        CSVFormat format = CSVFormat.DEFAULT;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentStart('\n'));
        assertEquals("The comment start character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakNullChar() throws Exception {
        // Use reflection to invoke private static isLineBreak(Character)
        java.lang.reflect.Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with null character (should be false)
        Boolean result = (Boolean) isLineBreakMethod.invoke(null, (Object) null);
        assertFalse(result);

        // Test with CR (should be true)
        result = (Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\r'));
        assertTrue(result);

        // Test with LF (should be true)
        result = (Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\n'));
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakPrimitiveChar() throws Exception {
        // Use reflection to invoke private static isLineBreak(char)
        java.lang.reflect.Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test with CR (should be true)
        Boolean result = (Boolean) isLineBreakMethod.invoke(null, '\r');
        assertTrue(result);

        // Test with LF (should be true)
        result = (Boolean) isLineBreakMethod.invoke(null, '\n');
        assertTrue(result);

        // Test with non-line break char (should be false)
        result = (Boolean) isLineBreakMethod.invoke(null, 'a');
        assertFalse(result);
    }
}