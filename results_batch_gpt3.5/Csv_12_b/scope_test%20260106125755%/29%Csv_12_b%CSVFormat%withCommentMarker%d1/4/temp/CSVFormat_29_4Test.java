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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormatWithCommentMarkerTest {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char validCommentMarker = '#';

        CSVFormat result = format.withCommentMarker(validCommentMarker);

        assertNotNull(result);
        assertEquals(Character.valueOf(validCommentMarker), result.getCommentMarker());
        // Original format remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_nullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character nullChar = null;

        CSVFormat result = format.withCommentMarker(nullChar);

        assertNotNull(result);
        assertNull(result.getCommentMarker());
        // Original format remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakChar_throws() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke private static isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with '\n' (LF)
        Character lf = '\n';
        boolean isLfLineBreak = (boolean) isLineBreakMethod.invoke(null, lf);
        assertTrue(isLfLineBreak);

        // Test with '\r' (CR)
        Character cr = '\r';
        boolean isCrLineBreak = (boolean) isLineBreakMethod.invoke(null, cr);
        assertTrue(isCrLineBreak);

        // Test with non-line break char
        Character nonLineBreak = 'a';
        boolean isNonLineBreak = (boolean) isLineBreakMethod.invoke(null, nonLineBreak);
        assertFalse(isNonLineBreak);

        // Now test withCommentMarker throws IllegalArgumentException for line break chars
        Character[] lineBreakChars = new Character[] { '\n', '\r' };
        for (Character c : lineBreakChars) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(c);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_preservesOtherFields() throws Exception {
        // Use reflection to create CSVFormat instance with all fields set
        // Because constructor is private, we use reflection to access it
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ';',               // delimiter
                '"',               // quoteCharacter
                QuoteMode.MINIMAL, // quoteMode
                null,              // commentMarker
                '\\',              // escapeCharacter
                true,              // ignoreSurroundingSpaces
                false,             // ignoreEmptyLines
                "\n",              // recordSeparator
                "NULL",            // nullString
                new String[]{"a","b"}, // header
                true,              // skipHeaderRecord
                true               // allowMissingColumnNames
        );

        char commentMarker = '!';
        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }
}