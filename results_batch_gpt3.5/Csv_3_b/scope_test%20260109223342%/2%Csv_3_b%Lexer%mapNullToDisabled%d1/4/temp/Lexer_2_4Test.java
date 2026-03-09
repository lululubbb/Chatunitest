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

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_2_4Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
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

        Object resultObj = method.invoke(lexer, (Object) null);
        char result = (char) resultObj;
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabledWithNonNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        Object resultObj = method.invoke(lexer, 'a');
        char result = (char) resultObj;
        assertEquals('a', result);

        resultObj = method.invoke(lexer, '\n');
        result = (char) resultObj;
        assertEquals('\n', result);
    }
}