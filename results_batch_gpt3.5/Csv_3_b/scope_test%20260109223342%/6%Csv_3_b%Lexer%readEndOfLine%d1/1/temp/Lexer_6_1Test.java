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

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_6_1Test {

    private Lexer lexer;
    private ExtendedBufferedReader in;

    @BeforeEach
    void setUp() throws Exception {
        in = mock(ExtendedBufferedReader.class);
        CSVFormat format = mock(CSVFormat.class);

        lexer = new Lexer(format, in) {
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
        inField.set(lexer, in);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withCR_followedByLF() throws Exception {
        final int CR = '\r';
        final int LF = '\n';

        when(in.lookAhead()).thenReturn(LF);
        when(in.read()).thenReturn(LF);

        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(lexer, CR);

        verify(in).lookAhead();
        verify(in).read();

        assertTrue(result, "readEndOfLine should return true for CR followed by LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withCR_notFollowedByLF() throws Exception {
        final int CR = '\r';
        final int other = 'a';

        when(in.lookAhead()).thenReturn(other);

        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(lexer, CR);

        verify(in).lookAhead();
        verify(in, never()).read();

        assertTrue(result, "readEndOfLine should return true for CR not followed by LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withLF() throws Exception {
        final int LF = '\n';

        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(lexer, LF);

        verify(in, never()).lookAhead();
        verify(in, never()).read();

        assertTrue(result, "readEndOfLine should return true for LF");
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_withOtherChar() throws Exception {
        final int other = 'x';

        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(lexer, other);

        verify(in, never()).lookAhead();
        verify(in, never()).read();

        assertFalse(result, "readEndOfLine should return false for non CR and non LF characters");
    }
}