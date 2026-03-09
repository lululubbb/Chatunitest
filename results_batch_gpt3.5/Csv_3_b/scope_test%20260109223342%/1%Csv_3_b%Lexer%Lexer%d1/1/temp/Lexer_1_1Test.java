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

class Lexer_1_1Test {

    private CSVFormat mockFormat;
    private ExtendedBufferedReader mockReader;
    private Lexer lexer;

    private static final int UNDEFINED_INT = org.apache.commons.csv.Constants.UNDEFINED;
    private static final char CR = org.apache.commons.csv.Constants.CR;
    private static final char LF = org.apache.commons.csv.Constants.LF;
    private static final char FF = org.apache.commons.csv.Constants.FF;
    private static final char BACKSPACE = org.apache.commons.csv.Constants.BACKSPACE;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        mockFormat = mock(CSVFormat.class);
        mockReader = mock(ExtendedBufferedReader.class);

        when(mockFormat.getDelimiter()).thenReturn(',');
        when(mockFormat.getEscape()).thenReturn('\\');
        when(mockFormat.getQuoteChar()).thenReturn('"');
        when(mockFormat.getCommentStart()).thenReturn('#');
        when(mockFormat.getIgnoreSurroundingSpaces()).thenReturn(true);
        when(mockFormat.getIgnoreEmptyLines()).thenReturn(true);

        lexer = new Lexer(mockFormat, mockReader) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNonNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char result = (char) method.invoke(lexer, 'a');
        assertEquals('a', result);

        result = (char) method.invoke(lexer, ',');
        assertEquals(',', result);

        result = (char) method.invoke(lexer, '\0');
        assertEquals('\0', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char result = (char) method.invoke(lexer, new Object[] { null });
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() {
        when(mockReader.getLineNumber()).thenReturn(42L);
        assertEquals(42L, lexer.getLineNumber());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_whenEscapeChar() throws IOException {
        when(mockReader.read()).thenReturn((int) 'n');
        int result = lexer.readEscape();
        assertEquals('n', result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_whenIOException() throws IOException {
        when(mockReader.read()).thenThrow(new IOException());
        int result = lexer.readEscape();
        assertEquals(UNDEFINED_INT, result);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_removesSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        StringBuilder buffer = new StringBuilder("abc   ");
        method.invoke(lexer, buffer);
        assertEquals("abc", buffer.toString());

        buffer = new StringBuilder("abc");
        method.invoke(lexer, buffer);
        assertEquals("abc", buffer.toString());

        buffer = new StringBuilder("   ");
        method.invoke(lexer, buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        // CR
        assertTrue((boolean) method.invoke(lexer, (int) CR));
        // LF
        assertTrue((boolean) method.invoke(lexer, (int) LF));
        // FF
        assertTrue((boolean) method.invoke(lexer, (int) FF));
        // BACKSPACE (not EOL)
        assertFalse((boolean) method.invoke(lexer, (int) BACKSPACE));
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace(FF));
        assertTrue(lexer.isWhitespace(CR));
        assertTrue(lexer.isWhitespace(LF));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(CR));
        assertTrue(lexer.isStartOfLine(LF));
        assertTrue(lexer.isStartOfLine(FF));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(org.apache.commons.csv.Constants.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter() throws NoSuchFieldException, IllegalAccessException {
        Field delimiterField = Lexer.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiterValue = delimiterField.getChar(lexer);

        assertTrue(lexer.isDelimiter((int) delimiterValue));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() throws NoSuchFieldException, IllegalAccessException {
        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        char escapeValue = escapeField.getChar(lexer);

        assertTrue(lexer.isEscape((int) escapeValue));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() throws NoSuchFieldException, IllegalAccessException {
        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        char quoteCharValue = quoteCharField.getChar(lexer);

        assertTrue(lexer.isQuoteChar((int) quoteCharValue));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() throws NoSuchFieldException, IllegalAccessException {
        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        char commentStartValue = commentStartField.getChar(lexer);

        assertTrue(lexer.isCommentStart((int) commentStartValue));
        assertFalse(lexer.isCommentStart('a'));
    }
}