package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.UNDEFINED;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LexerReadEscapeTest {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() {
        inMock = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(CSVFormat.DEFAULT, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    private int invokeReadEscape() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method readEscape = Lexer.class.getDeclaredMethod("readEscape");
        readEscape.setAccessible(true);
        return (int) readEscape.invoke(lexer);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsCR() throws Exception {
        when(inMock.read()).thenReturn((int) 'r');
        int result = invokeReadEscape();
        assertEquals(CR, result);
        verify(inMock).read();
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsLF() throws Exception {
        when(inMock.read()).thenReturn((int) 'n');
        int result = invokeReadEscape();
        assertEquals(LF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsTAB() throws Exception {
        when(inMock.read()).thenReturn((int) 't');
        int result = invokeReadEscape();
        assertEquals(TAB, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsBACKSPACE() throws Exception {
        when(inMock.read()).thenReturn((int) 'b');
        int result = invokeReadEscape();
        assertEquals(BACKSPACE, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsFF() throws Exception {
        when(inMock.read()).thenReturn((int) 'f');
        int result = invokeReadEscape();
        assertEquals(FF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsCRChar() throws Exception {
        when(inMock.read()).thenReturn((int) CR);
        int result = invokeReadEscape();
        assertEquals(CR, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsLFChar() throws Exception {
        when(inMock.read()).thenReturn((int) LF);
        int result = invokeReadEscape();
        assertEquals(LF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsFFChar() throws Exception {
        when(inMock.read()).thenReturn((int) FF);
        int result = invokeReadEscape();
        assertEquals(FF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsTABChar() throws Exception {
        when(inMock.read()).thenReturn((int) TAB);
        int result = invokeReadEscape();
        assertEquals(TAB, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsBACKSPACEChar() throws Exception {
        when(inMock.read()).thenReturn((int) BACKSPACE);
        int result = invokeReadEscape();
        assertEquals(BACKSPACE, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscapeThrowsIOExceptionOnEOF() throws Exception {
        when(inMock.read()).thenReturn(END_OF_STREAM);
        IOException exception = assertThrows(IOException.class, this::invokeReadEscape);
        assertEquals("EOF whilst processing escape sequence", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testReadEscapeReturnsDefaultChar() throws Exception {
        int arbitraryChar = 'x';
        when(inMock.read()).thenReturn(arbitraryChar);
        int result = invokeReadEscape();
        assertEquals(arbitraryChar, result);
    }
}