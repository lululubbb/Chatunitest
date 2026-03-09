package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonReaderSkipToEndOfLineTest {

    private JsonReader jsonReader;
    private Method skipToEndOfLineMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Create a JsonReader with a dummy Reader, we will manipulate internals directly
        jsonReader = new JsonReader(mock(java.io.Reader.class));

        // Access private method skipToEndOfLine via reflection
        skipToEndOfLineMethod = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
        skipToEndOfLineMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testSkipToEndOfLine_withNewLineInBuffer() throws Throwable {
        // Setup internal buffer with chars including a '\n'
        char[] buffer = new char[1024];
        buffer[0] = 'a';
        buffer[1] = 'b';
        buffer[2] = '\n';
        buffer[3] = 'c';

        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 4);
        setField(jsonReader, "lineNumber", 0);
        setField(jsonReader, "lineStart", 0);

        // Invoke skipToEndOfLine
        try {
            skipToEndOfLineMethod.invoke(jsonReader);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // pos should be at the index after '\n' (3)
        assertEquals(3, getIntField(jsonReader, "pos"));
        // lineNumber should be incremented by 1
        assertEquals(1, getIntField(jsonReader, "lineNumber"));
        // lineStart should be updated to pos
        assertEquals(3, getIntField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    void testSkipToEndOfLine_withCarriageReturnInBuffer() throws Throwable {
        // Setup internal buffer with chars including a '\r'
        char[] buffer = new char[1024];
        buffer[0] = 'x';
        buffer[1] = '\r';
        buffer[2] = 'y';

        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 3);
        setField(jsonReader, "lineNumber", 5);
        setField(jsonReader, "lineStart", 2);

        // Invoke skipToEndOfLine
        try {
            skipToEndOfLineMethod.invoke(jsonReader);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // pos should be at index after '\r' (2)
        assertEquals(2, getIntField(jsonReader, "pos"));
        // lineNumber should NOT be incremented
        assertEquals(5, getIntField(jsonReader, "lineNumber"));
        // lineStart should NOT be updated
        assertEquals(2, getIntField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    void testSkipToEndOfLine_fillBufferReturnsTrue() throws Throwable {
        // Setup internal buffer empty and pos == limit
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineNumber", 0);
        setField(jsonReader, "lineStart", 0);

        // Create spy with private fillBuffer stubbed via reflection
        JsonReader spyReader = spy(jsonReader);

        // Use reflection to make fillBuffer accessible and stub it
        Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        fillBufferMethod.setAccessible(true);

        // We cannot mock private methods directly with Mockito.
        // Instead, create a subclass that overrides fillBuffer for testing.
        JsonReader testReader = new JsonReader(mock(java.io.Reader.class)) {
            private boolean firstCall = true;

            @Override
            protected boolean fillBuffer(int minimum) throws IOException {
                if (firstCall) {
                    firstCall = false;
                    // Setup buffer with '\n' at position 0 after fillBuffer
                    char[] buffer = new char[1024];
                    buffer[0] = '\n';
                    setField(this, "buffer", buffer);
                    setField(this, "pos", 0);
                    setField(this, "limit", 1);
                    return true;
                }
                return false;
            }
        };

        setField(testReader, "pos", 0);
        setField(testReader, "limit", 0);
        setField(testReader, "lineNumber", 0);
        setField(testReader, "lineStart", 0);

        Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
        method.setAccessible(true);

        try {
            method.invoke(testReader);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // pos should be 1 after reading '\n'
        assertEquals(1, getIntField(testReader, "pos"));
        // lineNumber incremented
        assertEquals(1, getIntField(testReader, "lineNumber"));
        // lineStart updated
        assertEquals(1, getIntField(testReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    void testSkipToEndOfLine_fillBufferReturnsFalse() throws Throwable {
        // Setup pos == limit
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineNumber", 0);
        setField(jsonReader, "lineStart", 0);

        // Create subclass to override fillBuffer to always return false
        JsonReader testReader = new JsonReader(mock(java.io.Reader.class)) {
            @Override
            protected boolean fillBuffer(int minimum) {
                return false;
            }
        };

        setField(testReader, "pos", 0);
        setField(testReader, "limit", 0);
        setField(testReader, "lineNumber", 0);
        setField(testReader, "lineStart", 0);

        Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
        method.setAccessible(true);

        // Should exit loop immediately without exception
        try {
            method.invoke(testReader);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // pos should remain unchanged
        assertEquals(0, getIntField(testReader, "pos"));
        // lineNumber should remain unchanged
        assertEquals(0, getIntField(testReader, "lineNumber"));
        // lineStart should remain unchanged
        assertEquals(0, getIntField(testReader, "lineStart"));
    }

    // Helper to set private fields
    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to get private int fields
    private static int getIntField(Object target, String fieldName) {
        try {
            Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}