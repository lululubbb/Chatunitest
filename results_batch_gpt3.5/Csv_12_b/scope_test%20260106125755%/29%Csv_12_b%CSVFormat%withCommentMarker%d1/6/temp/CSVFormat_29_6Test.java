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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

public class CSVFormat_29_6Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_ValidCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character commentMarker = '#';

        CSVFormat newFormat = baseFormat.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(commentMarker, newFormat.getCommentMarker());
        // Original instance should remain unchanged
        assertNull(baseFormat.getCommentMarker());
        // Other fields should remain equal
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_NullCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat newFormat = baseFormat.withCommentMarker((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_LineBreakCharacters_Throws() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Use reflection to access private static isLineBreak(Character)
        try {
            Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
            isLineBreakMethod.setAccessible(true);

            // Characters that should be considered line breaks
            Character[] lineBreaks = {'\n', '\r'};

            for (Character lb : lineBreaks) {
                // Confirm isLineBreak returns true for these characters
                boolean result = (boolean) isLineBreakMethod.invoke(null, lb);
                assertTrue(result);
                // Test that withCommentMarker throws IllegalArgumentException for line break character
                IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                        () -> baseFormat.withCommentMarker(lb));
                assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
            }
        } catch (ReflectiveOperationException e) {
            fail("Reflection failure: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_PrivateIsLineBreakCharacter() throws Exception {
        // Access private static method isLineBreak(Character) via reflection
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, '#'));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, (Character) null));
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_PrivateIsLineBreakChar() throws Exception {
        // Access private static method isLineBreak(char) via reflection
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, '#'));
    }
}