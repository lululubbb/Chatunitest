package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_1_5Test {

    private ExtendedBufferedReader extendedBufferedReader;
    private Reader mockReader;

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
        when(mockReader.read()).thenReturn((int) 'a', -1);
        int firstChar = extendedBufferedReader.read();
        assertEquals('a', firstChar);
        int secondChar = extendedBufferedReader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, secondChar);
    }

    @Test
    @Timeout(8000)
    void testReadCharArrayOffsetLength() throws IOException {
        char[] buf = new char[5];
        when(mockReader.read(any(char[].class), eq(1), eq(3))).thenAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            b[off] = 'x';
            b[off + 1] = 'y';
            b[off + 2] = 'z';
            return 3;
        });
        int readCount = extendedBufferedReader.read(buf, 1, 3);
        assertEquals(3, readCount);
        assertEquals('x', buf[1]);
        assertEquals('y', buf[2]);
        assertEquals('z', buf[3]);
    }

    @Test
    @Timeout(8000)
    void testReadLine() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a', (int) '\n', -1);
        String line = extendedBufferedReader.readLine();
        assertEquals("a", line);
        String line2 = extendedBufferedReader.readLine();
        assertNull(line2);
    }

    @Test
    @Timeout(8000)
    void testLookAhead() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(mockReader.read()).thenReturn((int) 'b');
        Method lookAheadMethod = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAheadMethod.setAccessible(true);
        int result = (int) lookAheadMethod.invoke(extendedBufferedReader);
        assertEquals('b', result);
    }

    @Test
    @Timeout(8000)
    void testReadAgain() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(mockReader.read()).thenReturn((int) 'c');
        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals('c', result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber() throws NoSuchFieldException, IllegalAccessException {
        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        lineCounterField.setInt(extendedBufferedReader, 5);
        int lineNumber = extendedBufferedReader.getLineNumber();
        assertEquals(5, lineNumber);
    }
}