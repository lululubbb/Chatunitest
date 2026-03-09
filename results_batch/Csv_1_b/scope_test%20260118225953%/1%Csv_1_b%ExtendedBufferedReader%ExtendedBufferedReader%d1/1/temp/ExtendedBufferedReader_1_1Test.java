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

class ExtendedBufferedReader_1_1Test {

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
    void testReadDelegatesToReader() throws IOException {
        when(mockReader.read()).thenReturn(42);

        int result = extendedBufferedReader.read();

        assertEquals(42, result);
        verify(mockReader).read();
    }

    @Test
    @Timeout(8000)
    void testReadCharArray() throws IOException {
        char[] buf = new char[10];
        doAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            String s = "hello";
            int toCopy = Math.min(len, s.length());
            s.getChars(0, toCopy, b, off);
            return toCopy;
        }).when(mockReader).read(any(char[].class), anyInt(), anyInt());

        int readCount = extendedBufferedReader.read(buf, 0, 5);

        assertEquals(5, readCount);
        assertArrayEquals("hello".toCharArray(), java.util.Arrays.copyOfRange(buf, 0, 5));
        verify(mockReader).read(buf, 0, 5);
    }

    @Test
    @Timeout(8000)
    void testReadLineDelegatesToSuper() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a', (int) '\n', -1);

        String line = extendedBufferedReader.readLine();

        assertEquals("a", line);
    }

    @Test
    @Timeout(8000)
    void testLookAheadMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method lookAhead = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAhead.setAccessible(true);

        when(mockReader.read()).thenReturn(65); // 'A'

        int result = (int) lookAhead.invoke(extendedBufferedReader);

        assertEquals(65, result);
    }

    @Test
    @Timeout(8000)
    void testReadAgainMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method readAgain = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgain.setAccessible(true);

        when(mockReader.read()).thenReturn(66); // 'B'

        int result = (int) readAgain.invoke(extendedBufferedReader);

        assertEquals(66, result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumberMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getLineNumber = ExtendedBufferedReader.class.getDeclaredMethod("getLineNumber");
        getLineNumber.setAccessible(true);

        int lineNumber = (int) getLineNumber.invoke(extendedBufferedReader);

        assertEquals(0, lineNumber);
    }
}