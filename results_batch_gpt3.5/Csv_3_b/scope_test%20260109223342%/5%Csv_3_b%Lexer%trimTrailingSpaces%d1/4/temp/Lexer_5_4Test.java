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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class LexerTrimTrailingSpacesTest {

    private Lexer lexer;
    private Method trimTrailingSpacesMethod;

    @BeforeEach
    void setUp() throws Exception {
        // Create a concrete subclass of Lexer for testing since Lexer is abstract
        lexer = new Lexer(Mockito.mock(CSVFormat.class), Mockito.mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Access package-private method trimTrailingSpaces via reflection
        trimTrailingSpacesMethod = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        trimTrailingSpacesMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_NoTrailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("NoTrailingSpaces");
        StringBuilder original = new StringBuilder(buffer.toString());

        trimTrailingSpacesMethod.invoke(lexer, buffer);

        assertEquals(original.toString(), buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_OnlyTrailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("Some text with spaces   \t\n\r");
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("Some text with spaces", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_AllSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder(" \t\n\r");
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_EmptyBuffer() throws Exception {
        StringBuilder buffer = new StringBuilder();
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_MixedWhitespace() throws Exception {
        StringBuilder buffer = new StringBuilder("Text with mixed whitespace \t \n ");
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("Text with mixed whitespace", buffer.toString());
    }
}