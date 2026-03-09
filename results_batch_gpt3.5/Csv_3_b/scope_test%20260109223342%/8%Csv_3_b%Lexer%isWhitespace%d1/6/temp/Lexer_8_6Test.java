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

class Lexer_8_6Test {

    private Lexer lexer;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        ExtendedBufferedReader inMock = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass of abstract Lexer for testing
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withWhitespaceButNotDelimiter() throws Exception {
        when(formatMock.getDelimiter()).thenReturn(',');

        // Space character (whitespace) but not delimiter ','
        int input = ' ';

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, input);

        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withDelimiter() throws Exception {
        when(formatMock.getDelimiter()).thenReturn(' ');

        // Space character which equals delimiter, so should return false
        int input = ' ';

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, input);

        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withNonWhitespaceNonDelimiter() throws Exception {
        when(formatMock.getDelimiter()).thenReturn(',');

        // Character 'A' is not whitespace and not delimiter
        int input = 'A';

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, input);

        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace_withDelimiterNonWhitespace() throws Exception {
        when(formatMock.getDelimiter()).thenReturn('A');

        // Character 'A' is delimiter but not whitespace, should return false
        int input = 'A';

        Method isWhitespaceMethod = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        isWhitespaceMethod.setAccessible(true);
        boolean result = (boolean) isWhitespaceMethod.invoke(lexer, input);

        assertFalse(result);
    }
}