package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.UNDEFINED;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LexerReadEscapeTest {

    @Mock
    private ExtendedBufferedReader inMock;

    private Lexer lexer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        // Create anonymous subclass since Lexer is abstract
        lexer = new Lexer(CSVFormat.DEFAULT, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
        // Use reflection to set the private final field 'in' to inMock
        Field inField = Lexer.class.getDeclaredField("in");
        inField.setAccessible(true);
        inField.set(lexer, inMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsCR() throws Exception {
        when(inMock.read()).thenReturn((int) 'r');
        int result = invokeReadEscape(lexer);
        assertEquals(CR, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsLF() throws Exception {
        when(inMock.read()).thenReturn((int) 'n');
        int result = invokeReadEscape(lexer);
        assertEquals(LF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsTAB() throws Exception {
        when(inMock.read()).thenReturn((int) 't');
        int result = invokeReadEscape(lexer);
        assertEquals(TAB, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsBACKSPACE() throws Exception {
        when(inMock.read()).thenReturn((int) 'b');
        int result = invokeReadEscape(lexer);
        assertEquals(BACKSPACE, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsFF() throws Exception {
        when(inMock.read()).thenReturn((int) 'f');
        int result = invokeReadEscape(lexer);
        assertEquals(FF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsCRChar() throws Exception {
        when(inMock.read()).thenReturn((int) CR);
        int result = invokeReadEscape(lexer);
        assertEquals(CR, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsLFChar() throws Exception {
        when(inMock.read()).thenReturn((int) LF);
        int result = invokeReadEscape(lexer);
        assertEquals(LF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsFFChar() throws Exception {
        when(inMock.read()).thenReturn((int) FF);
        int result = invokeReadEscape(lexer);
        assertEquals(FF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsTABChar() throws Exception {
        when(inMock.read()).thenReturn((int) TAB);
        int result = invokeReadEscape(lexer);
        assertEquals(TAB, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsBACKSPACEChar() throws Exception {
        when(inMock.read()).thenReturn((int) BACKSPACE);
        int result = invokeReadEscape(lexer);
        assertEquals(BACKSPACE, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeThrowsIOExceptionOnEOF() throws Exception {
        when(inMock.read()).thenReturn(END_OF_STREAM);
        IOException thrown = assertThrows(IOException.class, () -> invokeReadEscape(lexer));
        assertEquals("EOF whilst processing escape sequence", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsDefaultChar() throws Exception {
        int arbitraryChar = 'x';
        when(inMock.read()).thenReturn(arbitraryChar);
        int result = invokeReadEscape(lexer);
        assertEquals(arbitraryChar, result);
    }

    private int invokeReadEscape(Lexer lexer) throws Exception {
        Method readEscape = Lexer.class.getDeclaredMethod("readEscape");
        readEscape.setAccessible(true);
        return (int) readEscape.invoke(lexer);
    }
}