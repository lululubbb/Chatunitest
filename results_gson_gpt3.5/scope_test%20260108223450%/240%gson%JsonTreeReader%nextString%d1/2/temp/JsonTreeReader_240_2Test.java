package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonTreeReader_240_2Test {

    private JsonTreeReader reader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonPrimitive to initialize the reader
        JsonPrimitive primitive = new JsonPrimitive("dummy");
        reader = new JsonTreeReader(primitive);

        // Clear and reset stack and stackSize fields to prepare for tests
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stackField.set(reader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 0);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndicesField.set(reader, pathIndices);
    }

    @Test
    @Timeout(8000)
    public void nextString_withStringToken_returnsString() throws Exception {
        // Arrange
        setStackWithTop(JsonToken.STRING, new JsonPrimitive("testString"));

        // Act
        String result = reader.nextString();

        // Assert
        assertEquals("testString", result);
        assertPathIndexIncremented(1);
    }

    @Test
    @Timeout(8000)
    public void nextString_withNumberToken_returnsNumberAsString() throws Exception {
        // Arrange
        setStackWithTop(JsonToken.NUMBER, new JsonPrimitive(123));

        // Act
        String result = reader.nextString();

        // Assert
        assertEquals("123", result);
        assertPathIndexIncremented(1);
    }

    @Test
    @Timeout(8000)
    public void nextString_withInvalidToken_throwsIllegalStateException() throws Exception {
        // Arrange
        setStackWithTop(JsonToken.BEGIN_ARRAY, new JsonPrimitive("shouldNotBeUsed"));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reader.nextString();
        });
        assertTrue(exception.getMessage().contains("Expected STRING but was BEGIN_ARRAY"));
    }

    @Test
    @Timeout(8000)
    public void nextString_withStackSizeZero_doesNotIncrementPathIndices() throws Exception {
        // Arrange
        // Setup stack and stackSize to 1 with a STRING token, then set stackSize to 0 AFTER spying
        JsonPrimitive primitive = new JsonPrimitive("zeroStack");

        // Setup stack array and pathIndices array
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitive;
        stackField.set(reader, stack);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndicesField.set(reader, pathIndices);

        // Set stackSize to 1 first
        setStackSize(1);

        // Spy the reader and override peek() to return STRING token
        JsonTreeReader spyReader = spy(reader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        // Replace reader with spyReader
        this.reader = spyReader;

        // Now set stackSize to 0 to simulate empty stack before calling nextString
        setStackSize(0);

        // Act
        String result = reader.nextString();

        // Assert
        assertEquals("zeroStack", result);
        // pathIndices should not be incremented because stackSize == 0
        int[] currentPathIndices = getPathIndices();
        for (int i : currentPathIndices) {
            assertEquals(0, i);
        }
    }

    // Helper methods

    private void setStackWithTop(JsonToken token, JsonPrimitive primitive) throws Exception {
        // Set stackSize to 1
        setStackSize(1);

        // Set stack[0] to primitive
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(reader);
        stack[0] = primitive;

        // Set pathIndices[0] to 0
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);
        pathIndices[0] = 0;

        // Spy the reader and override peek() to return the desired token
        JsonTreeReader spyReader = spy(reader);
        doReturn(token).when(spyReader).peek();

        // Replace reader with spyReader in this test instance
        this.reader = spyReader;
    }

    private void setStackSize(int size) throws Exception {
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, size);
    }

    private void assertPathIndexIncremented(int expected) throws Exception {
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(reader);
        if (stackSize > 0) {
            assertEquals(expected, pathIndices[stackSize - 1]);
        } else {
            // If stackSize == 0, no increment expected
            for (int i : pathIndices) {
                assertEquals(0, i);
            }
        }
    }

    private int[] getPathIndices() throws Exception {
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        return (int[]) pathIndicesField.get(reader);
    }
}