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

public class CSVFormat_30_2Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_validDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char delimiter = ';';

        CSVFormat result = original.withDelimiter(delimiter);

        assertNotNull(result);
        assertEquals(delimiter, result.getDelimiter());
        // All other fields should be the same as original
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreaksThrow() throws Exception {
        // Access private static method isLineBreak(char) via reflection
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test all line break chars that cause exception
        char[] lineBreaks = new char[]{'\r', '\n'};

        CSVFormat original = CSVFormat.DEFAULT;

        for (char lb : lineBreaks) {
            // Confirm isLineBreak returns true for these chars
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, lb);
            assertTrue(isLineBreak, "Expected isLineBreak to be true for char: " + (int) lb);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                original.withDelimiter(lb);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_nonLineBreakCharsDoNotThrow() throws Exception {
        // Access private static method isLineBreak(char) via reflection
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        CSVFormat original = CSVFormat.DEFAULT;

        // Test some chars that are not line breaks
        char[] nonLineBreaks = new char[]{',', ';', '|', 'A', '1', ' '};

        for (char c : nonLineBreaks) {
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, c);
            assertFalse(isLineBreak, "Expected isLineBreak to be false for char: " + (int) c);

            CSVFormat result = original.withDelimiter(c);
            assertNotNull(result);
            assertEquals(c, result.getDelimiter());
        }
    }
}