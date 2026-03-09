package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import java.io.IOException;

import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

class Lexer_10_3Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_withEndOfStream() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isEndOfFile", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, Integer.valueOf(END_OF_STREAM));
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_withNonEndOfStream() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isEndOfFile", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, Integer.valueOf('a'));
        assertFalse(result);
    }
}