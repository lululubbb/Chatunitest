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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_6_6Test {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() throws Exception {
        inMock = mock(ExtendedBufferedReader.class);
        CSVFormat format = mock(CSVFormat.class);

        // Create anonymous subclass to instantiate abstract Lexer
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

        // Inject the mocked ExtendedBufferedReader into the private final field 'in' of Lexer
        Field inField = Lexer.class.getDeclaredField("in");
        inField.setAccessible(true);
        inField.set(lexer, inMock);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withCRFollowedByLF() throws Exception {
        // Arrange
        when(inMock.lookAhead()).thenReturn((int) '\n');
        when(inMock.read()).thenReturn((int) '\n');

        // Act
        boolean result = invokeReadEndOfLine('\r');

        // Assert
        verify(inMock).lookAhead();
        verify(inMock).read();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withCRNotFollowedByLF() throws Exception {
        // Arrange
        when(inMock.lookAhead()).thenReturn((int) 'x'); // not LF

        // Act
        boolean result = invokeReadEndOfLine('\r');

        // Assert
        verify(inMock).lookAhead();
        verify(inMock, never()).read();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withLF() throws Exception {
        // Act
        boolean result = invokeReadEndOfLine('\n');

        // Assert
        verify(inMock, never()).lookAhead();
        verify(inMock, never()).read();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withOtherChar() throws Exception {
        // Act
        boolean result = invokeReadEndOfLine('x');

        // Assert
        verify(inMock, never()).lookAhead();
        verify(inMock, never()).read();
        assertFalse(result);
    }

    private boolean invokeReadEndOfLine(int c) throws Exception {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);
        try {
            return (boolean) method.invoke(lexer, c);
        } catch (Exception e) {
            // Unwrap invocation target exception if present
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw e;
        }
    }
}