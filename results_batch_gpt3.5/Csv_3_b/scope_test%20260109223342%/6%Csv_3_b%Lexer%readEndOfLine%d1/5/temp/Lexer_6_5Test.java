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

class Lexer_6_5Test {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() throws Exception {
        inMock = mock(ExtendedBufferedReader.class);
        CSVFormat format = mock(CSVFormat.class);

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

        // Inject the mocked inMock into the private final field 'in' of Lexer
        Field inField = Lexer.class.getDeclaredField("in");
        inField.setAccessible(true);
        inField.set(lexer, inMock);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_CR_followedByLF() throws IOException {
        when(inMock.lookAhead()).thenReturn((int) '\n');
        when(inMock.read()).thenReturn((int) '\n');

        boolean result = lexer.readEndOfLine('\r');

        verify(inMock).lookAhead();
        verify(inMock).read();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_CR_notFollowedByLF() throws IOException {
        when(inMock.lookAhead()).thenReturn((int) 'x');

        boolean result = lexer.readEndOfLine('\r');

        verify(inMock).lookAhead();
        verify(inMock, never()).read();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_LF() throws IOException {
        boolean result = lexer.readEndOfLine('\n');

        verify(inMock, never()).lookAhead();
        verify(inMock, never()).read();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_otherChar() throws IOException {
        boolean result = lexer.readEndOfLine('a');

        verify(inMock, never()).lookAhead();
        verify(inMock, never()).read();
        assertFalse(result);
    }
}