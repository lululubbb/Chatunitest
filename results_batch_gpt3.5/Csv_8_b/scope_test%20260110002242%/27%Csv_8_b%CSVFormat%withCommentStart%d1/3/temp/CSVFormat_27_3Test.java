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

class CSVFormatWithCommentStartTest {

    @Test
    @Timeout(8000)
    void withCommentStart_validChar_returnsNewCSVFormatWithCommentStart() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat result = original.withCommentStart(commentChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(commentChar), result.getCommentStart());
        // Other fields should remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
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
    void withCommentStart_nullChar_returnsNewCSVFormatWithNullCommentStart() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character commentChar = null;

        CSVFormat result = original.withCommentStart(commentChar);

        assertNotNull(result);
        assertNull(result.getCommentStart());
        // Other fields should remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
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
    void withCommentStart_lineBreakChar_throwsIllegalArgumentException() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke private static isLineBreak(char)
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        // Check known line break characters that should cause exception
        char[] lineBreaks = {
            '\n',
            '\r'
        };

        for (char lineBreak : lineBreaks) {
            // Confirm private method returns true for lineBreak
            assertTrue((Boolean) method.invoke(null, lineBreak));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> original.withCommentStart(lineBreak));
            assertEquals("The comment start character cannot be a line break", ex.getMessage());
        }
    }
}