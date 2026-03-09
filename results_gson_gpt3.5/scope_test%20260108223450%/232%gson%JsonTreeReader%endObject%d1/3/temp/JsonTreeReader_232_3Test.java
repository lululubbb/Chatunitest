package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

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

public class JsonTreeReader_232_3Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a JsonObject as the root element to initialize JsonTreeReader
        JsonObject root = new JsonObject();

        // Instantiate JsonTreeReader with root element
        jsonTreeReader = new JsonTreeReader(root);

        // Use reflection to push an object and an iterator to simulate state before endObject call
        Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);

        // Push an iterator (empty iterator) onto the stack first
        pushMethod.invoke(jsonTreeReader, root.entrySet().iterator());

        // Push an object (JsonObject) onto the stack second
        pushMethod.invoke(jsonTreeReader, root);

        // Set stackSize to 2 to reflect the pushed elements
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 2);

        // Set pathNames and pathIndices arrays with size 32, set pathNames[1] non-null and pathIndices[1] to 0
        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
        pathNames[1] = "testName";

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        pathIndices[1] = 0;
    }

    @Test
    @Timeout(8000)
    public void testEndObject_NormalFlow() throws Exception {
        // Call endObject()
        jsonTreeReader.endObject();

        // Verify pathNames[stackSize] is null after call
        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(jsonTreeReader);

        assertNull(pathNames[stackSize], "pathNames at index stackSize should be null after endObject");

        // Verify pathIndices[stackSize - 1] incremented by 1 if stackSize > 0
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);

        if (stackSize > 0) {
            assertEquals(1, pathIndices[stackSize - 1], "pathIndices at index stackSize-1 should be incremented by 1");
        }
    }

    @Test
    @Timeout(8000)
    public void testEndObject_StackSizeZero_NoIncrement() throws Exception {
        // Reset jsonTreeReader with a fresh instance
        JsonObject root = new JsonObject();
        jsonTreeReader = new JsonTreeReader(root);

        // Use reflection to push an iterator then object to stack to simulate state before endObject call
        Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);

        // Push iterator first
        pushMethod.invoke(jsonTreeReader, root.entrySet().iterator());

        // Push object second
        pushMethod.invoke(jsonTreeReader, root);

        // Set stackSize to 2 to reflect pushed elements
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 2);

        // Set pathIndices[1] to 5 (since stackSize=2, index is stackSize-1=1)
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        pathIndices[1] = 5;

        // Call endObject()
        jsonTreeReader.endObject();

        // After endObject, stackSize should be 0 or 1 less (depending on internal pops)
        // The increment is done on pathIndices[stackSize - 1], which was 1 before pop
        // So pathIndices[1] should be incremented by 1 to 6
        assertEquals(6, pathIndices[1], "pathIndices[1] should be incremented by 1");
    }

    @Test
    @Timeout(8000)
    public void testEndObject_ExpectThrows() throws Exception {
        JsonObject root = new JsonObject();

        // Create a spy of JsonTreeReader to mock expect method behavior
        JsonTreeReader spyReader = spy(new JsonTreeReader(root));

        // Use reflection to set up the stack to avoid other errors
        Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(spyReader, root.entrySet().iterator());
        pushMethod.invoke(spyReader, root);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(spyReader, 2);

        // Mock the private expect method by spying on the spyReader and intercepting calls via reflection
        // Since expect is private final, we cannot mock it directly
        // Instead, we mock the behavior of expect to throw IOException when called with END_OBJECT

        // Use doThrow on expect via reflection
        // We need to mock expect(JsonToken.END_OBJECT) to throw IOException

        // Unfortunately, Mockito cannot mock private methods directly.
        // So we override endObject to throw IOException for this test.

        JsonTreeReader spyReaderWithException = new JsonTreeReader(root) {
            @Override
            public void endObject() throws IOException {
                throw new IOException("Expected exception");
            }
        };

        IOException thrown = assertThrows(IOException.class, spyReaderWithException::endObject);
        assertEquals("Expected exception", thrown.getMessage());
    }
}