package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.JsonTreeReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

class JsonTreeReaderPushTest {

    private JsonTreeReader jsonTreeReader;
    private Method pushMethod;
    private Field stackField;
    private Field stackSizeField;
    private Field pathIndicesField;
    private Field pathNamesField;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonElement to instantiate JsonTreeReader (using null here as constructor param is not focal)
        jsonTreeReader = new JsonTreeReader(null);

        // Access private push method
        pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);

        // Access private fields
        stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);

        pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPushNormalWithinCapacity() throws Exception {
        // Initial stackSize = 0, stack length = 32
        // Push one object, stackSize should increase by 1, stack array unchanged

        Object newTop = new Object();
        int initialStackSize = (int) stackSizeField.get(jsonTreeReader);
        Object[] initialStack = (Object[]) stackField.get(jsonTreeReader);
        int initialLength = initialStack.length;

        pushMethod.invoke(jsonTreeReader, newTop);

        int updatedStackSize = (int) stackSizeField.get(jsonTreeReader);
        Object[] updatedStack = (Object[]) stackField.get(jsonTreeReader);

        // stackSize incremented by 1
        assertEquals(initialStackSize + 1, updatedStackSize);

        // The newTop is at the top of the stack
        assertSame(newTop, updatedStack[initialStackSize]);

        // The stack array reference remains the same (no resize)
        assertSame(initialStack, updatedStack);

        // pathIndices and pathNames arrays length remain unchanged
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
        assertEquals(initialLength, pathIndices.length);
        assertEquals(initialLength, pathNames.length);
    }

    @Test
    @Timeout(8000)
    public void testPushTriggersResize() throws Exception {
        // Setup stackSize == stack.length to trigger resize
        Object[] initialStack = new Object[4];
        Arrays.fill(initialStack, new Object());
        stackField.set(jsonTreeReader, initialStack);
        stackSizeField.set(jsonTreeReader, initialStack.length);

        int[] initialPathIndices = new int[4];
        pathIndicesField.set(jsonTreeReader, initialPathIndices);

        String[] initialPathNames = new String[4];
        pathNamesField.set(jsonTreeReader, initialPathNames);

        Object newTop = new Object();

        pushMethod.invoke(jsonTreeReader, newTop);

        int updatedStackSize = (int) stackSizeField.get(jsonTreeReader);
        Object[] updatedStack = (Object[]) stackField.get(jsonTreeReader);
        int[] updatedPathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        String[] updatedPathNames = (String[]) pathNamesField.get(jsonTreeReader);

        // stackSize incremented by 1
        assertEquals(initialStack.length + 1, updatedStackSize);

        // The newTop is at the top of the stack
        assertSame(newTop, updatedStack[initialStack.length]);

        // The stack array reference is new (resized)
        assertNotSame(initialStack, updatedStack);

        // The new length is doubled
        assertEquals(initialStack.length * 2, updatedStack.length);
        assertEquals(initialPathIndices.length * 2, updatedPathIndices.length);
        assertEquals(initialPathNames.length * 2, updatedPathNames.length);

        // The old contents are preserved
        for (int i = 0; i < initialStack.length; i++) {
            assertSame(initialStack[i], updatedStack[i]);
            assertEquals(initialPathIndices[i], updatedPathIndices[i]);
            assertEquals(initialPathNames[i], updatedPathNames[i]);
        }
    }
}