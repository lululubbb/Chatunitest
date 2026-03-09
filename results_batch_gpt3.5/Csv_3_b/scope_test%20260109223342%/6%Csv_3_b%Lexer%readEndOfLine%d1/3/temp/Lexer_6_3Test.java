package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_6_3Test {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() throws Exception {
        inMock = mock(ExtendedBufferedReader.class);
        CSVFormat format = mock(CSVFormat.class);

        // Create an anonymous subclass of Lexer since Lexer is abstract
        lexer = new Lexer(format, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }

            @Override
            boolean isWhitespace(int c) {
                return false;
            }

            @Override
            boolean isStartOfLine(int c) {
                return false;
            }

            @Override
            boolean isEndOfFile(int c) {
                return false;
            }

            @Override
            boolean isDelimiter(int c) {
                return false;
            }

            @Override
            boolean isEscape(int c) {
                return false;
            }

            @Override
            boolean isQuoteChar(int c) {
                return false;
            }

            @Override
            boolean isCommentStart(int c) {
                return false;
            }
        };

        // Use reflection to set the private final field 'in' to our mock
        Field inField = Lexer.class.getDeclaredField("in");
        inField.setAccessible(true);
        inField.set(lexer, inMock);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withCRFollowedByLF() throws Exception {
        // Setup: c == CR and lookAhead() returns LF, read() returns LF
        when(inMock.lookAhead()).thenReturn((int) Constants.LF);
        when(inMock.read()).thenReturn((int) Constants.LF);

        boolean result = lexer.readEndOfLine(Constants.CR);

        verify(inMock).lookAhead();
        verify(inMock).read();

        assertTrue(result, "Expected true for CR followed by LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withCRNotFollowedByLF() throws Exception {
        // Setup: c == CR and lookAhead() returns something other than LF
        when(inMock.lookAhead()).thenReturn((int) 'x');

        boolean result = lexer.readEndOfLine(Constants.CR);

        verify(inMock).lookAhead();
        verify(inMock, never()).read();

        assertTrue(result, "Expected true for CR not followed by LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withLF() throws Exception {
        // Setup: c == LF
        // lookAhead() and read() should not be called
        boolean result = lexer.readEndOfLine(Constants.LF);

        verify(inMock, never()).lookAhead();
        verify(inMock, never()).read();

        assertTrue(result, "Expected true for LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withOtherChar() throws Exception {
        // Setup: c is neither CR nor LF
        int c = 'x';

        boolean result = lexer.readEndOfLine(c);

        verify(inMock, never()).lookAhead();
        verify(inMock, never()).read();

        assertFalse(result, "Expected false for character other than CR or LF");
    }
}