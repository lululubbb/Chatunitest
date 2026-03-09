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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerIsWhitespaceTest {

    private Lexer lexer;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        ExtendedBufferedReader inMock = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass of Lexer because Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_charIsDelimiter() throws Exception {
        // delimiter char
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, (int) delimiter);
        assertFalse(result, "Delimiter char should not be considered whitespace");
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_charIsWhitespaceButNotDelimiter() throws Exception {
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        int space = ' ';
        boolean resultSpace = (boolean) isWhitespaceMethod.invoke(lexer, space);
        assertTrue(resultSpace, "Space should be considered whitespace when not delimiter");

        int tab = '\t';
        boolean resultTab = (boolean) isWhitespaceMethod.invoke(lexer, tab);
        assertTrue(resultTab, "Tab should be considered whitespace when not delimiter");

        int newline = '\n';
        boolean resultNewline = (boolean) isWhitespaceMethod.invoke(lexer, newline);
        assertTrue(resultNewline, "Newline should be considered whitespace when not delimiter");
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_charIsNotWhitespaceAndNotDelimiter() throws Exception {
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        int letterA = 'A';
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, letterA);
        assertFalse(result, "Non-whitespace non-delimiter char should return false");
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_charIsDelimiterAndWhitespace() throws Exception {
        // Unlikely scenario: delimiter is whitespace char, e.g. space
        char delimiter = ' ';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        int space = ' ';
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, space);
        assertFalse(result, "Delimiter char should not be considered whitespace even if whitespace char");
    }
}