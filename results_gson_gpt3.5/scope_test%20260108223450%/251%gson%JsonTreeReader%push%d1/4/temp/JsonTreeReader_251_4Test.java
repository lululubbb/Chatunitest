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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.bind.JsonTreeReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonTreeReaderPushTest {

    private JsonTreeReader jsonTreeReader;
    private Method pushMethod;
    private Field stackField;
    private Field stackSizeField;
    private Field pathIndicesField;
    private Field pathNamesField;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonElement to instantiate JsonTreeReader
        // Using null because constructor parameter is not used by push method
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
    public void testPushWhenStackNotFull() throws Exception {
        // Initial stackSize should be 0
        stackSizeField.setInt(jsonTreeReader, 0);

        // Save original stack length
        Object[] originalStack = (Object[]) stackField.get(jsonTreeReader);
        int originalLength = originalStack.length;

        // Push an object
        Object newTop = new Object();
        pushMethod.invoke(jsonTreeReader, newTop);

        // Verify stackSize incremented by 1
        int newStackSize = stackSizeField.getInt(jsonTreeReader);
        assertEquals(1, newStackSize);

        // Verify newTop is at stack[stackSize-1]
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        assertSame(newTop, stack[newStackSize - 1]);

        // Verify stack array length unchanged
        assertEquals(originalLength, stack.length);

        // Verify pathIndices and pathNames arrays length unchanged
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
        assertEquals(originalLength, pathIndices.length);
        assertEquals(originalLength, pathNames.length);
    }

    @Test
    @Timeout(8000)
    public void testPushWhenStackFull() throws Exception {
        // Set stackSize to stack length to simulate full stack
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        int originalLength = stack.length;

        stackSizeField.setInt(jsonTreeReader, originalLength);

        // Fill stack with dummy objects
        Object[] newStack = new Object[originalLength];
        for (int i = 0; i < originalLength; i++) {
            newStack[i] = new Object();
        }
        stackField.set(jsonTreeReader, newStack);

        // Fill pathIndices and pathNames arrays with initial values
        int[] pathIndices = new int[originalLength];
        String[] pathNames = new String[originalLength];
        for (int i = 0; i < originalLength; i++) {
            pathIndices[i] = i;
            pathNames[i] = "name" + i;
        }
        pathIndicesField.set(jsonTreeReader, pathIndices);
        pathNamesField.set(jsonTreeReader, pathNames);

        // Push new object to trigger resizing
        Object newTop = new Object();
        pushMethod.invoke(jsonTreeReader, newTop);

        // Verify stackSize incremented by 1
        int newStackSize = stackSizeField.getInt(jsonTreeReader);
        assertEquals(originalLength + 1, newStackSize);

        // Verify stack array resized (doubled)
        Object[] resizedStack = (Object[]) stackField.get(jsonTreeReader);
        assertEquals(originalLength * 2, resizedStack.length);

        // Verify old elements preserved
        for (int i = 0; i < originalLength; i++) {
            assertSame(newStack[i], resizedStack[i]);
        }

        // Verify newTop is at top of stack
        assertSame(newTop, resizedStack[newStackSize - 1]);

        // Verify pathIndices and pathNames arrays resized and old values preserved
        int[] resizedPathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        String[] resizedPathNames = (String[]) pathNamesField.get(jsonTreeReader);

        assertEquals(originalLength * 2, resizedPathIndices.length);
        assertEquals(originalLength * 2, resizedPathNames.length);

        for (int i = 0; i < originalLength; i++) {
            assertEquals(pathIndices[i], resizedPathIndices[i]);
            assertEquals(pathNames[i], resizedPathNames[i]);
        }
    }
}