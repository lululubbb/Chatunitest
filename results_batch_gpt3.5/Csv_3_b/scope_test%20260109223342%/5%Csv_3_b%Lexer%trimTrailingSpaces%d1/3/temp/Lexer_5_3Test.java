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

import java.lang.reflect.Method;

class LexerTrimTrailingSpacesTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        // Create an anonymous subclass since Lexer is abstract
        lexer = new Lexer(null, null) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_AllWhitespace() throws Exception {
        StringBuilder buffer = new StringBuilder("   \t\n\r\f");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals(0, buffer.length());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_MixedContentTrailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("abc \t\n");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("abc", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_NoTrailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("abc");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("abc", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_EmptyBuffer() throws Exception {
        StringBuilder buffer = new StringBuilder();
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals(0, buffer.length());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_TrailingNonWhitespace() throws Exception {
        StringBuilder buffer = new StringBuilder("a b c");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("a b c", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_OnlyOneTrailingSpace() throws Exception {
        StringBuilder buffer = new StringBuilder("abc ");
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);

        method.invoke(lexer, buffer);

        assertEquals("abc", buffer.toString());
    }
}