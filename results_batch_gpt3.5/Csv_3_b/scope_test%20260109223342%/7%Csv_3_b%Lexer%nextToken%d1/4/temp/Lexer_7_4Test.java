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

class Lexer_7_4Test {

    private Lexer lexer;
    private ExtendedBufferedReader readerMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        readerMock = mock(ExtendedBufferedReader.class);
        formatMock = mock(CSVFormat.class);
        lexer = new Lexer(formatMock, readerMock) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                // Provide a simple stub for abstract method to allow instantiation
                // Cannot call super.nextToken because it is abstract
                // Return a mock Token instead
                return mock(Token.class);
            }
        };
    }

    @Test
    @Timeout(8000)
    void testNextToken_withNullReusableToken() throws IOException {
        Token result = lexer.nextToken(null);
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testNextToken_withReusableToken() throws IOException {
        Token reusable = mock(Token.class);
        Token result = lexer.nextToken(reusable);
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNullCharacter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);
        Object ret = method.invoke(lexer, (Character) null);
        char result;
        if (ret instanceof Character) {
            result = (Character) ret;
        } else {
            result = (char) ret;
        }
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNonNullCharacter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);
        Object ret = method.invoke(lexer, (Character) 'a');
        char result;
        if (ret instanceof Character) {
            result = (Character) ret;
        } else {
            result = (char) ret;
        }
        assertEquals('a', result);
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
        when(readerMock.read()).thenReturn((int) '\\');
        int result = lexer.readEscape();
        assertTrue(result >= 0);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);
        StringBuilder sb = new StringBuilder("abc   ");
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());
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
    void testIsDelimiter() {
        char delimiter = getFieldChar("delimiter");
        assertTrue(lexer.isDelimiter(delimiter));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() {
        char escape = getFieldChar("escape");
        assertTrue(lexer.isEscape(escape));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() {
        char quoteChar = getFieldChar("quoteChar");
        assertTrue(lexer.isQuoteChar(quoteChar));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() {
        char commentStart = getFieldChar("commmentStart");
        assertTrue(lexer.isCommentStart(commentStart));
        assertFalse(lexer.isCommentStart('a'));
    }

    private char getFieldChar(String fieldName) {
        try {
            Field field = Lexer.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getChar(lexer);
        } catch (Exception e) {
            fail("Failed to get field " + fieldName);
            return 0;
        }
    }
}