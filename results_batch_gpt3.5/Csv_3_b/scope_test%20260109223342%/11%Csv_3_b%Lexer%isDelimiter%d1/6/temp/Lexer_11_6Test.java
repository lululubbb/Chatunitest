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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerIsDelimiterTest {

    private Lexer lexer;
    private char delimiterChar = ',';

    // Concrete subclass to instantiate abstract Lexer
    private static class LexerImpl extends Lexer {
        LexerImpl(CSVFormat format, ExtendedBufferedReader in, char delimiter, char escape, char quoteChar, char commentStart,
                  boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines) {
            super(format, in);
            // Use reflection to set final fields
            try {
                setField("delimiter", delimiter);
                setField("escape", escape);
                setField("quoteChar", quoteChar);
                setField("commmentStart", commentStart);
                setField("ignoreSurroundingSpaces", ignoreSurroundingSpaces);
                setField("ignoreEmptyLines", ignoreEmptyLines);
                setField("format", format);
                setField("in", in);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void setField(String name, Object value) throws Exception {
            Field field = Lexer.class.getDeclaredField(name);
            field.setAccessible(true);
            if (field.getType() == char.class) {
                field.setChar(this, (Character) value);
            } else if (field.getType() == boolean.class) {
                field.setBoolean(this, (Boolean) value);
            } else {
                field.set(this, value);
            }
        }

        @Override
        Token nextToken(Token reusableToken) {
            return null;
        }
    }

    @BeforeEach
    void setUp() {
        CSVFormat format = mock(CSVFormat.class);
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        lexer = new LexerImpl(format, in, delimiterChar, '\\', '"', '#', true, true);
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_returnsTrueWhenCharEqualsDelimiter() {
        assertTrue(lexer.isDelimiter(delimiterChar));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_returnsFalseWhenCharDiffersFromDelimiter() {
        assertFalse(lexer.isDelimiter('a'));
        assertFalse(lexer.isDelimiter('\n'));
        assertFalse(lexer.isDelimiter(0));
        assertFalse(lexer.isDelimiter(-1));
    }

    @Test
    @Timeout(8000)
    void testIsDelimiter_withVariousDelimiterSettings() throws Exception {
        // Use reflection to change delimiter and test
        Field field = Lexer.class.getDeclaredField("delimiter");
        field.setAccessible(true);

        field.setChar(lexer, 'X');
        assertTrue(lexer.isDelimiter('X'));
        assertFalse(lexer.isDelimiter('Y'));
        assertFalse(lexer.isDelimiter(' '));

        field.setChar(lexer, '\0');
        assertTrue(lexer.isDelimiter('\0'));
        assertFalse(lexer.isDelimiter('X'));
    }
}