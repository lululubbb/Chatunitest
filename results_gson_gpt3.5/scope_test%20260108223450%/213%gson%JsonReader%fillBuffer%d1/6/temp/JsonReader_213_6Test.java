package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_213_6Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    private boolean invokeFillBuffer(int minimum) throws Exception {
        Method method = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        method.setAccessible(true);
        try {
            return (boolean) method.invoke(jsonReader, minimum);
        } catch (InvocationTargetException e) {
            // unwrap IOException if thrown by fillBuffer
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_whenBufferHasDataAndReadReturnsData_shouldShiftAndRead() throws Exception {
        // Setup initial buffer state
        char[] initialBuffer = new char[JsonReader.BUFFER_SIZE];
        initialBuffer[0] = 'a';
        initialBuffer[1] = 'b';
        // fill buffer with some data and set pos and limit accordingly
        setField(jsonReader, "buffer", initialBuffer);
        setField(jsonReader, "pos", 1);
        setField(jsonReader, "limit", 2);
        setField(jsonReader, "lineStart", 5);
        setField(jsonReader, "lineNumber", 1);

        // Mock read to return 2 chars 'c' and 'd'
        when(mockReader.read(any(char[].class), eq(1), eq(JsonReader.BUFFER_SIZE - 1))).thenAnswer(invocation -> {
            char[] buf = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            buf[offset] = 'c';
            buf[offset + 1] = 'd';
            return 2;
        });

        boolean result = invokeFillBuffer(3);

        assertTrue(result);
        assertEquals(0, getIntField(jsonReader, "pos"));
        assertEquals(4, getIntField(jsonReader, "limit"));
        char[] buffer = getCharArrayField(jsonReader, "buffer");
        // After shift, buffer[0] should be 'b' (shifted from pos=1)
        assertEquals('b', buffer[0]);
        assertEquals('c', buffer[2]);
        assertEquals('d', buffer[3]);
        // lineStart adjusted by pos
        assertEquals(4, getIntField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_whenBufferEmptyAndReadReturnsData_shouldFillBuffer() throws Exception {
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 1);

        when(mockReader.read(any(char[].class), eq(0), eq(JsonReader.BUFFER_SIZE))).thenAnswer(invocation -> {
            char[] buf = invocation.getArgument(0);
            buf[0] = 'x';
            buf[1] = 'y';
            return 2;
        });

        boolean result = invokeFillBuffer(2);

        assertTrue(result);
        assertEquals(0, getIntField(jsonReader, "pos"));
        assertEquals(2, getIntField(jsonReader, "limit"));
        char[] buffer = getCharArrayField(jsonReader, "buffer");
        assertEquals('x', buffer[0]);
        assertEquals('y', buffer[1]);
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_whenBOMPresentAtStart_shouldConsumeBOM() throws Exception {
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 1);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 0);
        char[] buffer = new char[JsonReader.BUFFER_SIZE];
        buffer[0] = '\ufeff';
        setField(jsonReader, "buffer", buffer);

        // Mock read to return 1 char 'a' after BOM
        when(mockReader.read(any(char[].class), eq(1), eq(JsonReader.BUFFER_SIZE - 1))).thenAnswer(invocation -> {
            char[] buf = invocation.getArgument(0);
            buf[1] = 'a';
            return 1;
        });

        boolean result = invokeFillBuffer(1);

        assertTrue(result);
        assertEquals(1, getIntField(jsonReader, "pos"));
        assertEquals(2, getIntField(jsonReader, "limit"));
        assertEquals(1, getIntField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_whenReadReturnsMinusOne_shouldReturnFalse() throws Exception {
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 0);

        when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

        boolean result = invokeFillBuffer(1);

        assertFalse(result);
        assertEquals(0, getIntField(jsonReader, "pos"));
        assertEquals(0, getIntField(jsonReader, "limit"));
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_whenIOExceptionThrown_shouldThrow() throws Exception {
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 0);

        when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("read error"));

        IOException thrown = assertThrows(IOException.class, () -> invokeFillBuffer(1));
        assertEquals("read error", thrown.getMessage());
    }

    // Utility methods for reflection access to private fields
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getIntField(Object target, String fieldName) {
        try {
            var field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private char[] getCharArrayField(Object target, String fieldName) {
        try {
            var field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (char[]) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}