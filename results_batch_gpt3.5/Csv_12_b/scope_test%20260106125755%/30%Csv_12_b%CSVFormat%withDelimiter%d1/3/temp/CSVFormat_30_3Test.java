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
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;

class CSVFormatWithDelimiterTest {

    @Test
    @Timeout(8000)
    public void testWithDelimiterValidChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';
        CSVFormat result = original.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertEquals(newDelimiter, result.getDelimiter());
        // Verify other fields are preserved
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
    public void testWithDelimiterThrowsExceptionForLineBreaks() throws Exception {
        char[] lineBreakChars = {'\n', '\r'};
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        for (char c : lineBreakChars) {
            // Confirm isLineBreak returns true for these chars
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(isLineBreak, "Expected isLineBreak to return true for char: " + (int)c);

            char delimiter = c;
            Executable executable = () -> format.withDelimiter(delimiter);
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, executable,
                    "Expected withDelimiter to throw for delimiter: " + (int)delimiter);
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiterWithNonLineBreakEdgeChars() {
        CSVFormat format = CSVFormat.DEFAULT;

        char[] validDelimiters = {'a', 'Z', '0', '\t', ' '};
        for (char delimiter : validDelimiters) {
            CSVFormat result = format.withDelimiter(delimiter);
            assertEquals(delimiter, result.getDelimiter());
        }
    }
}