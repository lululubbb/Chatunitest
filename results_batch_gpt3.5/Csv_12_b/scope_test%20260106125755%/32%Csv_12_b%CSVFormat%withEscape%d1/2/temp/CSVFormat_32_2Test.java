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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

public class CSVFormat_32_2Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_ValidCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
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
    public void testWithEscape_NullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character escapeChar = null;

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_LineBreakCharactersThrows() {
        CSVFormat original = CSVFormat.DEFAULT;

        char[] lineBreaks = { '\n', '\r' };

        for (char c : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                original.withEscape(c);
            });
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateStaticMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        // Test with line break chars
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        // Test with non line break char
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));

        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        // Test with line break chars
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, (Character) '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, (Character) '\r'));
        // Test with null
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
        // Test with non line break char
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) 'a'));
    }
}