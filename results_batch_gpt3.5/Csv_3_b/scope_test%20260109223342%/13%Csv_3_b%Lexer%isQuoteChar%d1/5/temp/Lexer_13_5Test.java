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

class Lexer_13_5Test {

    private Lexer lexer;
    private char quoteChar;

    @BeforeEach
    void setUp() {
        // Create a concrete subclass of Lexer to instantiate it since Lexer is abstract
        lexer = new Lexer(Mockito.mock(CSVFormat.class), Mockito.mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
        // Use reflection to set the private final field quoteChar
        try {
            Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
            quoteCharField.setAccessible(true);
            quoteChar = '"';

            // Remove final modifier for the field (works in Java 8 and below)
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~Modifier.FINAL);

            quoteCharField.set(lexer, quoteChar);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void isQuoteChar_returnsTrue_whenCharEqualsQuoteChar() {
        assertTrue(lexer.isQuoteChar(quoteChar));
    }

    @Test
    @Timeout(8000)
    void isQuoteChar_returnsFalse_whenCharDiffersFromQuoteChar() {
        assertFalse(lexer.isQuoteChar('a'));
        assertFalse(lexer.isQuoteChar(0));
        assertFalse(lexer.isQuoteChar('\uffff'));
    }
}