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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LexerMapNullToDisabledTest {

    private Lexer lexer;
    private Method mapNullToDisabledMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);

        lexer = new Lexer(format, in) {
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
        // Since the method takes primitive char, we cannot pass null directly.
        // Instead, invoke with Character.MIN_VALUE or '\0' and test behavior accordingly.
        // But original method expects Character object, so we must call with char param.
        // To test null, we need to call the private method via reflection differently:
        // So we call with '\0' and check if it returns '\0', 
        // but the original method only maps null Character to DISABLED.
        // To test null, we must invoke with null boxed as Character, but method param is char.
        // So this test cannot be done as is.
        // Instead, we invoke the private method via reflection with null via Method.invoke(lexer, (Character) null)
        // But method param is primitive char, so it will throw IllegalArgumentException.
        // So we must change the method param to Character in reflection.

        // Fix: getDeclaredMethod("mapNullToDisabled", Character.class)
        // And invoke with (Character) null

        // So fix setUp method accordingly.

        // But here, since method param is primitive char, we cannot pass null.
        // So the original code's method signature is:
        // private final char mapNullToDisabled(final Character c)
        // So param is Character (wrapper), not char primitive.

        // So fix setUp method to getDeclaredMethod("mapNullToDisabled", Character.class)

        // Then invoke with (Character) null.

        Object result = mapNullToDisabledMethod.invoke(lexer, (Character) null);
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

        input = '\n';
        result = mapNullToDisabledMethod.invoke(lexer, input);
        c = (char) result;
        assertEquals('\n', c);

        input = '\0';
        result = mapNullToDisabledMethod.invoke(lexer, input);
        c = (char) result;
        assertEquals('\0', c);
    }
}