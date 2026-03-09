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

class Lexer_1_4Test {

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

        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // Not under test here
            }
        };
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNull_returnsDisabled() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char result = (char) method.invoke(lexer, (Character) null);
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withChar_returnsSameChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char c = 'a';
        char result = (char) method.invoke(lexer, c);
        assertEquals(c, result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber_delegatesToIn() {
        when(in.getLineNumber()).thenReturn(42L);
        long lineNumber = lexer.getLineNumber();
        assertEquals(42L, lineNumber);
        verify(in).getLineNumber();
    }

    @Test
    @Timeout(8000)
    void testReadEscape_readsCharAndReturns() throws IOException {
        when(in.read()).thenReturn((int) 'x');
        int result = lexer.readEscape();
        assertEquals('x', result);
        verify(in).read();
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_removesTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        StringBuilder sb = new StringBuilder("abc   ");
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());

        StringBuilder sb2 = new StringBuilder("abc");
        method.invoke(lexer, sb2);
        assertEquals("abc", sb2.toString());

        StringBuilder sb3 = new StringBuilder("   ");
        method.invoke(lexer, sb3);
        assertEquals("", sb3.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_variousEOL() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        // Test with CR followed by LF
        when(in.read()).thenReturn((int) '\n');
        boolean result = (boolean) method.invoke(lexer, (int) '\r');
        assertTrue(result);
        verify(in).read();

        reset(in);

        // Test with CR not followed by LF
        when(in.read()).thenReturn((int) 'x');
        result = (boolean) method.invoke(lexer, (int) '\r');
        assertTrue(result);
        verify(in).read();

        reset(in);

        // Test with LF
        result = (boolean) method.invoke(lexer, (int) '\n');
        assertTrue(result);
        verify(in, never()).read();

        reset(in);

        // Test with FF
        result = (boolean) method.invoke(lexer, (int) '\f');
        assertTrue(result);
        verify(in, never()).read();

        reset(in);

        // Test with other char
        result = (boolean) method.invoke(lexer, (int) 'x');
        assertFalse(result);
        verify(in, never()).read();
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
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
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
    void testIsDelimiter() throws NoSuchFieldException, IllegalAccessException {
        Field delimiterField = Lexer.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(lexer);

        assertTrue(lexer.isDelimiter(delimiter));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() throws NoSuchFieldException, IllegalAccessException {
        Field escapeField = Lexer.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        char escape = escapeField.getChar(lexer);

        assertTrue(lexer.isEscape(escape));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() throws NoSuchFieldException, IllegalAccessException {
        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        char quoteChar = quoteCharField.getChar(lexer);

        assertTrue(lexer.isQuoteChar(quoteChar));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() throws NoSuchFieldException, IllegalAccessException {
        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        char commentStart = commentStartField.getChar(lexer);

        assertTrue(lexer.isCommentStart(commentStart));
        assertFalse(lexer.isCommentStart('a'));
    }
}