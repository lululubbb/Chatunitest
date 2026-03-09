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

class ExtendedBufferedReader_6_1Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsChar() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        doReturn((int) 'A').when(spyReader).read();
        doNothing().when(spyReader).reset();

        Method lookAheadMethod = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAheadMethod.setAccessible(true);

        int result = (int) lookAheadMethod.invoke(spyReader);

        assertEquals('A', result);
        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsEndOfStream() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        doReturn(ExtendedBufferedReader.END_OF_STREAM).when(spyReader).read();
        doNothing().when(spyReader).reset();

        Method lookAheadMethod = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAheadMethod.setAccessible(true);

        int result = (int) lookAheadMethod.invoke(spyReader);

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOException() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doThrow(new IOException("mark failed")).when(spyReader).mark(1);

        Method lookAheadMethod = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAheadMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> lookAheadMethod.invoke(spyReader));
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mark failed", thrown.getCause().getMessage());
    }
}