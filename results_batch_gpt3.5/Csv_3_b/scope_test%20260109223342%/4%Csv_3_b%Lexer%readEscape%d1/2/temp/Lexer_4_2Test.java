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

    private Lexer lexer;
    private ExtendedBufferedReader mockedIn;

    @BeforeEach
    void setUp() {
        mockedIn = mock(ExtendedBufferedReader.class);
        CSVFormat format = mock(CSVFormat.class);

        // Create an anonymous subclass of Lexer since Lexer is abstract
        lexer = new Lexer(format, mockedIn) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // not used in this test
            }
        };
    }

    @Test
    @Timeout(8000)
    void testReadEscape_r() throws Exception {
        when(mockedIn.read()).thenReturn((int) 'r');
        int result = invokeReadEscape();
        assertEquals(CR, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_n() throws Exception {
        when(mockedIn.read()).thenReturn((int) 'n');
        int result = invokeReadEscape();
        assertEquals(LF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_t() throws Exception {
        when(mockedIn.read()).thenReturn((int) 't');
        int result = invokeReadEscape();
        assertEquals(TAB, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_b() throws Exception {
        when(mockedIn.read()).thenReturn((int) 'b');
        int result = invokeReadEscape();
        assertEquals(BACKSPACE, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_f() throws Exception {
        when(mockedIn.read()).thenReturn((int) 'f');
        int result = invokeReadEscape();
        assertEquals(FF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_CR() throws Exception {
        when(mockedIn.read()).thenReturn((int) CR);
        int result = invokeReadEscape();
        assertEquals(CR, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_LF() throws Exception {
        when(mockedIn.read()).thenReturn((int) LF);
        int result = invokeReadEscape();
        assertEquals(LF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_FF() throws Exception {
        when(mockedIn.read()).thenReturn((int) FF);
        int result = invokeReadEscape();
        assertEquals(FF, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_TAB() throws Exception {
        when(mockedIn.read()).thenReturn((int) TAB);
        int result = invokeReadEscape();
        assertEquals(TAB, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_BACKSPACE() throws Exception {
        when(mockedIn.read()).thenReturn((int) BACKSPACE);
        int result = invokeReadEscape();
        assertEquals(BACKSPACE, result);
    }

    @Test
    @Timeout(8000)
    void testReadEscape_END_OF_STREAM_throws() throws Exception {
        when(mockedIn.read()).thenReturn(END_OF_STREAM);
        IOException thrown = assertThrows(IOException.class, this::invokeReadEscape);
        assertEquals("EOF whilst processing escape sequence", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_defaultChar() throws Exception {
        int arbitraryChar = 'x';
        when(mockedIn.read()).thenReturn(arbitraryChar);
        int result = invokeReadEscape();
        assertEquals(arbitraryChar, result);
    }

    private int invokeReadEscape() throws Exception {
        Method readEscapeMethod = Lexer.class.getDeclaredMethod("readEscape");
        readEscapeMethod.setAccessible(true);
        return (int) readEscapeMethod.invoke(lexer);
    }
}