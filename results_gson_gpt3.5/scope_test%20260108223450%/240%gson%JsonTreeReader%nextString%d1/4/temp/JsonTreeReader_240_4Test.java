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

public class JsonTreeReader_240_4Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonPrimitive to initialize JsonTreeReader constructor.
        // Since JsonTreeReader's constructor requires a JsonElement, we create a JsonPrimitive.
        JsonPrimitive jsonPrimitive = new JsonPrimitive("initial");
        jsonTreeReader = new JsonTreeReader(jsonPrimitive);

        // Initialize stack and stackSize properly to avoid NullPointerException or array index issues
        setField(jsonTreeReader, "stackSize", 1);
        Object[] stack = new Object[32];
        stack[0] = jsonPrimitive;
        setField(jsonTreeReader, "stack", stack);

        int[] pathIndices = new int[32];
        setField(jsonTreeReader, "pathIndices", pathIndices);

        String[] pathNames = new String[32];
        setField(jsonTreeReader, "pathNames", pathNames);
    }

    @Test
    @Timeout(8000)
    public void testNextString_WithStringToken_ReturnsString() throws Exception {
        // Set stackSize to 1
        setField(jsonTreeReader, "stackSize", 1);

        // Set stack[0] to a JsonPrimitive mock that returns a known string when getAsString() is called
        JsonPrimitive jsonPrimitiveMock = mock(JsonPrimitive.class);
        when(jsonPrimitiveMock.getAsString()).thenReturn("testString");
        setStackElement(jsonTreeReader, 0, jsonPrimitiveMock);

        // Mock peek() method to return JsonToken.STRING using spy
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        // Set pathIndices[0] to 0 before increment
        setPathIndex(spyReader, 0, 0);

        String result = spyReader.nextString();

        assertEquals("testString", result);
        assertEquals(1, getPathIndex(spyReader, 0));
    }

    @Test
    @Timeout(8000)
    public void testNextString_WithNumberToken_ReturnsString() throws Exception {
        setField(jsonTreeReader, "stackSize", 1);

        JsonPrimitive jsonPrimitiveMock = mock(JsonPrimitive.class);
        when(jsonPrimitiveMock.getAsString()).thenReturn("123");
        setStackElement(jsonTreeReader, 0, jsonPrimitiveMock);

        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        setPathIndex(spyReader, 0, 5);

        String result = spyReader.nextString();

        assertEquals("123", result);
        assertEquals(6, getPathIndex(spyReader, 0));
    }

    @Test
    @Timeout(8000)
    public void testNextString_WithInvalidToken_ThrowsIllegalStateException() throws Exception {
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

        IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextString);
        assertTrue(exception.getMessage().contains("Expected STRING but was BEGIN_ARRAY"));
    }

    @Test
    @Timeout(8000)
    public void testNextString_WithStackSizeZero_DoesNotIncrementPathIndices() throws Exception {
        // Set stackSize to 1 and stack[0] to a mock JsonPrimitive
        setField(jsonTreeReader, "stackSize", 1);

        JsonPrimitive jsonPrimitiveMock = mock(JsonPrimitive.class);
        when(jsonPrimitiveMock.getAsString()).thenReturn("value");
        setStackElement(jsonTreeReader, 0, jsonPrimitiveMock);

        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        // Set pathIndices[0] to 0 before calling nextString
        setPathIndex(spyReader, 0, 0);

        // Call nextString(), which will popStack and decrement stackSize internally
        String result = spyReader.nextString();

        assertEquals("value", result);

        // After popStack, stackSize is 0, so pathIndices should not have been incremented
        // pathIndices[0] should remain 0
        assertEquals(0, getPathIndex(spyReader, 0));
    }

    // Helper methods to set private fields via reflection

    private void setStackElement(Object target, int index, Object value) throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(target);
        if (stack == null) {
            stack = new Object[32];
            stackField.set(target, stack);
        }
        stack[index] = value;
    }

    private void setPathIndex(Object target, int index, int value) throws Exception {
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(target);
        if (pathIndices == null) {
            pathIndices = new int[32];
            pathIndicesField.set(target, pathIndices);
        }
        pathIndices[index] = value;
    }

    private int getPathIndex(Object target, int index) throws Exception {
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(target);
        if (pathIndices == null) {
            return 0;
        }
        return pathIndices[index];
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonTreeReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}