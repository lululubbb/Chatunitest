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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_191_3Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_whenPeekedIsPeekedBeginObject_shouldPushEmptyObjectAndResetPeeked() throws Exception {
        // Set peeked to PEEKED_BEGIN_OBJECT (1)
        setPrivateField(jsonReader, "peeked", 1);
        // Set stackSize to 0 initially
        setPrivateField(jsonReader, "stackSize", 0);

        jsonReader.beginObject();

        int peeked = (int) getPrivateField(jsonReader, "peeked");
        int stackSize = (int) getPrivateField(jsonReader, "stackSize");
        int[] stack = (int[]) getPrivateField(jsonReader, "stack");

        assertEquals(0, peeked, "peeked should be reset to PEEKED_NONE");
        assertEquals(1, stackSize, "stackSize should be incremented after push");
        assertEquals(6, stack[0], "stack top should be JsonScope.EMPTY_OBJECT (6)");
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_whenPeekedIsPeekedNoneAndDoPeekReturnsBeginObject_shouldPushEmptyObjectAndResetPeeked() throws Exception {
        // peeked is PEEKED_NONE (0)
        setPrivateField(jsonReader, "peeked", 0);
        setPrivateField(jsonReader, "stackSize", 0);

        JsonReader spyReader = spy(jsonReader);
        doReturn(1).when(spyReader).doPeek();

        spyReader.beginObject();

        int peeked = (int) getPrivateField(spyReader, "peeked");
        int stackSize = (int) getPrivateField(spyReader, "stackSize");
        int[] stack = (int[]) getPrivateField(spyReader, "stack");

        assertEquals(0, peeked, "peeked should be reset to PEEKED_NONE");
        assertEquals(1, stackSize, "stackSize should be incremented after push");
        assertEquals(6, stack[0], "stack top should be JsonScope.EMPTY_OBJECT (6)");
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_whenPeekedIsInvalid_shouldThrowIllegalStateException() throws Exception {
        // Set peeked to an invalid value (e.g., 5 = PEEKED_TRUE)
        setPrivateField(jsonReader, "peeked", 5);

        // Mock peek() to return a token string
        JsonToken mockToken = JsonToken.TRUE; // fixed line

        JsonReader spyReader = spy(jsonReader);
        doReturn(mockToken).when(spyReader).peek();

        // Mock locationString() to return location info
        doReturn(" at line 1 column 1 path $").when(spyReader).locationString();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::beginObject);
        assertTrue(thrown.getMessage().contains("Expected BEGIN_OBJECT but was TRUE at line 1 column 1 path $"));
    }

    // Helper to set private fields via reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // Helper to get private fields via reflection
    private Object getPrivateField(Object target, String fieldName) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}