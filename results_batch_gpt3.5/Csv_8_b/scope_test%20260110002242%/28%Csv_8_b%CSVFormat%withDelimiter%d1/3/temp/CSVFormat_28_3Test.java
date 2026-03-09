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

class CSVFormatWithDelimiterTest {

    @Test
    @Timeout(8000)
    void testWithDelimiter_ValidDelimiter_ReturnsNewInstanceWithDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';
        assertNotEquals(newDelimiter, original.getDelimiter());

        CSVFormat updated = original.withDelimiter(newDelimiter);

        assertNotNull(updated);
        assertEquals(newDelimiter, updated.getDelimiter());
        // Other properties remain the same
        assertEquals(original.getQuoteChar(), updated.getQuoteChar());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_LineBreakDelimiter_ThrowsIllegalArgumentException() {
        CSVFormat original = CSVFormat.DEFAULT;

        char[] lineBreakDelimiters = {'\r', '\n'};

        for (char lb : lineBreakDelimiters) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> original.withDelimiter(lb));
            assertEquals("The delimiter cannot be a line break", ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_PrivateStaticMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        // Line breaks
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));

        // Non-line breaks
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ' '));
    }
}