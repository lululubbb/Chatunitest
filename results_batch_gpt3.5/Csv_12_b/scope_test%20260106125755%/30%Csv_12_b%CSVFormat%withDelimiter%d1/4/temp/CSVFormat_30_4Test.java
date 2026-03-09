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

public class CSVFormat_30_4Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiterValid() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';
        CSVFormat updated = original.withDelimiter(newDelimiter);

        assertNotNull(updated);
        assertEquals(newDelimiter, updated.getDelimiter());
        // Ensure other properties remain unchanged
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiterThrowsOnLineBreaks() {
        CSVFormat original = CSVFormat.DEFAULT;

        char[] lineBreaks = { '\n', '\r' };

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                original.withDelimiter(lb);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakPrivateMethod() throws Exception {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Line break characters return true
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\r'));

        // Non-line break characters return false
        assertFalse((Boolean) isLineBreakMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, ' '));
    }
}