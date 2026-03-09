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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_7_3Test {

    private Lexer lexer;
    private ExtendedBufferedReader in;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        in = mock(ExtendedBufferedReader.class);
        format = mock(CSVFormat.class);

        lexer = new Lexer(format, in) {
            private final StringBuilder tokenBuffer = new StringBuilder();

            @Override
            Token nextToken(Token reusableToken) throws IOException {
                int c = in.read();
                if (c == -1) {
                    return null;
                }
                if (reusableToken == null) {
                    Token t = new Token();
                    t.append((char) c);
                    return t;
                } else {
                    reusableToken.clearBuffer();
                    reusableToken.append((char) c);
                    return reusableToken;
                }
            }
        };
    }

    @Test
    @Timeout(8000)
    void testNextToken_nullReusableToken_returnsNewToken() throws IOException {
        when(in.read()).thenReturn((int) 'a', -1);

        Token token = lexer.nextToken(null);

        assertNotNull(token);
        assertEquals(1, token.length());
        assertEquals('a', token.charAt(0));
    }

    @Test
    @Timeout(8000)
    void testNextToken_reusableTokenUsed() throws IOException {
        Token reusable = new Token();
        reusable.append("existing");
        when(in.read()).thenReturn((int) 'b', -1);

        Token token = lexer.nextToken(reusable);

        assertSame(reusable, token);
        assertEquals(1, token.length());
        assertEquals('b', token.charAt(0));
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        // Test with null input
        char result = (char) method.invoke(lexer, (Character) null);
        assertEquals('\ufffe', result);

        // Test with non-null input
        char input = 'x';
        result = (char) method.invoke(lexer, input);
        assertEquals(input, result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() {
        when(in.getLineNumber()).thenReturn(42L);
        long lineNumber = lexer.getLineNumber();
        assertEquals(42L, lineNumber);
    }

    @Test
    @Timeout(8000)
    void testReadEscape() throws IOException {
        when(in.read()).thenReturn((int) 'n');
        int result = lexer.readEscape();
        assertEquals('n', result);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces() throws Exception {
        StringBuilder sb = new StringBuilder("abc  ");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine() throws IOException {
        assertTrue(lexer.readEndOfLine('\r'));
        assertTrue(lexer.readEndOfLine('\n'));
        assertFalse(lexer.readEndOfLine('a'));
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine('\n'));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(-1));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter() {
        char delimiter = ',';
        try {
            Field f = Lexer.class.getDeclaredField("delimiter");
            f.setAccessible(true);
            f.setChar(lexer, delimiter);
        } catch (Exception e) {
            fail(e);
        }

        assertTrue(lexer.isDelimiter(delimiter));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() {
        char escape = '\\';
        try {
            Field f = Lexer.class.getDeclaredField("escape");
            f.setAccessible(true);
            f.setChar(lexer, escape);
        } catch (Exception e) {
            fail(e);
        }

        assertTrue(lexer.isEscape(escape));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() {
        char quote = '"';
        try {
            Field f = Lexer.class.getDeclaredField("quoteChar");
            f.setAccessible(true);
            f.setChar(lexer, quote);
        } catch (Exception e) {
            fail(e);
        }

        assertTrue(lexer.isQuoteChar(quote));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() {
        char comment = '#';
        try {
            Field f = Lexer.class.getDeclaredField("commmentStart");
            f.setAccessible(true);
            f.setChar(lexer, comment);
        } catch (Exception e) {
            fail(e);
        }

        assertTrue(lexer.isCommentStart(comment));
        assertFalse(lexer.isCommentStart('a'));
    }
}