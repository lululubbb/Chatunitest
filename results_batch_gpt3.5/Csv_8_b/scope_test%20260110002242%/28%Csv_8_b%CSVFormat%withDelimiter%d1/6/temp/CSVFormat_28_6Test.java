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
    void testWithDelimiter_ValidDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat result = original.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertEquals(newDelimiter, result.getDelimiter());
        // Other properties remain unchanged
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getEscape(), result.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_LineBreakDelimiters() throws Exception {
        char[] lineBreakChars = {'\r', '\n'};

        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        for (char c : lineBreakChars) {
            // Verify private static isLineBreak returns true for line break chars
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(isLineBreak, "isLineBreak should return true for char: " + (int)c);

            CSVFormat original = CSVFormat.DEFAULT;
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                original.withDelimiter(c);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_NonLineBreakChars() throws Exception {
        char[] nonLineBreakChars = {'a', '1', ' ', '|', '\t'};

        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        for (char c : nonLineBreakChars) {
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, c);
            assertFalse(isLineBreak, "isLineBreak should return false for char: " + (int)c);

            CSVFormat original = CSVFormat.DEFAULT;
            CSVFormat result = original.withDelimiter(c);
            assertEquals(c, result.getDelimiter());
        }
    }
}