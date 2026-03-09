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

public class CSVFormat_39_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_NullQuoteChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withQuote((Character) null);
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        // Original should remain unchanged
        assertEquals(original.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_ValidQuoteChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat result = original.withQuote(Character.valueOf(quoteChar));
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // Original should remain unchanged
        assertEquals(original.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_LineBreakQuoteChar_Throws() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test line break characters that should cause exception
        Character[] lineBreaks = new Character[] { '\n', '\r' };

        for (Character lb : lineBreaks) {
            // Confirm isLineBreak returns true for these characters
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, lb.charValue());
            assertTrue(isLineBreak);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withQuote(lb);
            });
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }
}