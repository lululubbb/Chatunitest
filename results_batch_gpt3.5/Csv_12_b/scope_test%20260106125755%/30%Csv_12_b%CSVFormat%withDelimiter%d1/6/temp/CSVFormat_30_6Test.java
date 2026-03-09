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

public class CSVFormat_30_6Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_validDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';
        CSVFormat result = original.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertEquals(newDelimiter, result.getDelimiter());
        // Other properties remain the same
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
        // original instance unchanged
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiterThrows() throws Exception {
        // Use reflection to invoke private static isLineBreak(char) method to get line break chars
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        char[] lineBreakChars = new char[] {'\r', '\n'};

        for (char c : lineBreakChars) {
            assertTrue((Boolean) isLineBreakMethod.invoke(null, c));
            CSVFormat original = CSVFormat.DEFAULT;
            char delimiter = c;
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> original.withDelimiter(delimiter));
            assertEquals("The delimiter cannot be a line break", ex.getMessage());
        }
    }
}