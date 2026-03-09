package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Lexer_3_6Test {

    private ExtendedBufferedReader inMock;
    private CSVFormat formatMock;
    private Lexer lexer;

    @BeforeEach
    void setUp() {
        inMock = mock(ExtendedBufferedReader.class);
        formatMock = mock(CSVFormat.class);

        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() {
        when(inMock.getLineNumber()).thenReturn(42L);
        long lineNumber = lexer.getLineNumber();
        assertEquals(42L, lineNumber);

        when(inMock.getLineNumber()).thenReturn(0L);
        lineNumber = lexer.getLineNumber();
        assertEquals(0L, lineNumber);

        when(inMock.getLineNumber()).thenReturn(-1L);
        lineNumber = lexer.getLineNumber();
        assertEquals(-1L, lineNumber);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledUsingReflection() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        Character nullChar = null;
        char result = (char) method.invoke(lexer, nullChar);
        assertEquals('\ufffe', result);

        Character someChar = 'a';
        result = (char) method.invoke(lexer, someChar);
        assertEquals('a', result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeUsingReflection() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("readEscape");
        method.setAccessible(true);

        when(inMock.read()).thenReturn((int) 'x');

        int result = (int) method.invoke(lexer);
        assertEquals('x', result);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpacesUsingReflection() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        StringBuilder sb = new StringBuilder("abc  \t\n");
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
    void testReadEndOfLineUsingReflection() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("readEndOfLine", int.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(lexer, (int) '\r'));
        assertTrue((boolean) method.invoke(lexer, (int) '\n'));
        assertTrue((boolean) method.invoke(lexer, (int) '\f'));
        assertFalse((boolean) method.invoke(lexer, (int) '\t'));
        assertFalse((boolean) method.invoke(lexer, (int) 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsWhitespace() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isWhitespace", int.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(lexer, (int) ' '));
        assertTrue((boolean) method.invoke(lexer, (int) '\t'));
        assertFalse((boolean) method.invoke(lexer, (int) 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(lexer, (int) '\r'));
        assertTrue((boolean) method.invoke(lexer, (int) '\n'));
        assertFalse((boolean) method.invoke(lexer, (int) 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isEndOfFile", int.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(lexer, (int) -1));
        assertFalse((boolean) method.invoke(lexer, (int) 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isDelimiter", int.class);
        method.setAccessible(true);

        char delimiter = getFieldChar(lexer, "delimiter");
        assertTrue((boolean) method.invoke(lexer, (int) delimiter));
        assertFalse((boolean) method.invoke(lexer, (int) 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isEscape", int.class);
        method.setAccessible(true);

        char escape = getFieldChar(lexer, "escape");
        assertTrue((boolean) method.invoke(lexer, (int) escape));
        assertFalse((boolean) method.invoke(lexer, (int) 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isQuoteChar", int.class);
        method.setAccessible(true);

        char quoteChar = getFieldChar(lexer, "quoteChar");
        assertTrue((boolean) method.invoke(lexer, (int) quoteChar));
        assertFalse((boolean) method.invoke(lexer, (int) 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isCommentStart", int.class);
        method.setAccessible(true);

        char commentStart = getFieldChar(lexer, "commmentStart");
        assertTrue((boolean) method.invoke(lexer, (int) commentStart));
        assertFalse((boolean) method.invoke(lexer, (int) 'a'));
    }

    private char getFieldChar(Lexer lexer, String fieldName) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getChar(lexer);
    }
}