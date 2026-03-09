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
import org.mockito.Mockito;

import java.lang.reflect.Method;

class Lexer_5_6Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);
        // Create anonymous subclass since Lexer is abstract
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_noTrailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("abc");
        invokeTrimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_allSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("   ");
        invokeTrimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_trailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("abc   ");
        invokeTrimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_emptyString() throws Exception {
        StringBuilder buffer = new StringBuilder("");
        invokeTrimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_mixedWhitespace() throws Exception {
        StringBuilder buffer = new StringBuilder("abc \t\n\r");
        invokeTrimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    private void invokeTrimTrailingSpaces(StringBuilder buffer) throws Exception {
        Method method = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        method.setAccessible(true);
        method.invoke(lexer, buffer);
    }
}