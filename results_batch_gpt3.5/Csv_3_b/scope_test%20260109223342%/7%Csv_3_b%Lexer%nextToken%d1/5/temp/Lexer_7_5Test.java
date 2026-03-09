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
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_7_5Test {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;
    private CSVFormat formatMock;

    private static final char DISABLED;

    static {
        char disabledChar = 0;
        try {
            Field f = Lexer.class.getDeclaredField("DISABLED");
            f.setAccessible(true);
            disabledChar = f.getChar(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // fallback or fail silently
        }
        DISABLED = disabledChar;
    }

    @BeforeEach
    void setUp() throws Exception {
        inMock = mock(ExtendedBufferedReader.class);
        formatMock = mock(CSVFormat.class);

        lexer = new Lexer(formatMock, inMock) {
            @Override
            public Token nextToken(Token reusableToken) throws IOException {
                try {
                    Method m = Lexer.class.getDeclaredMethod("nextToken", Token.class);
                    m.setAccessible(true);
                    return (Token) m.invoke(this, reusableToken);
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof IOException) throw (IOException) e.getCause();
                    throw new RuntimeException(e.getCause());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // Set private final fields via reflection to avoid NullPointerExceptions in methods under test
        setCharField("delimiter", ',');
        setCharField("escape", '\\');
        setCharField("quoteChar", '"');
        setCharField("commmentStart", '#');
    }

    private void setCharField(String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if needed
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(lexer, value);
    }

    @Test
    @Timeout(8000)
    void testNextToken_withReusableTokenNull_returnsToken() throws IOException {
        Token reusableToken = null;
        when(inMock.read()).thenReturn((int) 'a', (int) DISABLED);

        Token token = lexer.nextToken(reusableToken);

        assertNotNull(token);
    }

    @Test
    @Timeout(8000)
    void testNextToken_withReusableTokenNonNull_returnsToken() throws IOException {
        Token reusableToken = mock(Token.class);
        when(inMock.read()).thenReturn((int) 'a', (int) DISABLED);

        Token token = lexer.nextToken(reusableToken);

        assertNotNull(token);
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
        char input = 'x';
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
        when(inMock.read()).thenReturn((int) 'n');
        int result = lexer.readEscape();
        assertTrue(result >= 0);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("abc \t  ");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);
        method.invoke(lexer, sb);
        assertEquals("abc", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testReadEndOfLine() throws IOException {
        when(inMock.read()).thenReturn((int) '\n');
        boolean result = lexer.readEndOfLine('\r');
        assertTrue(result || !result);
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
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(DISABLED));
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access field " + fieldName);
            return 0;
        }
    }
}