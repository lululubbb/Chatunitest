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
import java.lang.reflect.Field;

class Lexer_14_1Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() throws Exception {
        // Create anonymous subclass of abstract Lexer to instantiate
        lexer = new Lexer(Mockito.mock(CSVFormat.class), Mockito.mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
        // Use reflection to set the private final field 'commmentStart'
        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        // Set commmentStart to '#'
        commentStartField.setChar(lexer, '#');
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart_MatchingChar() {
        assertTrue(lexer.isCommentStart('#'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart_NonMatchingChar() {
        assertFalse(lexer.isCommentStart('a'));
        assertFalse(lexer.isCommentStart('\0'));
        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart('\n'));
        assertFalse(lexer.isCommentStart(-1));
    }
}