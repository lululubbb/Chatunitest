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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_7_6Test {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        inMock = mock(ExtendedBufferedReader.class);
        formatMock = mock(CSVFormat.class);
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                // Provide a dummy implementation to avoid calling abstract super method
                return reusableToken;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testNextToken_withReusableToken() throws IOException {
        Token reusableToken = mock(Token.class);

        Lexer spyLexer = spy(lexer);
        Token expectedToken = mock(Token.class);
        doReturn(expectedToken).when(spyLexer).nextToken(reusableToken);

        Token actualToken = spyLexer.nextToken(reusableToken);
        assertSame(expectedToken, actualToken);
        verify(spyLexer).nextToken(reusableToken);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        Character input = null;
        char result = (char) method.invoke(lexer, input);
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        Character input = 'a';
        char result = (char) method.invoke(lexer, input);
        assertEquals('a', result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(0L, lineNumber);
    }

    @Test
    @Timeout(8000)
    void testReadEscape() throws IOException {
        try {
            when(inMock.read()).thenReturn((int) '\\');
            int result = lexer.readEscape();
            assertTrue(result >= 0);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        StringBuilder sb = new StringBuilder("abc   ");
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());

        sb = new StringBuilder("abc");
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());

        sb = new StringBuilder("   ");
        method.invoke(lexer, sb);
        assertEquals("", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine() throws IOException {
        assertTrue(lexer.readEndOfLine('\r'));
        assertTrue(lexer.readEndOfLine('\n'));
        assertTrue(lexer.readEndOfLine('\f'));
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
        assertTrue(lexer.isStartOfLine('\r'));
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
    void testIsDelimiter() throws NoSuchFieldException, IllegalAccessException {
        Field delimiterField = Lexer.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = (char) delimiterField.get(lexer);

        assertTrue(lexer.isDelimiter(delimiter));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() throws NoSuchFieldException, IllegalAccessException {
        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        char escape = (char) escapeField.get(lexer);

        assertTrue(lexer.isEscape(escape));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() throws NoSuchFieldException, IllegalAccessException {
        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        char quoteChar = (char) quoteCharField.get(lexer);

        assertTrue(lexer.isQuoteChar(quoteChar));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() throws NoSuchFieldException, IllegalAccessException {
        Field commentStartField = Lexer.class.getDeclaredField("commmentStart".replace("commmentStart", "commentStart"));
        commentStartField.setAccessible(true);
        char commentStart = (char) commentStartField.get(lexer);

        assertTrue(lexer.isCommentStart(commentStart));
        assertFalse(lexer.isCommentStart('a'));
    }
}