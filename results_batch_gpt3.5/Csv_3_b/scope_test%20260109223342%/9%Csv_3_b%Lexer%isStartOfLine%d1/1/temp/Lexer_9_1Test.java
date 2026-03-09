package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

class Lexer_9_1Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        // Create a concrete subclass of Lexer for testing since Lexer is abstract
        lexer = new Lexer(Mockito.mock(CSVFormat.class), Mockito.mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_withLF() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) LF);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_withCR() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) CR);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_withUndefined() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) UNDEFINED);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_withOtherChar() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) 'a');
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_withZero() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, 0);
        assertFalse(result);
    }
}