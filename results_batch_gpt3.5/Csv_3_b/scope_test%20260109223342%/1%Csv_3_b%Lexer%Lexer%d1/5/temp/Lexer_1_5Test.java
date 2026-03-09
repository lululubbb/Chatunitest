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
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_1_5Test {

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

        // Create an anonymous subclass of Lexer to instantiate abstract class
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // Not needed for current tests
            }
        };
    }

    @Test
    @Timeout(8000)
    void testConstructorInitializesFields() {
        assertEquals(',', getFieldChar(lexer, "delimiter"));
        assertEquals('\\', getFieldChar(lexer, "escape"));
        assertEquals('"', getFieldChar(lexer, "quoteChar"));
        assertEquals('#', getFieldChar(lexer, "commmentStart"));
        assertTrue(lexer.ignoreSurroundingSpaces);
        assertTrue(lexer.ignoreEmptyLines);
        assertSame(format, lexer.format);
        assertSame(in, lexer.in);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNonNull() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char result = (char) method.invoke(lexer, 'x');
        assertEquals('x', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNull() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        char result = (char) method.invoke(lexer, (Object) null);
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumberDelegates() {
        when(in.getLineNumber()).thenReturn(42L);
        assertEquals(42L, lexer.getLineNumber());
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsCorrectly() throws IOException {
        when(in.read()).thenReturn((int) 'a');
        assertEquals('a', lexer.readEscape());

        when(in.read()).thenReturn(-1);
        assertEquals(-1, lexer.readEscape());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpacesRemovesTrailingSpaces() throws Exception {
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
    void testReadEndOfLineHandlesVariousCases() throws IOException, Exception {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        // Simulate LF
        when(in.read()).thenReturn((int) LF);
        assertTrue((boolean) method.invoke(lexer, (int) LF));

        // Simulate CR followed by LF
        when(in.read()).thenReturn((int) LF);
        assertTrue((boolean) method.invoke(lexer, (int) CR));

        // Simulate CR followed by non-LF
        when(in.read()).thenReturn((int) 'x');
        assertTrue((boolean) method.invoke(lexer, (int) CR));
        verify(in, times(1)).unread((int) 'x');

        // Simulate FF
        when(in.read()).thenReturn((int) LF);
        assertTrue((boolean) method.invoke(lexer, (int) FF));

        // Simulate other char
        assertFalse((boolean) method.invoke(lexer, (int) 'x'));
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace(TAB));
        assertTrue(lexer.isWhitespace(LF));
        assertTrue(lexer.isWhitespace(CR));
        assertTrue(lexer.isWhitespace(FF));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(LF));
        assertTrue(lexer.isStartOfLine(CR));
        assertTrue(lexer.isStartOfLine(FF));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter() {
        assertTrue(lexer.isDelimiter(getFieldChar(lexer, "delimiter")));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() {
        assertTrue(lexer.isEscape(getFieldChar(lexer, "escape")));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() {
        assertTrue(lexer.isQuoteChar(getFieldChar(lexer, "quoteChar")));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() {
        assertTrue(lexer.isCommentStart(getFieldChar(lexer, "commmentStart")));
        assertFalse(lexer.isCommentStart('a'));
    }

    private char getFieldChar(Lexer lexer, String fieldName) {
        try {
            Field f = Lexer.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.getChar(lexer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}