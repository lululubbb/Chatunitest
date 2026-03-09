package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.bind.JsonTreeReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;

public class JsonTreeReader_232_2Test {

    private JsonTreeReader jsonTreeReader;
    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() throws Exception {
        jsonObject = new JsonObject();
        jsonTreeReader = new JsonTreeReader(jsonObject);

        // Use reflection to set internal state to simulate a stack with an object and an iterator
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

        // Prepare stack: bottom = JsonObject, above = iterator of entrySet
        Iterator<?> iterator = jsonObject.entrySet().iterator();
        stack[0] = jsonObject;
        stack[1] = iterator;
        stackSizeField.setInt(jsonTreeReader, 2);

        // Set pathNames and pathIndices for index 1
        pathNames[1] = "testName";
        pathIndices[1] = 0;
    }

    @Test
    @Timeout(8000)
    public void testEndObject_success() throws Exception {
        // Setup stack so that after endObject, stackSize becomes 0
        // Spy and call real method directly:
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doCallRealMethod().when(spyReader).endObject();
        spyReader.endObject();

        // Verify stackSize decreased by 2 to 0
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSize);

        // Verify pathNames[stackSize - 1] is not accessed when stackSize == 0 to avoid IndexOutOfBoundsException
        // So no access here, just verify no exception thrown

        // Verify pathIndices incremented if stackSize > 0 (should not increment here because stackSize=0)
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(spyReader);

        // Since stackSize is now 0, no increment expected, so no access to pathIndices[-1]
        assertEquals(0, stackSizeField.getInt(spyReader));
    }

    @Test
    @Timeout(8000)
    public void testEndObject_stackSizeGreaterThanZeroIncrementsPathIndices() throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);

        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);

        // Setup stack with correct elements to avoid MalformedJsonException:
        // stack[0] = JsonObject
        // stack[1] = iterator of stack[0]
        // stack[2] = JsonObject (the one to end)
        stack[0] = new JsonObject();
        stack[1] = ((JsonObject) stack[0]).entrySet().iterator();
        stack[2] = new JsonObject();
        stackSizeField.setInt(jsonTreeReader, 3);

        // Set pathNames and pathIndices for top two stack elements
        pathNames[2] = "name2";
        pathNames[1] = "name1";
        pathIndices[1] = 5;
        pathIndices[0] = 10;

        // Spy and call real method
        JsonTreeReader spyReader = spy(jsonTreeReader);

        // Call real endObject method (cannot mock private expect)
        doCallRealMethod().when(spyReader).endObject();
        spyReader.endObject();

        // After endObject:
        // stackSize should be 1 (3 - 2)
        int newStackSize = stackSizeField.getInt(spyReader);
        assertEquals(1, newStackSize);

        // pathNames[newStackSize - 1] should be null
        assertNull(pathNames[newStackSize - 1]);

        // pathIndices[newStackSize - 1] should be incremented by 1 (from 10 to 11)
        assertEquals(11, pathIndices[newStackSize - 1]);
    }
}