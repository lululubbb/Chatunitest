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

class Lexer_8_4Test {

    private Lexer lexer;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        ExtendedBufferedReader inMock = mock(ExtendedBufferedReader.class);

        // Create a concrete subclass of Lexer since Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withDelimiterCharacter() throws Exception {
        // Arrange
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        // Act
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, (int) delimiter);

        // Assert
        assertFalse(result, "Delimiter character should not be considered whitespace");
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withWhitespaceCharacter() throws Exception {
        // Arrange
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        // Test with TAB (whitespace, not delimiter)
        boolean tabResult = (boolean) isWhitespaceMethod.invoke(lexer, (int) '\t');
        assertTrue(tabResult, "TAB should be considered whitespace");

        // Test with SPACE (whitespace, not delimiter)
        boolean spaceResult = (boolean) isWhitespaceMethod.invoke(lexer, (int) ' ');
        assertTrue(spaceResult, "SPACE should be considered whitespace");

        // Test with LF (whitespace, not delimiter)
        boolean lfResult = (boolean) isWhitespaceMethod.invoke(lexer, (int) '\n');
        assertTrue(lfResult, "LF should be considered whitespace");
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withNonWhitespaceNonDelimiterCharacter() throws Exception {
        // Arrange
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        // Act
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, (int) 'A');

        // Assert
        assertFalse(result, "'A' is not whitespace and not delimiter, so should return false");
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withDelimiterThatIsWhitespace() throws Exception {
        // Arrange
        // Use a whitespace char as delimiter (e.g. space)
        char delimiter = ' ';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);

        // Act
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, (int) delimiter);

        // Assert
        // Because c == delimiter, should return false even though space is whitespace
        assertFalse(result, "Delimiter character should not be considered whitespace even if it is whitespace");
    }
}