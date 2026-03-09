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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LexerReadEscapeTest {

    private Lexer lexer;
    private ExtendedBufferedReader inMock;

    @BeforeEach
    void setUp() throws Exception {
        inMock = mock(ExtendedBufferedReader.class);
        CSVFormat format = mock(CSVFormat.class);

        // Create anonymous subclass of Lexer to instantiate abstract class
        lexer = new Lexer(format, inMock) {
            @Override
            Token nextToken(Token reusableToken) {
                return null; // not used in this test
            }
        };

        // Inject the mocked ExtendedBufferedReader into the lexer instance via reflection
        Field inField = Lexer.class.getDeclaredField("in");
        inField.setAccessible(true);
        inField.set(lexer, inMock);
    }

    private int invokeReadEscape() throws Exception {
        Method readEscapeMethod = Lexer.class.getDeclaredMethod("readEscape");
        readEscapeMethod.setAccessible(true);
        return (int) readEscapeMethod.invoke(lexer);
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
        when(inMock.read()).thenReturn(CR);
        assertEquals(CR, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_LF() throws Exception {
        when(inMock.read()).thenReturn(LF);
        assertEquals(LF, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_FF() throws Exception {
        when(inMock.read()).thenReturn(FF);
        assertEquals(FF, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_TAB() throws Exception {
        when(inMock.read()).thenReturn(TAB);
        assertEquals(TAB, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_BACKSPACE() throws Exception {
        when(inMock.read()).thenReturn(BACKSPACE);
        assertEquals(BACKSPACE, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_defaultChar() throws Exception {
        int defaultChar = 'x';
        when(inMock.read()).thenReturn(defaultChar);
        assertEquals(defaultChar, invokeReadEscape());
    }

    @Test
    @Timeout(8000)
    void testReadEscape_endOfStreamThrows() throws Exception {
        when(inMock.read()).thenReturn(END_OF_STREAM);
        IOException thrown = assertThrows(IOException.class, this::invokeReadEscape);
        assertEquals("EOF whilst processing escape sequence", thrown.getMessage());
    }
}