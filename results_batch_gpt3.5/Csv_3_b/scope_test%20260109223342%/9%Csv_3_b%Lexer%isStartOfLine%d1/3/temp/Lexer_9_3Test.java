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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_9_3Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        CSVFormat format = mock(CSVFormat.class);
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, in) {
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
        boolean result = (boolean) method.invoke(lexer, (int) Constants.LF);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_withCR() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) Constants.CR);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_withUndefined() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) Constants.UNDEFINED);
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
}