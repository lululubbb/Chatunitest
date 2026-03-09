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
        char commentChar = '#'; // use primitive char to match method signature
        CSVFormat newFormat = format.withCommentStart(commentChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentStart());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getEscape(), newFormat.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withCommentStart((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getCommentStart());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getEscape(), newFormat.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakCharCR() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '\r'; // use primitive char
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentStart(commentChar));
        assertEquals("The comment start character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakCharLF() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '\n'; // use primitive char
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentStart(commentChar));
        assertEquals("The comment start character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakCharNullChar() throws Exception {
        // Use reflection to invoke private static isLineBreak(Character)
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) method.invoke(null, (Character) null));
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartLineBreakCharPrimitive() throws Exception {
        // Use reflection to invoke private static isLineBreak(char)
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertFalse((Boolean) method.invoke(null, 'a'));
    }
}