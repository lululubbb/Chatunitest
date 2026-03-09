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
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_14_6Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = mock(CSVFormat.class);
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);

        // Create an anonymous subclass since Lexer is abstract
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Use reflection to set the private final field commentStart (correct spelling with 1 'm')
        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        commentStartField.set(lexer, '#');
    }

    @Test
    @Timeout(8000)
    void testIsCommentStartMatchesCommentStartChar() {
        assertTrue(lexer.isCommentStart('#'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStartDoesNotMatchDifferentChar() {
        assertFalse(lexer.isCommentStart('a'));
        assertFalse(lexer.isCommentStart('\n'));
        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart(0));
    }
}