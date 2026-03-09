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

class LexerIsEscapeTest {

    private Lexer lexer;
    private CSVFormat formatMock;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        inMock = mock(ExtendedBufferedReader.class);

        // Create a concrete subclass of Lexer for testing since Lexer is abstract
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsEscape_ReturnsTrue_WhenCharEqualsEscape() throws Exception {
        // Use reflection to set private final field 'escape'
        setFinalCharField(lexer, "escape", 'e');

        // Call isEscape with character equal to escape
        boolean result = lexer.isEscape('e');

        assertTrue(result, "isEscape should return true when input equals escape char");
    }

    @Test
    @Timeout(8000)
    void testIsEscape_ReturnsFalse_WhenCharNotEqualsEscape() throws Exception {
        setFinalCharField(lexer, "escape", 'e');

        boolean result = lexer.isEscape('x');

        assertFalse(result, "isEscape should return false when input not equals escape char");
    }

    // Helper method to set private final char fields via reflection
    private void setFinalCharField(Object target, String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier using reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.setChar(target, value);
    }
}