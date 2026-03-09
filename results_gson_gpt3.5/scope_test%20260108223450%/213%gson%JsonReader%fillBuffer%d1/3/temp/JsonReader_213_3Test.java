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

public class JsonReader_213_3Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    private boolean invokeFillBuffer(int minimum) throws Throwable {
        Method fillBuffer = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        fillBuffer.setAccessible(true);
        try {
            return (boolean) fillBuffer.invoke(jsonReader, minimum);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_limitNotEqualPos_partialShiftAndReadEnough() throws Throwable {
        // Setup initial state: pos != limit, some data in buffer
        // pos = 3, limit = 5 means 2 chars to shift to front
        setField(jsonReader, "pos", 3);
        setField(jsonReader, "limit", 5);
        char[] buffer = new char[1024];
        buffer[3] = 'a';
        buffer[4] = 'b';
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "lineStart", 10);
        setField(jsonReader, "lineNumber", 1);

        // Mock in.read to read 3 chars, enough to reach minimum = 4 after shift + read
        when(mockReader.read(buffer, 2, buffer.length - 2)).thenAnswer(invocation -> {
            char[] buf = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            // fill 3 chars
            buf[off] = 'c';
            buf[off + 1] = 'd';
            buf[off + 2] = 'e';
            return 3;
        });

        boolean result = invokeFillBuffer(4);

        // After shift: limit = 5 - 3 = 2, then 3 chars read, limit = 5
        assertTrue(result);
        assertEquals(0, getField(jsonReader, "pos"));
        assertEquals(5, getField(jsonReader, "limit"));
        // lineStart decreased by pos (10 - 3)
        assertEquals(7, getField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_limitEqualsPos_noShiftAndReadEnough() throws Throwable {
        // Setup initial state: pos == limit means no data to shift
        setField(jsonReader, "pos", 4);
        setField(jsonReader, "limit", 4);
        char[] buffer = new char[1024];
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "lineStart", 5);
        setField(jsonReader, "lineNumber", 1);

        // Mock in.read to read 5 chars, enough to reach minimum = 5
        when(mockReader.read(buffer, 0, buffer.length)).thenAnswer(invocation -> {
            char[] buf = invocation.getArgument(0);
            for (int i = 0; i < 5; i++) {
                buf[i] = (char) ('a' + i);
            }
            return 5;
        });

        boolean result = invokeFillBuffer(5);

        assertTrue(result);
        assertEquals(0, getField(jsonReader, "pos"));
        assertEquals(5, getField(jsonReader, "limit"));
        // lineStart decreased by pos (5 - 4)
        assertEquals(1, getField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_withBOM_consumesBOMAndReturnsTrue() throws Throwable {
        // Setup initial state: first read, lineNumber=0, lineStart=0
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        char[] buffer = new char[1024];
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 0);

        // Mock in.read to read 1 char: BOM '\ufeff'
        when(mockReader.read(buffer, 0, buffer.length)).thenAnswer(invocation -> {
            buffer[0] = '\ufeff';
            return 1;
        });

        boolean result = invokeFillBuffer(1);

        assertTrue(result);
        assertEquals(1, getField(jsonReader, "pos"));
        assertEquals(1, getField(jsonReader, "lineStart"));
        // limit should be 1 after read
        assertEquals(1, getField(jsonReader, "limit"));
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_readReturnsMinusOne_returnsFalse() throws Throwable {
        // Setup initial state: pos == limit
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        char[] buffer = new char[1024];
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 0);

        // Mock in.read returns -1 immediately
        when(mockReader.read(buffer, 0, buffer.length)).thenReturn(-1);

        boolean result = invokeFillBuffer(1);

        assertFalse(result);
        assertEquals(0, getField(jsonReader, "pos"));
        assertEquals(0, getField(jsonReader, "limit"));
        assertEquals(0, getField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    public void fillBuffer_readsMultipleTimes_untilMinimumReached() throws Throwable {
        // Setup initial state: pos == limit
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        char[] buffer = new char[1024];
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "lineStart", 0);
        setField(jsonReader, "lineNumber", 0);

        // Mock in.read to read 2 chars first, then 2 chars second call, total 4 chars
        when(mockReader.read(buffer, 0, buffer.length))
                .thenAnswer(invocation -> {
                    buffer[0] = 'a';
                    buffer[1] = 'b';
                    return 2;
                })
                .thenAnswer(invocation -> {
                    buffer[2] = 'c';
                    buffer[3] = 'd';
                    return 2;
                })
                .thenReturn(-1);

        boolean result = invokeFillBuffer(4);

        assertTrue(result);
        assertEquals(0, getField(jsonReader, "pos"));
        assertEquals(4, getField(jsonReader, "limit"));
    }

    // Utility methods for reflection field access
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(Object target, String fieldName) {
        try {
            var field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}