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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerMapNullToDisabledTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        // Mock CSVFormat and ExtendedBufferedReader as they are required by Lexer constructor
        CSVFormat format = mock(CSVFormat.class);
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass of Lexer since Lexer is abstract
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        // Pass null as Character parameter
        Object result = method.invoke(lexer, new Object[] { null });
        char c = (Character) result;
        assertEquals('\ufffe', c);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNonNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        Character input = 'A';
        Object result = method.invoke(lexer, input);
        char c = (Character) result;
        assertEquals('A', c);
    }
}