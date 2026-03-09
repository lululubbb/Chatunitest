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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CSVFormat_36_5Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_NormalCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat newFormat = format.withQuoteChar(quoteChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(quoteChar), newFormat.getQuoteChar());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getCommentStart(), newFormat.getCommentStart());
        assertEquals(format.getEscape(), newFormat.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_NullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = null;
        CSVFormat newFormat = format.withQuoteChar(quoteChar);
        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_ThrowsOnLineBreak() throws Exception {
        // Use reflection to access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test all line break characters for which isLineBreak returns true
        Character[] lineBreaks = { '\n', '\r' };

        for (Character lb : lineBreaks) {
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, lb);
            assertTrue(result, "Expected isLineBreak to be true for character: " + (int) lb.charValue());
            CSVFormat format = CSVFormat.DEFAULT;
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> format.withQuoteChar(lb));
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateMethodChar() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ' '));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateMethodCharacter() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, ' '));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}