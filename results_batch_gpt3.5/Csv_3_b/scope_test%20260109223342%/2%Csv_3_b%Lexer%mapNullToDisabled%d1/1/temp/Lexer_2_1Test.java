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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LexerMapNullToDisabledTest {

    private Lexer lexer;
    private Method mapNullToDisabledMethod;
    private static final char DISABLED = '\ufffe';

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Create a concrete subclass of Lexer since Lexer is abstract
        lexer = new Lexer(Mockito.mock(CSVFormat.class), Mockito.mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
        mapNullToDisabledMethod = Lexer.class.getDeclaredMethod("mapNullToDisabled", char.class);
        mapNullToDisabledMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNull() throws InvocationTargetException, IllegalAccessException {
        char result = (char) mapNullToDisabledMethod.invoke(lexer, Character.valueOf('\0'));
        assertEquals(DISABLED, result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNonNull() throws InvocationTargetException, IllegalAccessException {
        char input = 'A';
        char result = (char) mapNullToDisabledMethod.invoke(lexer, input);
        assertEquals('A', result);
    }
}