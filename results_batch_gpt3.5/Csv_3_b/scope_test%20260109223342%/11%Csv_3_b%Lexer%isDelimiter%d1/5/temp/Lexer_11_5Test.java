package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class Lexer_11_5Test {

    private Lexer lexer;

    @Mock
    private CSVFormat format;

    @Mock
    private ExtendedBufferedReader in;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        // Create anonymous subclass since Lexer is abstract
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_MatchingDelimiter() throws Exception {
        // Use reflection to set private final field 'delimiter'
        setFinalField(lexer, "delimiter", 'a');
        // Call isDelimiter with same char
        assertTrue(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_NonMatchingDelimiter() throws Exception {
        setFinalField(lexer, "delimiter", 'a');
        assertFalse(lexer.isDelimiter('b'));
        assertFalse(lexer.isDelimiter('\0'));
        assertFalse(lexer.isDelimiter(-1));
    }

    // Helper method to set private final field via reflection
    private static void setFinalField(Object target, String fieldName, char value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}