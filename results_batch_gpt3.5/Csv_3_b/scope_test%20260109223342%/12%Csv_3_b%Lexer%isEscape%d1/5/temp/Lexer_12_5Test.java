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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerIsEscapeTest {

    private Lexer lexer;
    private CSVFormat formatMock;
    private ExtendedBufferedReader readerMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(ExtendedBufferedReader.class);
        // Use anonymous subclass to instantiate abstract Lexer
        lexer = new Lexer(formatMock, readerMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsEscape_returnsTrueWhenCharEqualsEscape() throws Exception {
        // Use reflection to set private final field 'escape'
        setFinalCharField(lexer, "escape", 'e');

        assertTrue(invokeIsEscape(lexer, 'e'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape_returnsFalseWhenCharNotEqualsEscape() throws Exception {
        setFinalCharField(lexer, "escape", 'e');

        assertFalse(invokeIsEscape(lexer, 'x'));
        assertFalse(invokeIsEscape(lexer, getDisabledValue()));
        assertFalse(invokeIsEscape(lexer, -1)); // test negative int
    }

    private static void setFinalCharField(Object obj, String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.setChar(obj, value);
    }

    private static boolean invokeIsEscape(Lexer lexer, int c) throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isEscape", int.class);
        method.setAccessible(true);
        return (boolean) method.invoke(lexer, c);
    }

    private static int getDisabledValue() throws Exception {
        Field disabledField = Lexer.class.getDeclaredField("DISABLED");
        disabledField.setAccessible(true);
        return (int) disabledField.getChar(null);
    }
}