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

class Lexer_13_2Test {

    private Lexer lexer;

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

        // Using reflection to set the private final field quoteChar
        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);

        // Remove final modifier using reflection (for Java 8+)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~Modifier.FINAL);

        // Set quoteChar to '"'
        quoteCharField.set(lexer, '"');
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_MatchingQuoteChar() {
        assertTrue(lexer.isQuoteChar('"'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_NonMatchingChar() {
        assertFalse(lexer.isQuoteChar('a'));
        assertFalse(lexer.isQuoteChar('\0'));
        assertFalse(lexer.isQuoteChar('\''));
        assertFalse(lexer.isQuoteChar(' '));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_DifferentQuoteChar() throws Exception {
        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~Modifier.FINAL);

        quoteCharField.set(lexer, '\'');

        assertTrue(lexer.isQuoteChar('\''));
        assertFalse(lexer.isQuoteChar('"'));
    }
}