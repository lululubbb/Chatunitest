package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class Lexer_13_1Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);

        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Use reflection to set the private final field quoteChar
        try {
            Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
            quoteCharField.setAccessible(true);

            // Remove final modifier on quoteChar field
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~Modifier.FINAL);

            // Set the quoteChar field to '"'
            quoteCharField.set(lexer, '"');
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set quoteChar field via reflection: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_withQuoteChar() {
        assertTrue(lexer.isQuoteChar('"'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_withNonQuoteChar() {
        assertFalse(lexer.isQuoteChar('a'));

        // Access private static final DISABLED field via reflection
        try {
            Field disabledField = Lexer.class.getDeclaredField("DISABLED");
            disabledField.setAccessible(true);
            char disabledValue = disabledField.getChar(null);
            assertFalse(lexer.isQuoteChar(disabledValue));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access DISABLED field via reflection: " + e.getMessage());
        }

        assertFalse(lexer.isQuoteChar('\0'));
        assertFalse(lexer.isQuoteChar(' '));
        assertFalse(lexer.isQuoteChar('\n'));
    }
}