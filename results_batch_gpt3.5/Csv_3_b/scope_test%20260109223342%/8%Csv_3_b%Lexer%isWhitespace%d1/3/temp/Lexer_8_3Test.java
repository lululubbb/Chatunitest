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

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerIsWhitespaceTest {

    private Lexer lexer;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        ExtendedBufferedReader inMock = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass of abstract Lexer to instantiate it
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withDelimiterChar_returnsFalse() throws Exception {
        // Arrange
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        // Use reflection to access isWhitespace method
        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        // Act
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, (int) delimiter);

        // Assert
        assertFalse(result, "isWhitespace should return false when character equals delimiter");
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withWhitespaceCharNotDelimiter_returnsTrue() throws Exception {
        // Arrange
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        char whitespaceChar = ' ';
        assertNotEquals(delimiter, whitespaceChar);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        // Act
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, (int) whitespaceChar);

        // Assert
        assertTrue(result, "isWhitespace should return true for whitespace chars not equal to delimiter");
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withNonWhitespaceNonDelimiterChar_returnsFalse() throws Exception {
        // Arrange
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        char nonWhitespaceChar = 'A';
        assertNotEquals(delimiter, nonWhitespaceChar);
        assertFalse(Character.isWhitespace(nonWhitespaceChar));

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        // Act
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, (int) nonWhitespaceChar);

        // Assert
        assertFalse(result, "isWhitespace should return false for non-whitespace and non-delimiter chars");
    }
}