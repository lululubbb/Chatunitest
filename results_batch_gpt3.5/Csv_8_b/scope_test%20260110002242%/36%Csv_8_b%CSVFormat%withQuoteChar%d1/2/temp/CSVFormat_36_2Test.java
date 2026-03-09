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

public class CSVFormat_36_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_validChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newQuote = '\'';
        CSVFormat updated = original.withQuoteChar(newQuote);
        assertNotNull(updated);
        assertEquals(Character.valueOf(newQuote), updated.getQuoteChar());
        // Original should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), original.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_nullChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withQuoteChar((Character) null);
        assertNotNull(updated);
        assertNull(updated.getQuoteChar());
        // Original should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), original.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_lineBreakCharThrows() {
        CSVFormat original = CSVFormat.DEFAULT;

        Character[] lineBreaks = new Character[] {'\n', '\r'};

        for (Character c : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                original.withQuoteChar(c);
            });
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_privateStaticMethod() throws Exception {
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, 'a'));

        Method isLineBreakCharacterMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacterMethod.setAccessible(true);
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\r')));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, (Character) null));
    }
}