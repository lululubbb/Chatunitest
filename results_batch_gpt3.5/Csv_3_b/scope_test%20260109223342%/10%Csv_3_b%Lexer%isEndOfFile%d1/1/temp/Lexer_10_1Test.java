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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

class Lexer_10_1Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        // Create a concrete subclass of Lexer since Lexer is abstract
        lexer = new Lexer(Mockito.mock(CSVFormat.class), Mockito.mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_whenEndOfStream() throws Exception {
        Method isEndOfFileMethod = Lexer.class.getDeclaredMethod("isEndOfFile", int.class);
        isEndOfFileMethod.setAccessible(true);

        boolean result = (boolean) isEndOfFileMethod.invoke(lexer, END_OF_STREAM);
        assertTrue(result, "Should return true when input is END_OF_STREAM");
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_whenNotEndOfStream() throws Exception {
        Method isEndOfFileMethod = Lexer.class.getDeclaredMethod("isEndOfFile", int.class);
        isEndOfFileMethod.setAccessible(true);

        int notEndOfStream = 'A'; // any char other than END_OF_STREAM
        boolean result = (boolean) isEndOfFileMethod.invoke(lexer, notEndOfStream);
        assertFalse(result, "Should return false when input is not END_OF_STREAM");
    }
}