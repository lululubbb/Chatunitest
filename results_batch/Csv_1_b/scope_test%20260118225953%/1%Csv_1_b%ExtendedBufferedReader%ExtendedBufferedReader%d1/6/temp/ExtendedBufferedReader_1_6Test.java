package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_1_6Test {

    private ExtendedBufferedReader extendedBufferedReader;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testRead_EndOfStream() throws IOException {
        when(mockReader.read()).thenReturn(-1);
        int result = extendedBufferedReader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    @Test
    @Timeout(8000)
    void testRead_NormalChar() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a');
        int result = extendedBufferedReader.read();
        assertEquals('a', result);
    }

    @Test
    @Timeout(8000)
    void testReadAgain() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);

        when(mockReader.read()).thenReturn((int) 'b');
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals('b', result);
    }

    @Test
    @Timeout(8000)
    void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        when(mockReader.read(buf, 0, 5)).thenAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            b[0] = 'h';
            b[1] = 'e';
            b[2] = 'l';
            b[3] = 'l';
            b[4] = 'o';
            return 5;
        });
        int readCount = extendedBufferedReader.read(buf, 0, 5);
        assertEquals(5, readCount);
        assertArrayEquals(new char[] {'h','e','l','l','o'}, buf);
    }

    @Test
    @Timeout(8000)
    void testReadLine() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a', (int) 'b', (int) '\n', -1);
        String line = extendedBufferedReader.readLine();
        assertEquals("ab", line);
    }

    @Test
    @Timeout(8000)
    void testLookAhead() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method lookAheadMethod = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAheadMethod.setAccessible(true);

        when(mockReader.read()).thenReturn((int) 'c');
        int result = (int) lookAheadMethod.invoke(extendedBufferedReader);
        assertEquals('c', result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getLineNumberMethod = ExtendedBufferedReader.class.getDeclaredMethod("getLineNumber");
        getLineNumberMethod.setAccessible(true);

        int lineNumber = (int) getLineNumberMethod.invoke(extendedBufferedReader);
        assertEquals(0, lineNumber);
    }
}