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

class Lexer_1_6Test {

    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    // Concrete subclass for abstract Lexer to instantiate
    private static class LexerImpl extends Lexer {
        LexerImpl(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        Token nextToken(Token reusableToken) {
            return null;
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
        lexer = new LexerImpl(format, in);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNonNullChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char result = (char) method.invoke(lexer, Character.valueOf('X'));
        assertEquals('X', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        // Pass null as a single argument, boxed as Character
        char result = (char) method.invoke(lexer, (Object) null);
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() throws IOException {
        when(in.getLineNumber()).thenReturn(123L);
        long lineNumber = lexer.getLineNumber();
        assertEquals(123L, lineNumber);
        verify(in).getLineNumber();
    }

    @Test
    @Timeout(8000)
    void testReadEscape() throws IOException {
        when(in.read()).thenReturn((int) 'a');
        int c = lexer.readEscape();
        assertEquals('a', c);
        verify(in).read();
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        StringBuilder sb = new StringBuilder("abc   ");
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());

        StringBuilder sbNoSpaces = new StringBuilder("abc");
        method.invoke(lexer, sbNoSpaces);
        assertEquals("abc", sbNoSpaces.toString());

        StringBuilder sbAllSpaces = new StringBuilder("   ");
        method.invoke(lexer, sbAllSpaces);
        assertEquals("", sbAllSpaces.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        // Case c = CR, next char LF
        when(in.read()).thenReturn((int) '\n');
        assertTrue((boolean) method.invoke(lexer, (int) '\r'));
        verify(in).read();

        reset(in);
        // Case c = CR, next char not LF
        when(in.read()).thenReturn((int) 'X');
        assertTrue((boolean) method.invoke(lexer, (int) '\r'));
        verify(in).read();

        reset(in);
        // Case c = LF
        assertTrue((boolean) method.invoke(lexer, (int) '\n'));
        verify(in, never()).read();

        // Case c = FF
        assertTrue((boolean) method.invoke(lexer, (int) '\f'));
        verify(in, never()).read();

        // Case c = BACKSPACE (not EOL)
        assertFalse((boolean) method.invoke(lexer, (int) '\b'));

        // Case c = UNDEFINED (not EOL)
        // Access private static final char DISABLED via reflection
        char disabledChar;
        try {
            Field field = Lexer.class.getDeclaredField("DISABLED");
            field.setAccessible(true);
            disabledChar = field.getChar(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // fallback to the known value if reflection fails
            disabledChar = '\ufffe';
        }
        assertFalse((boolean) method.invoke(lexer, (int) disabledChar));
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
        assertFalse(lexer.isWhitespace(','));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine('\f'));
        assertFalse(lexer.isStartOfLine('a'));
        assertFalse(lexer.isStartOfLine(','));
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