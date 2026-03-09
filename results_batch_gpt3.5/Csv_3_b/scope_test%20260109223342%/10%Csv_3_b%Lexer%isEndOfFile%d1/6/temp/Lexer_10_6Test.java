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
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;

class Lexer_10_6Test {

    private Lexer lexer;

    @Mock
    private ExtendedBufferedReader mockReader;

    @Mock
    private CSVFormat mockFormat;

    private MockitoSession mockitoSession;

    @BeforeEach
    void setUp() {
        mockitoSession = org.mockito.Mockito.mockitoSession()
                .initMocks(this)
                .startMocking();
        lexer = new Lexer(mockFormat, mockReader) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoSession.finishMocking();
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_WithEndOfStream() {
        int c = Constants.END_OF_STREAM;
        assertTrue(lexer.isEndOfFile(c));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_WithNonEndOfStream() {
        int c = 'a';
        assertFalse(lexer.isEndOfFile(c));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_WithNegativeValue() {
        int c = -1;
        assertFalse(lexer.isEndOfFile(c));
    }

    @Test
    @Timeout(8000)
    void testIsEndOfFile_UsingReflection() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("isEndOfFile", int.class);
        method.setAccessible(true);

        int endOfStream = Constants.END_OF_STREAM;
        int normalChar = 'z';

        assertTrue((Boolean) method.invoke(lexer, endOfStream));
        assertFalse((Boolean) method.invoke(lexer, normalChar));
    }
}