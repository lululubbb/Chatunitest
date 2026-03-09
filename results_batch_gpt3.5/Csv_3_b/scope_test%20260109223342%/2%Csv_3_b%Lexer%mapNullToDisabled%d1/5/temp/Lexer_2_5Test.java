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
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerMapNullToDisabledTest {

    private Lexer lexer;
    private Method mapNullToDisabledMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Create a concrete subclass of Lexer since Lexer is abstract
        lexer = new Lexer(mock(CSVFormat.class), mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        mapNullToDisabledMethod = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        mapNullToDisabledMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNull() throws InvocationTargetException, IllegalAccessException {
        Object result = mapNullToDisabledMethod.invoke(lexer, (Object) null);
        char c = (char) result;
        assertEquals('\ufffe', c);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNonNull() throws InvocationTargetException, IllegalAccessException {
        Character input = 'a';
        Object result = mapNullToDisabledMethod.invoke(lexer, input);
        char c = (char) result;
        assertEquals('a', c);
    }
}