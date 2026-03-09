package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JsonTreeReader_232_4Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        jsonObject.addProperty("key2", "value2");
        jsonTreeReader = new JsonTreeReader(jsonObject);

        // Setup internal stack to simulate state before endObject call
        // stack[stackSize-1] should be an Iterator of the object's entry set (simulate)
        // stack[stackSize-2] should be the JsonObject itself
        // stackSize should be 2 or more

        // Using reflection to set private fields
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);

        // Prepare stack and fields
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = entrySet.iterator();

        stack[0] = iterator; // top of stack is iterator
        stack[1] = jsonObject; // below top is JsonObject
        stackSizeField.setInt(jsonTreeReader, 2);

        pathNames[1] = "someName";
        pathIndices[1] = 5;
    }

    @Test
    @Timeout(8000)
    public void testEndObject_NormalFlow() throws Exception {
        // Use reflection to invoke private expect method to verify it is called with END_OBJECT
        Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);

        // Spy on jsonTreeReader to verify expect call via reflection
        JsonTreeReader spyReader = spy(jsonTreeReader);

        // Instead of mocking expect (private), invoke real method but verify call by reflection after endObject
        // Call endObject
        spyReader.endObject();

        // Verify pathNames[stackSize] is null after endObject
        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = (String[]) pathNamesField.get(spyReader);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(spyReader);
        assertNull(pathNames[stackSize]);

        // Verify stackSize decreased by 2 after popping twice
        assertEquals(0, stackSize);

        // Verify pathIndices[stackSize -1] incremented if stackSize > 0
        // In this case stackSize == 0, so no increment
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
        // Since stackSize is 0, no increment, so no checks
    }

    @Test
    @Timeout(8000)
    public void testEndObject_PathIndicesIncrement() throws Exception {
        // Setup stackSize > 2 to test pathIndices increment
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 3);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        pathIndices[2] = 10;

        // Call endObject normally (no mocking expect)
        jsonTreeReader.endObject();

        // After endObject, pathIndices[stackSize -1] should be incremented by 1
        assertEquals(11, pathIndices[2]);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_ExpectThrowsIOException() throws Exception {
        // Use reflection to get the current top of stack as JsonElement
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(jsonTreeReader);
        Object top = stack[stackSize - 1];

        // Find the JsonElement to pass to constructor
        JsonElement element;
        if (top instanceof JsonElement) {
            element = (JsonElement) top;
        } else if (top instanceof Iterator) {
            // If top is iterator, get the JsonObject below it
            element = (JsonElement) stack[stackSize - 2];
        } else {
            // fallback to a new empty JsonObject
            element = new JsonObject();
        }

        // Create a spy of JsonTreeReader to override expect method to throw IOException
        JsonTreeReader throwingReader = spy(new JsonTreeReader(element));
        doThrow(new IOException("Expected exception")).when(throwingReader).expect(JsonToken.END_OBJECT);

        IOException thrown = assertThrows(IOException.class, throwingReader::endObject);
        assertEquals("Expected exception", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_StackUnderflow() throws Exception {
        // Setup stackSize = 1 to cause popStack to underflow or similar
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        // Call endObject normally (no mocking expect)
        assertDoesNotThrow(() -> jsonTreeReader.endObject());

        // After endObject, stackSize should be 0 or more depending on popStack implementation
        int stackSize = stackSizeField.getInt(jsonTreeReader);
        assertTrue(stackSize >= 0);
    }
}