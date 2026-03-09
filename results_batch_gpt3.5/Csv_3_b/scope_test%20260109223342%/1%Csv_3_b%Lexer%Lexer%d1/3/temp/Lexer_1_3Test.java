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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_1_3Test {

    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    // Concrete subclass of abstract Lexer for testing
    private static class TestLexer extends Lexer {
        TestLexer(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        Token nextToken(Token reusableToken) {
            return null; // Not tested here
        }
    }

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
        lexer = new TestLexer(format, in);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        // Pass null as single argument
        Character arg = null;
        char result = (char) method.invoke(lexer, arg);
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char result = (char) method.invoke(lexer, Character.valueOf('x'));
        assertEquals('x', result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() throws IOException {
        when(in.getLineNumber()).thenReturn(42L);
        assertEquals(42L, lexer.getLineNumber());
        verify(in).getLineNumber();
    }

    @Test
    @Timeout(8000)
    void testReadEscape() throws IOException {
        when(in.read()).thenReturn((int) 'a');
        int result = lexer.readEscape();
        assertEquals('a', result);
        verify(in).read();
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_removesSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        StringBuilder sb = new StringBuilder("abc   ");
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_noSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        StringBuilder sb = new StringBuilder("abc");
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_trueCases() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        // Case c == CR (carriage return)
        when(in.read()).thenReturn((int) '\n');
        boolean resultCR = (boolean) method.invoke(lexer, (int) '\r');
        assertTrue(resultCR);
        verify(in).read();

        reset(in);

        // Case c == LF (line feed)
        boolean resultLF = (boolean) method.invoke(lexer, (int) '\n');
        assertTrue(resultLF);

        // Case c == FF (form feed)
        boolean resultFF = (boolean) method.invoke(lexer, (int) '\f');
        assertTrue(resultFF);
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine_falseCase() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(lexer, (int) 'a');
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\n'));
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
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(';'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() {
        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() {
        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() {
        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('a'));
    }
}