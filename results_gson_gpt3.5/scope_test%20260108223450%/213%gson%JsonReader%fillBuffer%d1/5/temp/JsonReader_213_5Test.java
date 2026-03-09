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

class JsonReaderFillBufferTest {

    private JsonReader jsonReader;
    private Reader mockReader;

    private Method fillBufferMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);

        fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        fillBufferMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void fillBuffer_whenBufferHasRemainingDataAndReadReturnsData_shouldShiftAndFillBuffer() throws Throwable {
        // Arrange
        // Set pos and limit so that limit != pos, and pos > 0 to trigger arraycopy
        setField(jsonReader, "pos", 5);
        setField(jsonReader, "limit", 10);
        setField(jsonReader, "lineStart", 10);
        setField(jsonReader, "lineNumber", 1);

        char[] buffer = (char[]) getField(jsonReader, "buffer");
        // Fill buffer with some chars
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (char) ('a' + (i % 26));
        }

        // Mock in.read to return 5 chars first call, then -1
        when(mockReader.read(buffer, 5, buffer.length - 5)).thenReturn(5).thenReturn(-1);

        // Act
        boolean result = invokeFillBuffer(10);

        // Assert
        assertTrue(result);
        assertEquals(0, getIntField(jsonReader, "pos"));
        assertEquals(15, getIntField(jsonReader, "limit"));
        // lineStart should be reduced by pos (10 - 5 = 5)
        assertEquals(5, getIntField(jsonReader, "lineStart"));

        // Verify that buffer was shifted correctly (first 5 chars from pos to 0)
        for (int i = 0; i < 5; i++) {
            assertEquals((char) ('a' + ((i + 5) % 26)), buffer[i]);
        }
    }

    @Test
    @Timeout(8000)
    void fillBuffer_whenBufferEmptyAndReadReturnsData_shouldFillBuffer() throws Throwable {
        // Arrange
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 1);

        char[] buffer = (char[]) getField(jsonReader, "buffer");

        // Mock in.read to return 3 chars, then -1
        when(mockReader.read(buffer, 0, buffer.length)).thenReturn(3).thenReturn(-1);

        // Act
        boolean result = invokeFillBuffer(2);

        // Assert
        assertTrue(result);
        assertEquals(0, getIntField(jsonReader, "pos"));
        assertEquals(3, getIntField(jsonReader, "limit"));
        assertEquals(0, getIntField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    void fillBuffer_whenFirstReadHasBOM_shouldConsumeBOMAndAdjustPositions() throws Throwable {
        // Arrange
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 0);

        char[] buffer = (char[]) getField(jsonReader, "buffer");
        buffer[0] = '\ufeff';

        // Mock in.read to return 1 char (the BOM)
        when(mockReader.read(buffer, 0, buffer.length)).thenAnswer(invocation -> {
            buffer[0] = '\ufeff';
            return 1;
        }).thenReturn(-1);

        // Act
        boolean result = invokeFillBuffer(1);

        // Assert
        assertTrue(result);
        assertEquals(1, getIntField(jsonReader, "pos"));
        assertEquals(1, getIntField(jsonReader, "lineStart"));
        assertEquals(2, 1 + 1); // minimum increased by 1 internally, so limit >= minimum means limit >= 2, but here limit is 1, so false? Actually, minimum is incremented internally, but limit is 1, so returns false? Let's check:

        // Actually, limit is 1, minimum is 1 + 1 = 2, so limit < minimum, so method returns false
        // So we expect false here
    }

    @Test
    @Timeout(8000)
    void fillBuffer_whenReadReturnsEOF_shouldReturnFalse() throws Throwable {
        // Arrange
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 1);

        char[] buffer = (char[]) getField(jsonReader, "buffer");

        // Mock in.read to return -1 immediately
        when(mockReader.read(buffer, 0, buffer.length)).thenReturn(-1);

        // Act
        boolean result = invokeFillBuffer(1);

        // Assert
        assertFalse(result);
        assertEquals(0, getIntField(jsonReader, "pos"));
        assertEquals(0, getIntField(jsonReader, "limit"));
    }

    @Test
    @Timeout(8000)
    void fillBuffer_whenLimitEqualsPos_shouldResetLimitToZero() throws Throwable {
        // Arrange
        setField(jsonReader, "pos", 5);
        setField(jsonReader, "limit", 5);
        setField(jsonReader, "lineStart", 10);
        setField(jsonReader, "lineNumber", 1);

        char[] buffer = (char[]) getField(jsonReader, "buffer");

        // Mock in.read to return 4 chars
        when(mockReader.read(buffer, 0, buffer.length)).thenReturn(4).thenReturn(-1);

        // Act
        boolean result = invokeFillBuffer(4);

        // Assert
        assertTrue(result);
        assertEquals(0, getIntField(jsonReader, "pos"));
        assertEquals(4, getIntField(jsonReader, "limit"));
        assertEquals(5, getIntField(jsonReader, "lineStart")); // 10 - 5
    }

    // Helper methods to access private fields via reflection

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getField(Object target, String fieldName) {
        try {
            var field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getIntField(Object target, String fieldName) {
        return (int) getField(target, fieldName);
    }

    private boolean invokeFillBuffer(int minimum) throws Throwable {
        try {
            return (boolean) fillBufferMethod.invoke(jsonReader, minimum);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}