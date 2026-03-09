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

class Lexer_8_2Test {

    private Lexer lexer;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        ExtendedBufferedReader inMock = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass since Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_whenCharIsWhitespaceAndNotDelimiter() throws Exception {
        // Arrange
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);
        int whitespaceChar = ' '; // space is whitespace

        // Act
        Method method = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, whitespaceChar);

        // Assert
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_whenCharIsWhitespaceButIsDelimiter() throws Exception {
        // Arrange
        char delimiter = ' ';
        when(formatMock.getDelimiter()).thenReturn(delimiter);
        int whitespaceChar = ' '; // space is whitespace and delimiter

        // Act
        Method method = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, whitespaceChar);

        // Assert
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_whenCharIsNotWhitespaceAndNotDelimiter() throws Exception {
        // Arrange
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);
        int nonWhitespaceChar = 'A'; // 'A' is not whitespace

        // Act
        Method method = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, nonWhitespaceChar);

        // Assert
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_whenCharIsNotWhitespaceButIsDelimiter() throws Exception {
        // Arrange
        char delimiter = 'A';
        when(formatMock.getDelimiter()).thenReturn(delimiter);
        int nonWhitespaceChar = 'A'; // 'A' is delimiter but not whitespace

        // Act
        Method method = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, nonWhitespaceChar);

        // Assert
        assertFalse(result);
    }
}