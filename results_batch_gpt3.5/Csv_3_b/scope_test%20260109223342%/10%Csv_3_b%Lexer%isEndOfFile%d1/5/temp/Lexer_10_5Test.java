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
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Lexer_10_5Test {

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
    void testIsEndOfFileReturnsTrueForEndOfStream() throws Exception {
        // Access constant END_OF_STREAM via reflection from Constants class
        Class<?> constantsClass = Class.forName("org.apache.commons.csv.Constants");
        Field endOfStreamField = constantsClass.getDeclaredField("END_OF_STREAM");
        endOfStreamField.setAccessible(true);
        int END_OF_STREAM = (int) endOfStreamField.get(null);

        Method isEndOfFileMethod = Lexer.class.getDeclaredMethod("isEndOfFile", int.class);
        isEndOfFileMethod.setAccessible(true);

        boolean result = (boolean) isEndOfFileMethod.invoke(lexer, END_OF_STREAM);
        assertTrue(result, "isEndOfFile should return true for END_OF_STREAM");
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFileReturnsFalseForNonEndOfStream() throws Exception {
        // Access constant END_OF_STREAM via reflection from Constants class
        Class<?> constantsClass = Class.forName("org.apache.commons.csv.Constants");
        Field endOfStreamField = constantsClass.getDeclaredField("END_OF_STREAM");
        endOfStreamField.setAccessible(true);
        int END_OF_STREAM = (int) endOfStreamField.get(null);

        Method isEndOfFileMethod = Lexer.class.getDeclaredMethod("isEndOfFile", int.class);
        isEndOfFileMethod.setAccessible(true);

        int testChar = END_OF_STREAM == Integer.MIN_VALUE ? Integer.MAX_VALUE : END_OF_STREAM - 1;
        boolean result = (boolean) isEndOfFileMethod.invoke(lexer, testChar);
        assertFalse(result, "isEndOfFile should return false for any char other than END_OF_STREAM");
    }
}