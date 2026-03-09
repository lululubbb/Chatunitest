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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Lexer_13_3Test {

    private Lexer lexer;
    private char quoteChar;

    @BeforeEach
    void setUp() throws Exception {
        // Mock CSVFormat and ExtendedBufferedReader since Lexer constructor requires them
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);

        // Create anonymous subclass of Lexer since Lexer is abstract
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Use reflection to set the private final field quoteChar
        quoteChar = '"';
        Field quoteCharField = Lexer.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);

        // Remove final modifier from the field to allow modification
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~Modifier.FINAL);

        quoteCharField.set(lexer, quoteChar);
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_withQuoteChar() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isQuoteChar", int.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(lexer, (int) quoteChar);
        assertTrue(result, "isQuoteChar should return true when input equals quoteChar");
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_withDifferentChar() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isQuoteChar", int.class);
        method.setAccessible(true);

        int differentChar = 'a';
        if (differentChar == quoteChar) {
            differentChar = 'b'; // ensure differentChar is different
        }

        boolean result = (boolean) method.invoke(lexer, differentChar);
        assertFalse(result, "isQuoteChar should return false when input does not equal quoteChar");
    }
}