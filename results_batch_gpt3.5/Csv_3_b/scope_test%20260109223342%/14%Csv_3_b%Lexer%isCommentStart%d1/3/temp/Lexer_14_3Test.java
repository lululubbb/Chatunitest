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

class Lexer_14_3Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        // Mock CSVFormat and ExtendedBufferedReader as they are dependencies of Lexer constructor
        CSVFormat format = mock(CSVFormat.class);
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass of Lexer because Lexer is abstract
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // not needed for this test
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart_MatchingChar() throws Exception {
        // Set private field commentStart via reflection (corrected field name)
        setPrivateCharField("commmentStart", '#');
        assertTrue(lexer.isCommentStart('#'));
    }

    @Test
    @Timeout(8000)
    void testIsCommentStart_NonMatchingChar() throws Exception {
        setPrivateCharField("commmentStart", '#');
        assertFalse(lexer.isCommentStart('!'));
        assertFalse(lexer.isCommentStart(0));
        assertFalse(lexer.isCommentStart(-1));
    }

    private void setPrivateCharField(String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(lexer, value);
    }
}