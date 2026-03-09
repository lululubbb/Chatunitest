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

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonReader_216_2Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testSkipToEndOfLine_WithNewLineInBuffer() throws Exception {
        // Arrange
        char[] buffer = new char[1024];
        buffer[0] = 'a';
        buffer[1] = 'b';
        buffer[2] = '\n'; // newline char to stop at
        buffer[3] = 'c';

        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 4);
        setField(jsonReader, "lineNumber", 0);
        setField(jsonReader, "lineStart", 0);

        // Act
        invokeSkipToEndOfLine(jsonReader);

        // Assert
        int pos = getField(jsonReader, "pos");
        int lineNumber = getField(jsonReader, "lineNumber");
        int lineStart = getField(jsonReader, "lineStart");

        assertEquals(3, pos, "Position should have advanced to after newline");
        assertEquals(1, lineNumber, "lineNumber should be incremented");
        assertEquals(pos, lineStart, "lineStart should be updated to pos");
    }

    @Test
    @Timeout(8000)
    public void testSkipToEndOfLine_WithCarriageReturnInBuffer() throws Exception {
        // Arrange
        char[] buffer = new char[1024];
        buffer[0] = 'x';
        buffer[1] = '\r'; // carriage return to stop at
        buffer[2] = 'y';

        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 3);
        setField(jsonReader, "lineNumber", 5);
        setField(jsonReader, "lineStart", 3);

        // Act
        invokeSkipToEndOfLine(jsonReader);

        // Assert
        int pos = getField(jsonReader, "pos");
        int lineNumber = getField(jsonReader, "lineNumber");
        int lineStart = getField(jsonReader, "lineStart");

        assertEquals(2, pos, "Position should have advanced to after carriage return");
        assertEquals(5, lineNumber, "lineNumber should not increment on carriage return");
        assertEquals(3, lineStart, "lineStart should remain unchanged");
    }

    @Test
    @Timeout(8000)
    public void testSkipToEndOfLine_BufferEmpty_FillBufferReturnsFalse() throws Exception {
        // Arrange
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);

        // Create a subclass without @Override annotation to avoid compile error
        JsonReader spyWithMockFillBuffer = new JsonReader(mockReader) {
            protected boolean fillBuffer(int minimum) throws IOException {
                return false;
            }
        };

        setField(spyWithMockFillBuffer, "pos", 0);
        setField(spyWithMockFillBuffer, "limit", 0);

        // Act & Assert
        invokeSkipToEndOfLine(spyWithMockFillBuffer);

        int pos = getField(spyWithMockFillBuffer, "pos");
        assertEquals(0, pos, "Position should remain unchanged when buffer empty and fillBuffer returns false");
    }

    @Test
    @Timeout(8000)
    public void testSkipToEndOfLine_BufferEmpty_FillBufferReturnsTrue_WithNewLine() throws Exception {
        // Arrange
        char[] buffer = new char[1024];
        buffer[0] = '\n';

        // Create a subclass without @Override annotation to avoid compile error
        JsonReader spyWithMockFillBuffer = new JsonReader(mockReader) {
            protected boolean fillBuffer(int minimum) throws IOException {
                try {
                    setField(this, "buffer", buffer);
                    setField(this, "limit", 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        };

        setField(spyWithMockFillBuffer, "pos", 0);
        setField(spyWithMockFillBuffer, "limit", 0);
        setField(spyWithMockFillBuffer, "lineNumber", 0);
        setField(spyWithMockFillBuffer, "lineStart", 0);

        // Act
        invokeSkipToEndOfLine(spyWithMockFillBuffer);

        // Assert
        int pos = getField(spyWithMockFillBuffer, "pos");
        int lineNumber = getField(spyWithMockFillBuffer, "lineNumber");
        int lineStart = getField(spyWithMockFillBuffer, "lineStart");

        assertEquals(1, pos, "Position should have advanced after newline");
        assertEquals(1, lineNumber, "lineNumber should increment");
        assertEquals(pos, lineStart, "lineStart should update to pos");
    }

    private void invokeSkipToEndOfLine(JsonReader reader) throws Exception {
        Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
        method.setAccessible(true);
        method.invoke(reader);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private int getField(Object target, String fieldName) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getInt(target);
    }
}