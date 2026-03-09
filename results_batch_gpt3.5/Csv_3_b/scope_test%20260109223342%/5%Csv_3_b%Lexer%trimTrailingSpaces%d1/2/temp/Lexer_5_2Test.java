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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LexerTrimTrailingSpacesTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        // Using anonymous subclass since Lexer is abstract
        lexer = new Lexer(null, null) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_withTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder("abc   ");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("abc", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_withNoTrailingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder("abc");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("abc", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_emptyBuffer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_allSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder("    \t\n\r");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_mixedWhitespaceAndChars() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder("abc \t\n");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("abc", buffer.toString());
    }
}