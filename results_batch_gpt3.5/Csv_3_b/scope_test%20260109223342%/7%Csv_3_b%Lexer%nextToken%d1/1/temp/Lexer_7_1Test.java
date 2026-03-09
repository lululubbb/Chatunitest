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

class Lexer_7_1Test {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        inMock = mock(ExtendedBufferedReader.class);
        formatMock = mock(CSVFormat.class);

        // Creating an anonymous subclass of Lexer since Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                // Provide a dummy implementation for abstract method
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testNextToken_nullReusableToken() throws IOException {
        // Mock input reader to simulate input stream with some characters
        when(inMock.read()).thenReturn((int) 'a', (int) Constants.END_OF_STREAM);

        // Since nextToken is abstract, we cannot invoke it directly on Lexer instance,
        // so we create a subclass with concrete nextToken for this test.
        Lexer concreteLexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                int c = inMock.read();
                if (c == Constants.END_OF_STREAM) {
                    return null;
                }

                // Create a Token instance via reflection to call the package-private constructor
                try {
                    Class<?> tokenClass = Class.forName("org.apache.commons.csv.Token");
                    // Find a constructor with String argument, package-private
                    java.lang.reflect.Constructor<?> ctor = tokenClass.getDeclaredConstructor(String.class);
                    ctor.setAccessible(true);
                    return (Token) ctor.newInstance(String.valueOf((char) c));
                } catch (Exception e) {
                    // Fallback: return null to avoid compilation error
                    return null;
                }
            }
        };

        Token token = concreteLexer.nextToken(null);
        assertNotNull(token);
        // Use reflection to call getValue or getRawChars if available
        String value = null;
        try {
            Method getValueMethod = token.getClass().getMethod("getValue");
            value = (String) getValueMethod.invoke(token);
        } catch (Exception e) {
            // fallback: try getRawChars() or toString()
            try {
                Method getRawCharsMethod = token.getClass().getMethod("getRawChars");
                char[] chars = (char[]) getRawCharsMethod.invoke(token);
                value = new String(chars);
            } catch (Exception ex) {
                value = token.toString();
            }
        }
        assertEquals("a", value);

        Token token2 = concreteLexer.nextToken(null);
        assertNull(token2);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method mapNullToDisabled = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        mapNullToDisabled.setAccessible(true);

        Character inputNull = null;
        char resultNull = (char) mapNullToDisabled.invoke(lexer, inputNull);
        assertEquals('\ufffe', resultNull);

        Character inputChar = 'x';
        char resultChar = (char) mapNullToDisabled.invoke(lexer, inputChar);
        assertEquals('x', resultChar);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() {
        // Assume initial line number is 0
        long lineNumber = lexer.getLineNumber();
        assertEquals(0L, lineNumber);
    }

    @Test
    @Timeout(8000)
    void testReadEscape() throws IOException {
        when(inMock.read()).thenReturn((int) 'n');
        int escapeChar = lexer.readEscape();
        assertEquals('n', escapeChar);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method trimTrailingSpaces = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        trimTrailingSpaces.setAccessible(true);

        StringBuilder sb = new StringBuilder("abc   ");
        trimTrailingSpaces.invoke(lexer, sb);
        assertEquals("abc", sb.toString());

        StringBuilder sbNoSpaces = new StringBuilder("abc");
        trimTrailingSpaces.invoke(lexer, sbNoSpaces);
        assertEquals("abc", sbNoSpaces.toString());

        StringBuilder sbAllSpaces = new StringBuilder("   ");
        trimTrailingSpaces.invoke(lexer, sbAllSpaces);
        assertEquals("", sbAllSpaces.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine() throws IOException {
        // Test with LF
        assertTrue(lexer.readEndOfLine(Constants.LF));
        // Test with CR followed by LF
        when(inMock.read()).thenReturn((int) Constants.LF);
        assertTrue(lexer.readEndOfLine(Constants.CR));
        // Test with CR not followed by LF
        when(inMock.read()).thenReturn((int) 'x');
        assertTrue(lexer.readEndOfLine(Constants.CR));
        // Test with non EOL char
        assertFalse(lexer.readEndOfLine('a'));
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace(Constants.TAB));
        assertTrue(lexer.isWhitespace(Constants.BACKSPACE));
        assertTrue(lexer.isWhitespace(Constants.FF));
        assertTrue(lexer.isWhitespace(Constants.CR));
        assertTrue(lexer.isWhitespace(Constants.LF));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(Constants.CR));
        assertTrue(lexer.isStartOfLine(Constants.LF));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter() {
        char delimiter;
        try {
            Field field = Lexer.class.getDeclaredField("delimiter");
            field.setAccessible(true);
            delimiter = field.getChar(lexer);
        } catch (Exception e) {
            delimiter = ',';
        }
        assertTrue(lexer.isDelimiter(delimiter));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() {
        char escape;
        try {
            Field field = Lexer.class.getDeclaredField("escape");
            field.setAccessible(true);
            escape = field.getChar(lexer);
        } catch (Exception e) {
            escape = '\\';
        }
        assertTrue(lexer.isEscape(escape));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() {
        char quoteChar;
        try {
            Field field = Lexer.class.getDeclaredField("quoteChar");
            field.setAccessible(true);
            quoteChar = field.getChar(lexer);
        } catch (Exception e) {
            quoteChar = '"';
        }
        assertTrue(lexer.isQuoteChar(quoteChar));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() {
        char commentStart;
        try {
            Field field = Lexer.class.getDeclaredField("commmentStart");
            field.setAccessible(true);
            commentStart = field.getChar(lexer);
        } catch (Exception e) {
            commentStart = '#';
        }
        assertTrue(lexer.isCommentStart(commentStart));
        assertFalse(lexer.isCommentStart('a'));
    }
}