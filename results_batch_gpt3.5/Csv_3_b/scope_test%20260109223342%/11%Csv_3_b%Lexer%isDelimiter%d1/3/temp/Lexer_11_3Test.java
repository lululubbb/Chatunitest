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

class Lexer_11_3Test {

    private Lexer lexer;
    private CSVFormat formatMock;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        inMock = mock(ExtendedBufferedReader.class);
        // Create an anonymous subclass to instantiate the abstract Lexer
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // no-op for test
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsDelimiterTrue() throws Exception {
        setFinalCharField(lexer, "delimiter", 'a');
        assertTrue(lexer.isDelimiter('a'));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiterFalse() throws Exception {
        setFinalCharField(lexer, "delimiter", 'a');
        assertFalse(lexer.isDelimiter('b'));
        assertFalse(lexer.isDelimiter(-1));
        assertFalse(lexer.isDelimiter(0));
    }

    // Helper method to set private final char fields via reflection
    private void setFinalCharField(Object target, String fieldName, char value) throws Exception {
        Field field = Lexer.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.setChar(target, value);
    }
}