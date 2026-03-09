package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Lexer_3_2Test {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() throws Exception {
        inMock = mock(ExtendedBufferedReader.class);
        CSVFormat formatMock = mock(CSVFormat.class);
        lexer = new Lexer(formatMock, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };

        // Use reflection to set the private final field 'in' to the mocked inMock
        Field inField = Lexer.class.getDeclaredField("in");
        inField.setAccessible(true);
        inField.set(lexer, inMock);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() throws IOException {
        when(inMock.getLineNumber()).thenReturn(42L);
        long lineNumber = lexer.getLineNumber();
        assertEquals(42L, lineNumber);
    }
}