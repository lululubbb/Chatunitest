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

class LexerIsDelimiterTest {

    private Lexer lexer;
    private CSVFormat formatMock;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        inMock = mock(ExtendedBufferedReader.class);
        // Create anonymous subclass since Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
        // Set final fields that are initialized in constructor to avoid issues
        setFinalCharField(lexer, "escape", '\0');
        setFinalCharField(lexer, "quoteChar", '\0');
        setFinalCharField(lexer, "commmentStart", '\0');
        // Set final booleans to false via reflection
        setFinalBooleanField(lexer, "ignoreSurroundingSpaces", false);
        setFinalBooleanField(lexer, "ignoreEmptyLines", false);
        // Set final 'delimiter' field to a default value to avoid constructor issues
        setFinalCharField(lexer, "delimiter", '\0');
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_MatchingDelimiter() throws Exception {
        // Use reflection to set private final field 'delimiter'
        setFinalCharField(lexer, "delimiter", ';');

        assertTrue(invokeIsDelimiter(lexer, ';'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_NonMatchingDelimiter() throws Exception {
        setFinalCharField(lexer, "delimiter", ',');

        assertFalse(invokeIsDelimiter(lexer, 'a'));
        assertFalse(invokeIsDelimiter(lexer, '\n'));
        assertFalse(invokeIsDelimiter(lexer, -1));
        assertFalse(invokeIsDelimiter(lexer, 0));
    }

    // Helper method to invoke package-private isDelimiter method via reflection
    private boolean invokeIsDelimiter(Lexer lexer, int c) throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isDelimiter", int.class);
        method.setAccessible(true);
        return (boolean) method.invoke(lexer, c);
    }

    // Helper method to set private final char field via reflection
    private void setFinalCharField(Lexer lexer, String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.setChar(lexer, value);
    }

    // Helper method to set private final boolean field via reflection
    private void setFinalBooleanField(Lexer lexer, String fieldName, boolean value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.setBoolean(lexer, value);
    }
}