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
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_11_4Test {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        // Create a concrete subclass of Lexer for testing since Lexer is abstract
        lexer = new Lexer(mock(CSVFormat.class), mock(ExtendedBufferedReader.class)) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsDelimiterTrue() throws Exception {
        // Use reflection to set the private final field delimiter
        setFinalCharField("delimiter", 'a');
        assertTrue(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiterFalse() throws Exception {
        setFinalCharField("delimiter", 'b');
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiterWithSpecialChars() throws Exception {
        setFinalCharField("delimiter", '\ufffe'); // DISABLED char
        assertTrue(lexer.isDelimiter('\ufffe'));
        assertFalse(lexer.isDelimiter(' '));
    }

    // Helper method to set private final char fields via reflection
    private void setFinalCharField(String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if necessary
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Set the field on the actual instance (lexer)
        field.set(lexer, value);
    }
}