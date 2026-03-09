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
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerIsEscapeTest {

    private Lexer lexer;
    private CSVFormat formatMock;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        inMock = mock(ExtendedBufferedReader.class);

        // Create a concrete subclass of Lexer for testing since Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Use reflection to set the private final 'escape' field
        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);

        // Remove final modifier on the field so it can be set
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        escapeField.setChar(lexer, '"'); // set escape char to double quote for testing
    }

    @Test
    @Timeout(8000)
    void testIsEscape_whenCharIsEscape() throws Exception {
        // The char equals escape character
        char escapeChar = getEscapeChar(lexer);
        assertTrue(invokeIsEscape(lexer, escapeChar));
    }

    @Test
    @Timeout(8000)
    void testIsEscape_whenCharIsNotEscape() throws Exception {
        // The char does not equal escape character
        char escapeChar = getEscapeChar(lexer);
        char notEscapeChar = (char) (escapeChar + 1);
        assertFalse(invokeIsEscape(lexer, notEscapeChar));
    }

    // Helper method to get the private 'escape' field value
    private char getEscapeChar(Lexer lexer) throws Exception {
        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        return escapeField.getChar(lexer);
    }

    // Helper method to invoke package-private isEscape method via reflection
    private boolean invokeIsEscape(Lexer lexer, int c) throws Exception {
        Method isEscapeMethod = Lexer.class.getDeclaredMethod("isEscape", int.class);
        isEscapeMethod.setAccessible(true);
        return (boolean) isEscapeMethod.invoke(lexer, c);
    }
}