package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.UNDEFINED;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LexerReadEscapeTest {

    Lexer lexer;
    ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() {
        inMock = mock(ExtendedBufferedReader.class);
        // Creating anonymous subclass because Lexer is abstract
        lexer = new Lexer(CSVFormat.DEFAULT, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null;
            }
        };
    }

    private int invokeReadEscape() throws Exception {
        Method method = Lexer.class.getDeclaredMethod("readEscape");
        method.setAccessible(true);
        return (int) method.invoke(lexer);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_r() throws Exception {
        when(inMock.read()).thenReturn((int) 'r');
        assertEquals(CR, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_n() throws Exception {
        when(inMock.read()).thenReturn((int) 'n');
        assertEquals(LF, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_t() throws Exception {
        when(inMock.read()).thenReturn((int) 't');
        assertEquals(TAB, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_b() throws Exception {
        when(inMock.read()).thenReturn((int) 'b');
        assertEquals(BACKSPACE, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_f() throws Exception {
        when(inMock.read()).thenReturn((int) 'f');
        assertEquals(FF, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_CR() throws Exception {
        when(inMock.read()).thenReturn((int) CR);
        assertEquals(CR, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_LF() throws Exception {
        when(inMock.read()).thenReturn((int) LF);
        assertEquals(LF, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_FF() throws Exception {
        when(inMock.read()).thenReturn((int) FF);
        assertEquals(FF, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_TAB() throws Exception {
        when(inMock.read()).thenReturn((int) TAB);
        assertEquals(TAB, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_BACKSPACE() throws Exception {
        when(inMock.read()).thenReturn((int) BACKSPACE);
        assertEquals(BACKSPACE, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_defaultChar() throws Exception {
        int c = 'x';
        when(inMock.read()).thenReturn(c);
        assertEquals(c, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_endOfStreamThrows() throws Exception {
        when(inMock.read()).thenReturn(END_OF_STREAM);
        IOException thrown = assertThrows(IOException.class, this::invokeReadEscape);
        assertEquals("EOF whilst processing escape sequence", thrown.getMessage());
    }
}