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

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LexerIsStartOfLineTest {

    private Lexer lexer;
    private Method isStartOfLineMethod;

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
        isStartOfLineMethod = Lexer.class.getDeclaredMethod("isStartOfLine", int.class);
        isStartOfLineMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_LF_ReturnsTrue() throws Exception {
        assertTrue((Boolean) isStartOfLineMethod.invoke(lexer, (int) LF));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_CR_ReturnsTrue() throws Exception {
        assertTrue((Boolean) isStartOfLineMethod.invoke(lexer, (int) CR));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_UNDEFINED_ReturnsTrue() throws Exception {
        assertTrue((Boolean) isStartOfLineMethod.invoke(lexer, UNDEFINED));
    }

    @Test
    @Timeout(8000)
    void testIsStartOfLine_OtherChar_ReturnsFalse() throws Exception {
        assertFalse((Boolean) isStartOfLineMethod.invoke(lexer, (int) 'a'));
        assertFalse((Boolean) isStartOfLineMethod.invoke(lexer, 0));
        assertFalse((Boolean) isStartOfLineMethod.invoke(lexer, 32)); // space
        assertFalse((Boolean) isStartOfLineMethod.invoke(lexer, 255));
    }
}