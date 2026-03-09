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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;

class LexerIsEndOfFileTest {

    // A minimal concrete subclass of Lexer to instantiate Lexer (since Lexer is abstract)
    private static class LexerImpl extends Lexer {
        LexerImpl(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        Token nextToken(Token reusableToken) {
            return null;
        }
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_withEndOfStream() throws Exception {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);
        Lexer lexer = new LexerImpl(format, in);

        Field endOfStreamField = Constants.class.getDeclaredField("END_OF_STREAM");
        endOfStreamField.setAccessible(true);
        int endOfStream = endOfStreamField.getInt(null);

        boolean result = lexer.isEndOfFile(endOfStream);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_withNonEndOfStream() throws Exception {
        CSVFormat format = Mockito.mock(CSVFormat.class);
        ExtendedBufferedReader in = Mockito.mock(ExtendedBufferedReader.class);
        Lexer lexer = new LexerImpl(format, in);

        Field endOfStreamField = Constants.class.getDeclaredField("END_OF_STREAM");
        endOfStreamField.setAccessible(true);
        int endOfStream = endOfStreamField.getInt(null);

        int testChar = (endOfStream == 0) ? 1 : 0;

        boolean result = lexer.isEndOfFile(testChar);
        assertFalse(result);
    }
}