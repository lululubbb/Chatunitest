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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LexerIsStartOfLineTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() throws Exception {
        lexer = new Lexer(Mockito.mock(CSVFormat.class), Mockito.mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // not needed for this test
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_LF() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, LF);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_CR() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, CR);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_UNDEFINED() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, UNDEFINED);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_OtherChar() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) 'A');
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_EndOfStream() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, Constants.END_OF_STREAM);
        assertFalse(result);
    }

}