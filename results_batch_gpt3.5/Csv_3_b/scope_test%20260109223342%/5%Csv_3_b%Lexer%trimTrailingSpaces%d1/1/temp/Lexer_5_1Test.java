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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LexerTrimTrailingSpacesTest {

    private Lexer lexer;
    private Method trimTrailingSpacesMethod;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);

        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        trimTrailingSpacesMethod = Lexer.class.getDeclaredMethod("trimTrailingSpaces", StringBuilder.class);
        trimTrailingSpacesMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_noTrailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("NoTrailingSpaces");
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("NoTrailingSpaces", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_allSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("     ");
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_trailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("TrailingSpaces   \t\n");
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("TrailingSpaces", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_emptyBuffer() throws Exception {
        StringBuilder buffer = new StringBuilder();
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testTrimTrailingSpaces_spacesInMiddle() throws Exception {
        StringBuilder buffer = new StringBuilder("Space in middle   and end   ");
        trimTrailingSpacesMethod.invoke(lexer, buffer);
        assertEquals("Space in middle   and end", buffer.toString());
    }
}