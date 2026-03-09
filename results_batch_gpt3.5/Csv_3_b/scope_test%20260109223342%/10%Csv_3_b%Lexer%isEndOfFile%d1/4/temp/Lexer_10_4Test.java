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

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class LexerIsEndOfFileTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = mock(CSVFormat.class);
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);

        // Create anonymous subclass because Lexer is abstract
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_withEndOfStream() throws Exception {
        // Use reflection to access END_OF_STREAM constant from Constants class
        Class<?> constantsClass = Class.forName("org.apache.commons.csv.Constants");
        Field field = constantsClass.getDeclaredField("END_OF_STREAM");
        field.setAccessible(true);
        int END_OF_STREAM = field.getInt(null);

        // Call isEndOfFile with END_OF_STREAM
        boolean result = lexer.isEndOfFile(END_OF_STREAM);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_withNonEndOfStream() throws Exception {
        // Use reflection to get END_OF_STREAM constant
        Class<?> constantsClass = Class.forName("org.apache.commons.csv.Constants");
        Field field = constantsClass.getDeclaredField("END_OF_STREAM");
        field.setAccessible(true);
        int END_OF_STREAM = field.getInt(null);

        // Test with a character that is not END_OF_STREAM (e.g. 0)
        int nonEofChar = END_OF_STREAM == 0 ? 1 : 0;
        boolean result = lexer.isEndOfFile(nonEofChar);
        assertFalse(result);
    }
}