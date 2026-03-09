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

class LexerIsCommentStartTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = mock(CSVFormat.class);
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass since Lexer is abstract
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Use reflection to set private final field 'commmentStart' (correct spelling)
        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        commentStartField.set(lexer, '#'); // example comment start char
    }

    @Test
    @Timeout(8000)
    void testIsCommentStartWhenCharIsCommentStart() {
        assertTrue(lexer.isCommentStart('#'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStartWhenCharIsNotCommentStart() {
        assertFalse(lexer.isCommentStart('a'));
        assertFalse(lexer.isCommentStart('\n'));
        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart(0));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStartWithNegativeAndSpecialChars() {
        assertFalse(lexer.isCommentStart(-1));
        assertFalse(lexer.isCommentStart('\ufffe'));
        assertFalse(lexer.isCommentStart('\0'));
    }
}