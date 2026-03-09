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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerIsCommentStartTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat and ExtendedBufferedReader as they are dependencies of Lexer constructor
        CSVFormat mockFormat = mock(CSVFormat.class);
        ExtendedBufferedReader mockReader = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass of Lexer since Lexer is abstract
        lexer = new Lexer(mockFormat, mockReader) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Use reflection to set the private final field commmentStart (with triple 'm')
        Field commentStartField = Lexer.class.getDeclaredField("commmentStart");
        commentStartField.setAccessible(true);
        commentStartField.setChar(lexer, '#'); // set comment start char to '#'
    }

    @Test
    @Timeout(8000)
    void testIsCommentStartReturnsTrueWhenCharMatches() throws Exception {
        // The comment start char is set to '#'
        boolean result = invokeIsCommentStart('#');
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsCommentStartReturnsFalseWhenCharDoesNotMatch() throws Exception {
        // The comment start char is set to '#'
        boolean result = invokeIsCommentStart('!');
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsCommentStartReturnsFalseForNegativeInt() throws Exception {
        // The comment start char is set to '#'
        boolean result = invokeIsCommentStart(-1);
        assertFalse(result);
    }

    private boolean invokeIsCommentStart(int c) throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isCommentStart", int.class);
        method.setAccessible(true);
        return (boolean) method.invoke(lexer, c);
    }
}