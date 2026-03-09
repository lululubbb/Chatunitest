package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Iterator;

public class JsonTreeReader_232_6Test {

    private JsonTreeReader jsonTreeReader;
    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        jsonTreeReader = new JsonTreeReader(jsonObject);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_NormalFlow() throws Exception {
        // Spy on jsonTreeReader
        JsonTreeReader spyReader = spy(jsonTreeReader);

        // Initialize internal state to simulate being inside an object
        setField(spyReader, "stackSize", 2);
        Object[] stack = new Object[32];
        stack[0] = new Object(); // something on stack
        // The iterator must be a real iterator over a JsonObject's entrySet to avoid MalformedJsonException
        JsonObject obj = new JsonObject();
        obj.addProperty("dummy", "value");
        Iterator<Map.Entry<String, JsonElement>> iterator = obj.entrySet().iterator();
        stack[1] = iterator; // iterator on top of stack
        setField(spyReader, "stack", stack);

        String[] pathNames = new String[32];
        pathNames[1] = "someName";
        setField(spyReader, "pathNames", pathNames);

        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        pathIndices[1] = 5;
        setField(spyReader, "pathIndices", pathIndices);

        // No need to push JsonToken.END_OBJECT, just set stackSize to 2 and stack top to iterator over object

        // Invoke endObject via reflection
        var endObjectMethod = JsonTreeReader.class.getDeclaredMethod("endObject");
        endObjectMethod.setAccessible(true);
        endObjectMethod.invoke(spyReader);

        // Verify pathNames[stackSize - 1] is null
        String[] updatedPathNames = (String[]) getField(spyReader, "pathNames");
        assertNull(updatedPathNames[1]);

        // Verify stackSize decreased by 2
        int updatedStackSize = (int) getField(spyReader, "stackSize");
        assertEquals(0, updatedStackSize);

        // Verify pathIndices[stackSize - 1] incremented (stackSize > 0 condition)
        // Since after popStack twice stackSize is 0, no increment should happen
        int[] updatedPathIndices = (int[]) getField(spyReader, "pathIndices");
        assertEquals(0, updatedPathIndices[0]);
        assertEquals(5, updatedPathIndices[1]);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_PathIndicesIncrementedWhenStackSizeGreaterThanZero() throws Exception {
        JsonTreeReader spyReader = spy(jsonTreeReader);

        // Setup stackSize = 3, so after popping twice it will be 1 (still > 0)
        setField(spyReader, "stackSize", 3);
        Object[] stack = new Object[32];
        stack[0] = new Object();
        // Provide real iterator over JsonObject entrySet to avoid MalformedJsonException
        JsonObject obj = new JsonObject();
        obj.addProperty("dummy", "value");
        Iterator<Map.Entry<String, JsonElement>> iterator = obj.entrySet().iterator();
        stack[1] = iterator;
        stack[2] = new Object();
        setField(spyReader, "stack", stack);

        String[] pathNames = new String[32];
        pathNames[2] = "lastName";
        setField(spyReader, "pathNames", pathNames);

        int[] pathIndices = new int[32];
        pathIndices[0] = 1;
        pathIndices[1] = 2;
        pathIndices[2] = 3;
        setField(spyReader, "pathIndices", pathIndices);

        // Invoke endObject via reflection
        var endObjectMethod = JsonTreeReader.class.getDeclaredMethod("endObject");
        endObjectMethod.setAccessible(true);
        endObjectMethod.invoke(spyReader);

        // After popping twice, stackSize should be 1
        int updatedStackSize = (int) getField(spyReader, "stackSize");
        assertEquals(1, updatedStackSize);

        // pathNames[stackSize - 1] should be null
        String[] updatedPathNames = (String[]) getField(spyReader, "pathNames");
        assertNull(updatedPathNames[0]);

        // pathIndices[stackSize - 1] should be incremented by 1
        int[] updatedPathIndices = (int[]) getField(spyReader, "pathIndices");
        assertEquals(2, updatedPathIndices[0]); // incremented from 1 to 2
        assertEquals(2, updatedPathIndices[1]);
        assertEquals(3, updatedPathIndices[2]);
    }

    // Helper method to set private fields via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = JsonTreeReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to get private fields via reflection
    private Object getField(Object target, String fieldName) {
        try {
            Field field = JsonTreeReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}