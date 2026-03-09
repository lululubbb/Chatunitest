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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_30_5Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_validDelimiter_returnsNewInstance() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat result = original.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertEquals(newDelimiter, result.getDelimiter());
        // Other fields should be the same as original
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

        // Original instance remains unchanged
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), original.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiter_throwsIllegalArgumentException() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test delimiters that are line breaks
        char[] lineBreaks = new char[] { '\n', '\r' };
        for (char lb : lineBreaks) {
            // Confirm isLineBreak returns true for these chars
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, lb);
            assertTrue(isLineBreak);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                original.withDelimiter(lb);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_nonLineBreakChars() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        // Access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test some chars that are not line breaks
        char[] nonLineBreaks = new char[] { ',', ';', 'a', ' ' };
        for (char c : nonLineBreaks) {
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, c);
            assertFalse(isLineBreak);

            CSVFormat result = original.withDelimiter(c);
            assertEquals(c, result.getDelimiter());
        }
    }
}