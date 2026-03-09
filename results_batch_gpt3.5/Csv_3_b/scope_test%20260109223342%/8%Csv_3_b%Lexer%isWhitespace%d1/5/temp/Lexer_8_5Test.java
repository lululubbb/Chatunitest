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
    void setUp() {
        formatMock = mock(CSVFormat.class);
        ExtendedBufferedReader inMock = mock(ExtendedBufferedReader.class);

        // Anonymous subclass since Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_returnsFalse_whenCharIsDelimiter() throws Exception {
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, (int) delimiter);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_returnsTrue_whenCharIsWhitespaceAndNotDelimiter() throws Exception {
        char delimiter = 'x';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        int whitespaceChar = ' ';
        assertNotEquals(delimiter, whitespaceChar);

        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, whitespaceChar);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_returnsFalse_whenCharIsNotWhitespaceAndNotDelimiter() throws Exception {
        char delimiter = 'x';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        int nonWhitespaceChar = 'a';
        assertNotEquals(delimiter, nonWhitespaceChar);
        assertFalse(Character.isWhitespace((char) nonWhitespaceChar));

        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, nonWhitespaceChar);
        assertFalse(result);
    }
}