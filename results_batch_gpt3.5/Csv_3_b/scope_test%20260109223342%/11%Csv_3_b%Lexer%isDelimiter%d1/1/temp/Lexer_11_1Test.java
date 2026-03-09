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

class Lexer_11_1Test {

    private Lexer lexer;
    private CSVFormat formatMock;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        inMock = mock(ExtendedBufferedReader.class);

        // Create an anonymous subclass of Lexer because Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // Not needed for this test
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_returnsTrue_whenCharEqualsDelimiter() throws Exception {
        setFinalCharField(lexer, "delimiter", 'a');

        assertTrue(invokeIsDelimiter(lexer, 'a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_returnsFalse_whenCharNotEqualsDelimiter() throws Exception {
        setFinalCharField(lexer, "delimiter", 'a');

        assertFalse(invokeIsDelimiter(lexer, 'b'));
        assertFalse(invokeIsDelimiter(lexer, '\0'));
        assertFalse(invokeIsDelimiter(lexer, ' '));
        assertFalse(invokeIsDelimiter(lexer, 0));
        assertFalse(invokeIsDelimiter(lexer, 65535)); // char max
    }

    private boolean invokeIsDelimiter(Lexer lexer, int c) throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isDelimiter", int.class);
        method.setAccessible(true);
        return (boolean) method.invoke(lexer, c);
    }

    private void setFinalCharField(Object target, String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}