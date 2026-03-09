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

public class JsonReader_217_6Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    private boolean invokeSkipTo(String toFind) throws Throwable {
        Method skipToMethod = JsonReader.class.getDeclaredMethod("skipTo", String.class);
        skipToMethod.setAccessible(true);
        try {
            return (boolean) skipToMethod.invoke(jsonReader, toFind);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private void setBufferAndPositions(char[] buffer, int pos, int limit, int lineNumber, int lineStart) throws Exception {
        // set private fields via reflection
        var bufferField = JsonReader.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        char[] buf = (char[]) bufferField.get(jsonReader);
        System.arraycopy(buffer, 0, buf, 0, buffer.length);

        var posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        posField.setInt(jsonReader, pos);

        var limitField = JsonReader.class.getDeclaredField("limit");
        limitField.setAccessible(true);
        limitField.setInt(jsonReader, limit);

        var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
        lineNumberField.setAccessible(true);
        lineNumberField.setInt(jsonReader, lineNumber);

        var lineStartField = JsonReader.class.getDeclaredField("lineStart");
        lineStartField.setAccessible(true);
        lineStartField.setInt(jsonReader, lineStart);
    }

    private void setFillBufferBehavior(boolean returnValue) throws Exception {
        // Mock fillBuffer(int) private method by subclassing JsonReader and overriding fillBuffer
        // Since fillBuffer is private, we cannot mock it directly with Mockito.
        // Instead, we create a subclass for testing with overridden fillBuffer.
        // We will create a new instance of a subclass with overridden fillBuffer.

        jsonReader = new JsonReader(mockReader) {
            @Override
            protected boolean fillBuffer(int minimum) throws IOException {
                return returnValue;
            }
        };
    }

    @Test
    @Timeout(8000)
    public void testSkipTo_foundAtCurrentPosition() throws Throwable {
        // buffer contains "abcde", pos=0, limit=5, toFind = "abc"
        char[] buffer = new char[1024];
        String content = "abcde";
        content.getChars(0, content.length(), buffer, 0);
        setBufferAndPositions(buffer, 0, 5, 0, 0);

        // Override fillBuffer to always return false, no more data
        jsonReader = new JsonReader(mockReader) {
            @Override
            protected boolean fillBuffer(int minimum) {
                return false;
            }
        };

        boolean result = invokeSkipTo("abc");
        assertTrue(result);
        // pos should remain unchanged because skipTo increments pos after check, but pos is private and not checked here
    }

    @Test
    @Timeout(8000)
    public void testSkipTo_foundAfterAdvance() throws Throwable {
        // buffer contains "x\nabc", pos=0, limit=6, toFind = "abc"
        char[] buffer = new char[1024];
        String content = "x\nabc";
        content.getChars(0, content.length(), buffer, 0);
        setBufferAndPositions(buffer, 0, 6, 0, 0);

        jsonReader = new JsonReader(mockReader) {
            @Override
            protected boolean fillBuffer(int minimum) {
                return false;
            }
        };

        boolean result = invokeSkipTo("abc");
        assertTrue(result);

        // Check that lineNumber and lineStart updated due to newline at pos=1
        var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
        lineNumberField.setAccessible(true);
        int lineNumber = lineNumberField.getInt(jsonReader);
        assertEquals(1, lineNumber);

        var lineStartField = JsonReader.class.getDeclaredField("lineStart");
        lineStartField.setAccessible(true);
        int lineStart = lineStartField.getInt(jsonReader);
        assertEquals(2, lineStart);
    }

    @Test
    @Timeout(8000)
    public void testSkipTo_notFoundAndFillBufferReturnsFalse() throws Throwable {
        // buffer contains "xyz", pos=0, limit=3, toFind = "abc"
        char[] buffer = new char[1024];
        String content = "xyz";
        content.getChars(0, content.length(), buffer, 0);
        setBufferAndPositions(buffer, 0, 3, 0, 0);

        jsonReader = new JsonReader(mockReader) {
            @Override
            protected boolean fillBuffer(int minimum) {
                return false;
            }
        };

        boolean result = invokeSkipTo("abc");
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testSkipTo_notFoundButFillBufferReturnsTrueThenFalse() throws Throwable {
        // buffer initially contains "xy", pos=0, limit=2, toFind = "abc"
        char[] buffer = new char[1024];
        String initialContent = "xy";
        initialContent.getChars(0, initialContent.length(), buffer, 0);
        setBufferAndPositions(buffer, 0, 2, 0, 0);

        // We create a subclass that simulates fillBuffer behavior:
        // first call returns true and adds "zabc" to buffer, second call returns false
        jsonReader = new JsonReader(mockReader) {
            int fillBufferCallCount = 0;

            @Override
            protected boolean fillBuffer(int minimum) {
                fillBufferCallCount++;
                if (fillBufferCallCount == 1) {
                    // Append "zabc" starting at limit=2
                    char[] buf = new char[BUFFER_SIZE];
                    try {
                        var bufferField = JsonReader.class.getDeclaredField("buffer");
                        bufferField.setAccessible(true);
                        char[] internalBuffer = (char[]) bufferField.get(this);
                        System.arraycopy(internalBuffer, 0, buf, 0, this.limit);
                        String appendStr = "zabc";
                        appendStr.getChars(0, appendStr.length(), buf, this.limit);
                        int newLimit = this.limit + appendStr.length();

                        bufferField.set(this, buf);

                        var limitField = JsonReader.class.getDeclaredField("limit");
                        limitField.setAccessible(true);
                        limitField.setInt(this, newLimit);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
                return false;
            }
        };

        boolean result = invokeSkipTo("abc");
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testSkipTo_handlesNewlinesCorrectly() throws Throwable {
        // buffer contains "\n\nabc", pos=0, limit=5, toFind = "abc"
        char[] buffer = new char[1024];
        String content = "\n\nabc";
        content.getChars(0, content.length(), buffer, 0);
        setBufferAndPositions(buffer, 0, 5, 0, 0);

        jsonReader = new JsonReader(mockReader) {
            @Override
            protected boolean fillBuffer(int minimum) {
                return false;
            }
        };

        boolean result = invokeSkipTo("abc");
        assertTrue(result);

        var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
        lineNumberField.setAccessible(true);
        int lineNumber = lineNumberField.getInt(jsonReader);
        assertEquals(2, lineNumber);

        var lineStartField = JsonReader.class.getDeclaredField("lineStart");
        lineStartField.setAccessible(true);
        int lineStart = lineStartField.getInt(jsonReader);
        assertEquals(2, lineStart);
    }

}