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
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_12_3Test {

    private Lexer lexer;
    private CSVFormat formatMock;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        inMock = mock(ExtendedBufferedReader.class);

        // Create an anonymous subclass of Lexer since Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    private void setEscapeChar(char c) throws Exception {
        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);

        // Remove final modifier via reflection to set the value
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        escapeField.setChar(lexer, c);
    }

    @Test
    @Timeout(8000)
    void testIsEscape_withEscapeChar() throws Exception {
        setEscapeChar('e');

        // c == escape, should return true
        assertTrue(lexer.isEscape('e'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape_withDifferentChar() throws Exception {
        setEscapeChar('e');

        // c != escape, should return false
        assertFalse(lexer.isEscape('x'));
        assertFalse(lexer.isEscape('\0'));
        assertFalse(lexer.isEscape('E'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape_withDisabledEscapeChar() throws Exception {
        // Use reflection to get DISABLED constant value
        Field disabledField = Lexer.class.getDeclaredField("DISABLED");
        disabledField.setAccessible(true);
        char disabledChar = (char) disabledField.get(null);

        setEscapeChar(disabledChar);

        // c == escape (DISABLED), should return true
        assertTrue(lexer.isEscape(disabledChar));

        // c != escape, should return false
        assertFalse(lexer.isEscape('e'));
    }
}