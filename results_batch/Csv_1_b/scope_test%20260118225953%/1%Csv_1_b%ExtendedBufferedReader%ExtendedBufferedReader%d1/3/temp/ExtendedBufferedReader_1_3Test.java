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

class ExtendedBufferedReader_1_3Test {

    private Reader mockReader;
    private ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testConstructor() {
        assertNotNull(extendedBufferedReader);
    }

    @Test
    @Timeout(8000)
    void testRead() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a').thenReturn(-1);
        int first = extendedBufferedReader.read();
        int second = extendedBufferedReader.read();
        assertEquals('a', first);
        assertEquals(-1, second);
    }

    @Test
    @Timeout(8000)
    void testReadCharArrayOffsetLength() throws IOException {
        char[] buf = new char[10];
        when(mockReader.read(buf, 2, 5)).thenReturn(5);
        int readCount = extendedBufferedReader.read(buf, 2, 5);
        assertEquals(5, readCount);
    }

    @Test
    @Timeout(8000)
    void testReadLine() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a', (int) '\n', -1);
        String line = extendedBufferedReader.readLine();
        assertEquals("a", line);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getLineNumber = ExtendedBufferedReader.class.getDeclaredMethod("getLineNumber");
        getLineNumber.setAccessible(true);
        int lineNumber = (int) getLineNumber.invoke(extendedBufferedReader);
        assertEquals(0, lineNumber);
    }

    @Test
    @Timeout(8000)
    void testLookAhead() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method lookAhead = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAhead.setAccessible(true);
        when(mockReader.read()).thenReturn((int) 'x');
        int result = (int) lookAhead.invoke(extendedBufferedReader);
        assertEquals('x', result);
    }

    @Test
    @Timeout(8000)
    void testReadAgain() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method readAgain = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgain.setAccessible(true);
        when(mockReader.read()).thenReturn((int) 'y');
        int result = (int) readAgain.invoke(extendedBufferedReader);
        assertEquals('y', result);
    }
}