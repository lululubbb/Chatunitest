package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtendedBufferedReader_4_5Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader) {
            // Override read(char[], int, int) to call super.read
            @Override
            public int read(char[] buf, int offset, int length) throws IOException {
                return super.read(buf, offset, length);
            }
        };
    }

    @Test
    @Timeout(8000)
    void testRead_lengthZero_returnsZero() throws IOException {
        char[] buf = new char[10];
        int result = extendedBufferedReader.read(buf, 0, 0);
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withNewLineNotPrecededByCarriageReturn() throws IOException {
        char[] buf = new char[] {'a', '\n', 'b', 'c', 'd'};
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            char[] cbuf = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, cbuf, off, len);
            // Return the length of characters copied instead of calling real method to avoid recursion
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        char[] readBuf = new char[5];
        int readLen = spyReader.read(readBuf, 0, 5);
        assertEquals(5, readLen);

        int lineNumber = (int) getPrivateField(spyReader, "lineCounter");
        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals(1, lineNumber);
        assertEquals('d', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withNewLinePrecededByCarriageReturn() throws IOException {
        char[] buf = new char[] {'\r', '\n', 'x'};
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            char[] cbuf = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, cbuf, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        char[] readBuf = new char[3];
        int readLen = spyReader.read(readBuf, 0, 3);
        assertEquals(3, readLen);

        int lineNumber = (int) getPrivateField(spyReader, "lineCounter");
        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals(1, lineNumber);
        assertEquals('x', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_newLineAtOffsetZero_lastCharNotCarriageReturn() throws IOException {
        char[] buf = new char[] {'\n', 'a'};
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        setPrivateField(spyReader, "lastChar", (int) 'z');

        doAnswer(invocation -> {
            char[] cbuf = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, cbuf, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        char[] readBuf = new char[2];
        int readLen = spyReader.read(readBuf, 0, 2);
        assertEquals(2, readLen);

        int lineNumber = (int) getPrivateField(spyReader, "lineCounter");
        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals(1, lineNumber);
        assertEquals('a', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_newLineAtOffsetZero_lastCharCarriageReturn() throws IOException {
        char[] buf = new char[] {'\n', 'a'};
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        setPrivateField(spyReader, "lastChar", (int) '\r');

        doAnswer(invocation -> {
            char[] cbuf = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, cbuf, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        char[] readBuf = new char[2];
        int readLen = spyReader.read(readBuf, 0, 2);
        assertEquals(2, readLen);

        int lineNumber = (int) getPrivateField(spyReader, "lineCounter");
        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals(0, lineNumber);
        assertEquals('a', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenNegativeOne_setsLastCharToEndOfStream() throws IOException {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doReturn(-1).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        int readLen = spyReader.read(new char[3], 0, 3);
        assertEquals(-1, readLen);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    // Helper to get private field value via reflection
    private Object getPrivateField(Object instance, String fieldName) {
        try {
            Field field = getDeclaredFieldIncludingSuperclasses(instance.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to set private field value via reflection
    private void setPrivateField(Object instance, String fieldName, Object value) {
        try {
            Field field = getDeclaredFieldIncludingSuperclasses(instance.getClass(), fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getDeclaredFieldIncludingSuperclasses(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}