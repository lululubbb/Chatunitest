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

class Lexer_13_6Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);

        // Create an anonymous subclass with nextToken overridden (since Lexer is abstract)
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Set quoteChar field via reflection
        setQuoteChar('\''); // default quote char for setup
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_MatchingChar() throws Exception {
        setQuoteChar('\'');
        Method method = Lexer.class.getDeclaredMethod("isQuoteChar", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) '\'');
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsQuoteChar_NonMatchingChar() throws Exception {
        setQuoteChar('\"');
        Method method = Lexer.class.getDeclaredMethod("isQuoteChar", int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(lexer, (int) '\'');
        assertFalse(result);
    }

    private void setQuoteChar(char quoteChar) throws Exception {
        Field field = Lexer.class.getDeclaredField("quoteChar");
        field.setAccessible(true);

        // Remove final modifier if present (works for JDK 8 and below)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(lexer, quoteChar);
    }
}