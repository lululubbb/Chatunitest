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

public class CSVFormat_30_1Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_validDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat newFormat = format.withDelimiter(newDelimiter);

        assertNotNull(newFormat);
        assertEquals(newDelimiter, newFormat.getDelimiter());
        // Other fields should remain the same as original
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiterThrows() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to access private static isLineBreak(char) method
        try {
            Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
            isLineBreakMethod.setAccessible(true);

            // Test with '\n' (LF)
            char lf = '\n';
            boolean isLfLineBreak = (boolean) isLineBreakMethod.invoke(null, lf);
            assertTrue(isLfLineBreak);

            // Test with '\r' (CR)
            char cr = '\r';
            boolean isCrLineBreak = (boolean) isLineBreakMethod.invoke(null, cr);
            assertTrue(isCrLineBreak);

            // Test with other char (e.g. 'a')
            char a = 'a';
            boolean isALineBreak = (boolean) isLineBreakMethod.invoke(null, a);
            assertFalse(isALineBreak);

        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }

        // Delimiters that are line breaks should throw IllegalArgumentException
        char[] lineBreakDelimiters = {'\n', '\r'};
        for (char delimiter : lineBreakDelimiters) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withDelimiter(delimiter);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }
}