package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtendedBufferedReader_4_1Test {

    private ExtendedBufferedReader extendedBufferedReader;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadLengthZeroReturnsZero() throws IOException {
        char[] buf = new char[10];
        int result = extendedBufferedReader.read(buf, 0, 0);
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsMinusOneSetsLastCharToEndOfStream() throws Exception {
        char[] buf = new char[10];

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        Reader underlyingReader = getPrivateField(spyReader, "in");
        Reader spyUnderlyingReader = Mockito.spy(underlyingReader);
        setPrivateField(spyReader, "in", spyUnderlyingReader);

        doReturn(-1).when(spyUnderlyingReader).read(any(char[].class), anyInt(), anyInt());

        int result = spyReader.read(buf, 0, 10);
        assertEquals(-1, result);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    @Test
    @Timeout(8000)
    void testReadProcessesBufferWithNewlinesAndCarriageReturns() throws Exception {
        char[] inputBuf = new char[] {'a', '\n', 'b', '\r', 'c', '\n', '\r', 'd'};
        int offset = 0;
        int length = inputBuf.length;

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        Reader underlyingReader = getPrivateField(spyReader, "in");
        Reader spyUnderlyingReader = Mockito.spy(underlyingReader);
        setPrivateField(spyReader, "in", spyUnderlyingReader);

        doAnswer(invocation -> {
            char[] targetBuf = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            System.arraycopy(inputBuf, 0, targetBuf, off, length);
            return length;
        }).when(spyUnderlyingReader).read(any(char[].class), anyInt(), anyInt());

        setPrivateField(spyReader, "lastChar", ExtendedBufferedReader.UNDEFINED);
        setPrivateField(spyReader, "lineCounter", 0);

        char[] buf = new char[length];
        int result = spyReader.read(buf, offset, length);

        assertEquals(length, result);

        int lineCounter = (int) getPrivateField(spyReader, "lineCounter");
        assertEquals(4, lineCounter);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals('d', lastChar);
    }

    @Test
    @Timeout(8000)
    void testReadProcessesNewlineAfterCarriageReturnDoesNotIncrementLineCounter() throws Exception {
        char[] inputBuf = new char[] {'\r', '\n'};
        int offset = 0;
        int length = inputBuf.length;

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        Reader underlyingReader = getPrivateField(spyReader, "in");
        Reader spyUnderlyingReader = Mockito.spy(underlyingReader);
        setPrivateField(spyReader, "in", spyUnderlyingReader);

        doAnswer(invocation -> {
            char[] targetBuf = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            System.arraycopy(inputBuf, 0, targetBuf, off, length);
            return length;
        }).when(spyUnderlyingReader).read(any(char[].class), anyInt(), anyInt());

        setPrivateField(spyReader, "lastChar", ExtendedBufferedReader.UNDEFINED);
        setPrivateField(spyReader, "lineCounter", 0);

        char[] buf = new char[length];
        int result = spyReader.read(buf, offset, length);
        assertEquals(length, result);

        int lineCounter = (int) getPrivateField(spyReader, "lineCounter");
        assertEquals(1, lineCounter);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals('\n', lastChar);
    }

    /*
     * Helper methods to access private fields
     */

    private <T> T getPrivateField(Object instance, String fieldName) throws Exception {
        java.lang.reflect.Field field = null;
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        T value = (T) field.get(instance);
        return value;
    }

    private void setPrivateField(Object instance, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = null;
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        field.setAccessible(true);
        field.set(instance, value);
    }
}