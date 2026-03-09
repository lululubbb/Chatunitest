package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LexerReadEndOfLineTest {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() throws Exception {
        inMock = mock(ExtendedBufferedReader.class);
        CSVFormat format = mock(CSVFormat.class);

        // Create anonymous subclass of Lexer because Lexer is abstract
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

        // Use reflection to set the private final field 'in' to the mock
        Field inField = Lexer.class.getDeclaredField("in");
        inField.setAccessible(true);
        inField.set(lexer, inMock);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_CRFollowedByLF() throws Exception {
        when(inMock.lookAhead()).thenReturn((int) LF);
        when(inMock.read()).thenReturn((int) LF);

        boolean result = lexer.readEndOfLine(CR);

        verify(inMock).lookAhead();
        verify(inMock).read();
        assertTrue(result, "Should return true for CR followed by LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_CRNotFollowedByLF() throws IOException {
        when(inMock.lookAhead()).thenReturn((int) 'a');

        boolean result = lexer.readEndOfLine(CR);

        verify(inMock).lookAhead();
        verify(inMock, never()).read();
        assertTrue(result, "Should return true for CR not followed by LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_LF() throws IOException {
        boolean result = lexer.readEndOfLine(LF);

        verify(inMock, never()).lookAhead();
        verify(inMock, never()).read();
        assertTrue(result, "Should return true for LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_OtherChar() throws IOException {
        int c = 'x';
        boolean result = lexer.readEndOfLine(c);

        verify(inMock, never()).lookAhead();
        verify(inMock, never()).read();
        assertFalse(result, "Should return false for non CR/LF char");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_CRFollowedByLF_readThrowsIOException() throws Exception {
        when(inMock.lookAhead()).thenReturn((int) LF);
        when(inMock.read()).thenThrow(new IOException("read error"));

        IOException thrown = assertThrows(IOException.class, () -> lexer.readEndOfLine(CR));
        assertEquals("read error", thrown.getMessage());

        verify(inMock).lookAhead();
        verify(inMock).read();
    }
}