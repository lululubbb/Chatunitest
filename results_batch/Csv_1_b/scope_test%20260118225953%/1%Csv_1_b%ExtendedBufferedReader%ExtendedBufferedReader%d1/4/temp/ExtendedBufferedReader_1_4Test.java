package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_1_4Test {

    private Reader mockReader;
    private ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testRead_whenReaderReturnsChar() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a');
        int result = extendedBufferedReader.read();
        assertEquals('a', result);
    }

    @Test
    @Timeout(8000)
    void testRead_whenReaderReturnsEndOfStream() throws IOException {
        when(mockReader.read()).thenReturn(-1);
        int result = extendedBufferedReader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    @Test
    @Timeout(8000)
    void testReadAgain_reflective() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        when(mockReader.read()).thenReturn((int) 'b').thenReturn(-1);

        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);

        int firstCall = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals('b', firstCall);

        int secondCall = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, secondCall);
    }

    @Test
    @Timeout(8000)
    void testReadBuffer() throws IOException {
        char[] buffer = new char[5];
        when(mockReader.read(buffer, 0, 5)).thenReturn(3);

        int readCount = extendedBufferedReader.read(buffer, 0, 5);
        assertEquals(3, readCount);
    }

    @Test
    @Timeout(8000)
    void testReadLine() throws IOException {
        String line = "test line";
        when(mockReader.read(any(char[].class), anyInt(), anyInt()))
            .thenAnswer(invocation -> {
                char[] buf = invocation.getArgument(0);
                int off = invocation.getArgument(1);
                String s = line + "\n";
                for (int i = 0; i < s.length(); i++) {
                    buf[off + i] = s.charAt(i);
                }
                return s.length();
            });

        String readLine = extendedBufferedReader.readLine();
        assertEquals(line, readLine);
    }

    @Test
    @Timeout(8000)
    void testLookAhead_reflective() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        when(mockReader.read()).thenReturn((int) 'c');

        Method lookAheadMethod = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAheadMethod.setAccessible(true);

        int result = (int) lookAheadMethod.invoke(extendedBufferedReader);
        assertEquals('c', result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber_reflective() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Field field = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        field.setAccessible(true);
        field.setInt(extendedBufferedReader, 42);

        Method getLineNumberMethod = ExtendedBufferedReader.class.getDeclaredMethod("getLineNumber");
        getLineNumberMethod.setAccessible(true);

        int lineNumber = (int) getLineNumberMethod.invoke(extendedBufferedReader);
        assertEquals(42, lineNumber);
    }
}