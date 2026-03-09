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

class Lexer_13_4Test {

    private Lexer lexer;
    private char quoteChar = '"';

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Use reflection to set the private final quoteChar field
        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);

        // Remove final modifier for Java 12+ compatibility (modifiers field may not exist in newer Java versions)
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // Java 12+ does not have 'modifiers' field, ignore
        }

        quoteCharField.set(lexer, quoteChar);
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_whenCharEqualsQuoteChar_shouldReturnTrue() {
        assertTrue(lexer.isQuoteChar(quoteChar));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_whenCharNotEqualsQuoteChar_shouldReturnFalse() {
        assertFalse(lexer.isQuoteChar('a'));
        assertFalse(lexer.isQuoteChar('\0'));
        assertFalse(lexer.isQuoteChar((char)(quoteChar + 1)));
        assertFalse(lexer.isQuoteChar((char)(quoteChar - 1)));
    }
}