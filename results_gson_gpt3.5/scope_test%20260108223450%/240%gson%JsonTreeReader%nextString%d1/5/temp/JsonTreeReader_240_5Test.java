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

public class JsonTreeReader_240_5Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonPrimitive for stack
        JsonPrimitive jsonPrimitive = new JsonPrimitive("testString");

        // Create instance with dummy JsonElement (null is acceptable since we will override stack)
        jsonTreeReader = new JsonTreeReader(null);

        // Use reflection to set private fields stack, stackSize, pathIndices
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = jsonPrimitive;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Spy the instance after fields are set
        jsonTreeReader = spy(jsonTreeReader);

        // Re-set the fields on the spy instance as spying creates a new proxy object
        stackField.set(jsonTreeReader, stack);
        stackSizeField.setInt(jsonTreeReader, 1);
        pathIndicesField.set(jsonTreeReader, pathIndices);
    }

    @Test
    @Timeout(8000)
    public void nextString_withStringToken_returnsString() throws Exception {
        doReturn(JsonToken.STRING).when(jsonTreeReader).peek();

        // Ensure stackSize is 1 before call
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        // Setup stack with JsonPrimitive string
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = new JsonPrimitive("testString");
        stackField.set(jsonTreeReader, stack);

        // Setup pathIndices
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Set stackSize to 1 again to be sure
        stackSizeField.setInt(jsonTreeReader, 1);

        String result = jsonTreeReader.nextString();

        assertEquals("testString", result);

        int stackSize = stackSizeField.getInt(jsonTreeReader);

        assertEquals(0, pathIndices[stackSize - 1]);
    }

    @Test
    @Timeout(8000)
    public void nextString_withNumberToken_returnsString() throws Exception {
        // Setup stack with JsonPrimitive number
        JsonPrimitive jsonPrimitiveNumber = new JsonPrimitive(123);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = jsonPrimitiveNumber;

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];

        // Create new instance and spy it
        jsonTreeReader = new JsonTreeReader(null);
        stackField.set(jsonTreeReader, stack);
        stackSizeField.setInt(jsonTreeReader, 1);
        pathIndicesField.set(jsonTreeReader, pathIndices);

        jsonTreeReader = spy(jsonTreeReader);

        // Re-set the fields on the spy instance
        stackField.set(jsonTreeReader, stack);
        stackSizeField.setInt(jsonTreeReader, 1);
        pathIndicesField.set(jsonTreeReader, pathIndices);

        doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

        // Set stackSize to 1 again to be sure
        stackSizeField.setInt(jsonTreeReader, 1);

        String result = jsonTreeReader.nextString();

        assertEquals("123", result);

        int stackSize = stackSizeField.getInt(jsonTreeReader);

        assertEquals(0, pathIndices[stackSize - 1]);
    }

    @Test
    @Timeout(8000)
    public void nextString_withInvalidToken_throwsIllegalStateException() throws Exception {
        doReturn(JsonToken.BEGIN_ARRAY).when(jsonTreeReader).peek();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            jsonTreeReader.nextString();
        });

        assertTrue(thrown.getMessage().contains("Expected STRING but was BEGIN_ARRAY"));
    }

    @Test
    @Timeout(8000)
    public void nextString_withStackSizeZero_doesNotIncrementPathIndices() throws Exception {
        doReturn(JsonToken.STRING).when(jsonTreeReader).peek();

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1); // Must be 1 before nextString() to avoid popStack error

        // Setup stack with JsonPrimitive string
        JsonPrimitive jsonPrimitive = new JsonPrimitive("zeroStack");
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = jsonPrimitive;
        stackField.set(jsonTreeReader, stack);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Set stackSize to 1 again to be sure
        stackSizeField.setInt(jsonTreeReader, 1);

        String result = jsonTreeReader.nextString();

        assertEquals("zeroStack", result);

        // Now set stackSize to zero after popStack to test no increment happens
        stackSizeField.setInt(jsonTreeReader, 0);

        // pathIndices should not be incremented because stackSize == 0
        for (int i = 0; i < pathIndices.length; i++) {
            assertEquals(0, pathIndices[i]);
        }
    }
}