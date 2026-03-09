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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_7_2Test {

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
                // Provide a simple implementation for testing purposes
                // This is to allow instantiation of abstract Lexer
                return null;
            }
        };
    }

    // Helper method to create a Token instance via reflection (no-arg constructor)
    private Token createToken() {
        try {
            return Token.class.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to set Token value via reflection (setValue method does not exist, use reflection on field or toString override)
    private void setTokenValue(Token token, String value) {
        try {
            // Try to set a private field "value" or similar if exists
            // If not, try to find a method to set value
            // Since no source is provided, we assume a private field "value" of type String
            Field field = Token.class.getDeclaredField("value");
            field.setAccessible(true);
            field.set(token, value);
        } catch (NoSuchFieldException e) {
            // fallback: do nothing or throw
            throw new RuntimeException("Token class does not have a 'value' field");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testNextToken_withReusableToken_null() throws IOException {
        Lexer testLexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                assertNull(reusableToken);
                Token token = createToken();
                setTokenValue(token, "token");
                return token;
            }
        };
        Token result = testLexer.nextToken(null);
        assertNotNull(result);
        assertEquals("token", result.toString());
    }

    @Test
    @Timeout(8000)
    void testNextToken_withReusableToken_notNull() throws IOException {
        Token reusable = createToken();
        setTokenValue(reusable, "reuse");
        Lexer testLexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                assertSame(reusable, reusableToken);
                setTokenValue(reusableToken, "newtoken");
                return reusableToken;
            }
        };
        Token result = testLexer.nextToken(reusable);
        assertSame(reusable, result);
        assertEquals("newtoken", result.toString());
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);
        char result = (char) method.invoke(lexer, (Character) null);
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withCharacter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);
        char input = 'a';
        char result = (char) method.invoke(lexer, input);
        assertEquals(input, result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertTrue(lineNumber >= 0);
    }

    @Test
    @Timeout(8000)
    void testReadEscape() throws IOException {
        when(inMock.read()).thenReturn((int) '\\');
        int c = lexer.readEscape();
        assertEquals('\\', c);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);
        StringBuilder buffer = new StringBuilder("abc  \t ");
        method.invoke(lexer, buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine() throws IOException {
        assertTrue(lexer.readEndOfLine('\n'));
        assertTrue(lexer.readEndOfLine('\r'));
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
        assertTrue(lexer.isStartOfLine('\r'));
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
    void testIsDelimiter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method method = Lexer.class.getDeclaredMethod("isDelimiter", int.class);
        method.setAccessible(true);

        Field delimiterField = Lexer.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        delimiterField.setChar(lexer, ',');

        assertTrue((Boolean) method.invoke(lexer, (int) ','));
        assertFalse((Boolean) method.invoke(lexer, (int) ';'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isEscape", int.class);
        method.setAccessible(true);

        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        escapeField.setChar(lexer, '\\');

        assertTrue((Boolean) method.invoke(lexer, (int) '\\'));
        assertFalse((Boolean) method.invoke(lexer, (int) 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isQuoteChar", int.class);
        method.setAccessible(true);

        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        quoteCharField.setChar(lexer, '"');

        assertTrue((Boolean) method.invoke(lexer, (int) '"'));
        assertFalse((Boolean) method.invoke(lexer, (int) '\''));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isCommentStart", int.class);
        method.setAccessible(true);

        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        commentStartField.setChar(lexer, '#');

        assertTrue((Boolean) method.invoke(lexer, (int) '#'));
        assertFalse((Boolean) method.invoke(lexer, (int) '!'));
    }
}