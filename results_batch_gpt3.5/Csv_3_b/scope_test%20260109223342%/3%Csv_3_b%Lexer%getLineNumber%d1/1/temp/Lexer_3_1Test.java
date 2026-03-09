package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class Lexer_3_1Test {

    @Mock
    private ExtendedBufferedReader in;

    private Lexer lexer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        lexer = new Lexer(CSVFormat.DEFAULT, in) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // Not relevant for this test
            }
        };
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber_ReturnsLineNumber() throws IOException {
        long expectedLineNumber = 42L;
        when(in.getLineNumber()).thenReturn(expectedLineNumber);

        long actualLineNumber = lexer.getLineNumber();

        assertEquals(expectedLineNumber, actualLineNumber);
        verify(in).getLineNumber();
    }
}