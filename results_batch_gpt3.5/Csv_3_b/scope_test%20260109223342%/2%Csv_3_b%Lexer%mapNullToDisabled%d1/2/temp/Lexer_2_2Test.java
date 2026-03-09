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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LexerMapNullToDisabledTest {

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNull_returnsDisabled() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        // Act
        Object resultObj = method.invoke(lexer, (Object) null);
        char result = (Character) resultObj;

        // Assert
        assertEquals('\ufffe', result);
    }

    @Test
    @Timeout(8000)
    void testMapNullToDisabled_withNonNullCharacter_returnsCharacterValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        Method method = Lexer.class.getDeclaredMethod("mapNullToDisabled", Character.class);
        method.setAccessible(true);

        Character input = 'a';

        // Act
        Object resultObj = method.invoke(lexer, input);
        char result = (Character) resultObj;

        // Assert
        assertEquals('a', result);
    }
}