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

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerIsEscapeTest {

    private Lexer lexer;
    private CSVFormat format;
    private ExtendedBufferedReader in;

    @BeforeEach
    void setUp() throws Exception {
        format = mock(CSVFormat.class);
        in = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Set the final field 'escape' once here to avoid repeated reflection in tests
        setFinalCharField(lexer, "escape", '\"');
    }

    @Test
    @Timeout(8000)
    void testIsEscape_MatchesEscapeChar() throws Exception {
        assertTrue(invokeIsEscape(lexer, '\"'));
    }

    @Test
    @Timeout(8000)
    void testIsEscape_DoesNotMatchEscapeChar() throws Exception {
        assertFalse(invokeIsEscape(lexer, 'a'));
        assertFalse(invokeIsEscape(lexer, '\n'));
        assertFalse(invokeIsEscape(lexer, 0));
        assertFalse(invokeIsEscape(lexer, -1));
    }

    private boolean invokeIsEscape(Lexer lexer, int c) throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isEscape", int.class);
        method.setAccessible(true);
        return (boolean) method.invoke(lexer, c);
    }

    private void setFinalCharField(Object obj, String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier (Java 12+ requires this hack)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.setChar(obj, value);
    }
}