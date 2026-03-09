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

class Lexer_1_2Test {

    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
        in = mock(ExtendedBufferedReader.class);

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscape()).thenReturn('\\');
        when(format.getQuoteChar()).thenReturn('"');
        when(format.getCommentStart()).thenReturn('#');
        when(format.getIgnoreSurroundingSpaces()).thenReturn(true);
        when(format.getIgnoreEmptyLines()).thenReturn(true);

        // Create anonymous subclass of abstract Lexer to instantiate
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testConstructorInitializesFields() throws Exception {
        Field delimiterField = Lexer.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        assertEquals(',', delimiterField.getChar(lexer));

        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        assertEquals('\\', escapeField.getChar(lexer));

        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        assertEquals('"', quoteCharField.getChar(lexer));

        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        assertEquals('#', commentStartField.getChar(lexer));

        assertTrue(lexer.ignoreSurroundingSpaces);
        assertTrue(lexer.ignoreEmptyLines);
        assertSame(format, lexer.format);
        assertSame(in, lexer.in);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char result = (char) method.invoke(lexer, (Character) null);
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char input = 'x';
        char result = (char) method.invoke(lexer, input);
        assertEquals(input, result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() throws IOException {
        when(in.getLineNumber()).thenReturn(42L);
        long lineNumber = lexer.getLineNumber();
        assertEquals(42L, lineNumber);
    }

    @Test
    @Timeout(8000)
    void testReadEscape() throws IOException {
        when(in.read()).thenReturn((int) 'a');
        int result = lexer.readEscape();
        assertEquals('a', result);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces() throws Exception {
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

        sb = new StringBuilder("");
        method.invoke(lexer, sb);
        assertEquals("", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        when(in.read()).thenReturn((int) '\n', (int) 'x');

        // CR followed by LF
        boolean result = (boolean) method.invoke(lexer, (int) '\r');
        assertTrue(result);

        // LF alone
        result = (boolean) method.invoke(lexer, (int) '\n');
        assertTrue(result);

        // FF alone
        result = (boolean) method.invoke(lexer, (int) '\f');
        assertTrue(result);

        // Other char, returns false
        result = (boolean) method.invoke(lexer, (int) 'x');
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\n'));
        assertTrue(lexer.isWhitespace('\r'));
        assertTrue(lexer.isWhitespace('\f'));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\f'));
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
    void testIsDelimiter() throws Exception {
        Field delimiterField = Lexer.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(lexer);

        assertTrue(lexer.isDelimiter(delimiter));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() throws Exception {
        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        char escape = escapeField.getChar(lexer);

        assertTrue(lexer.isEscape(escape));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() throws Exception {
        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        char quoteChar = quoteCharField.getChar(lexer);

        assertTrue(lexer.isQuoteChar(quoteChar));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() throws Exception {
        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        char commentStart = commentStartField.getChar(lexer);

        assertTrue(lexer.isCommentStart(commentStart));
        assertFalse(lexer.isCommentStart('a'));
    }
}