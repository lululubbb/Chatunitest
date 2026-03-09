package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_212_1Test {

    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() {
        // Using a dummy Reader since push doesn't use it directly
        jsonReader = new JsonReader(mock(java.io.Reader.class));
    }

    @Test
    @Timeout(8000)
    public void testPushWithSpaceInStack() throws Exception {
        // Access and set private fields stackSize and stack
        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonReader, 0);

        Field stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        // Initialize stack with default size 32
        stackField.set(jsonReader, new int[32]);

        Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        pathIndicesField.set(jsonReader, new int[32]);

        Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        pathNamesField.set(jsonReader, new String[32]);

        Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);

        // Call push with newTop = 5
        pushMethod.invoke(jsonReader, 5);

        // Validate stackSize incremented
        int stackSize = stackSizeField.getInt(jsonReader);
        assertEquals(1, stackSize);

        // Validate stack[0] == 5
        int[] stack = (int[]) stackField.get(jsonReader);
        assertEquals(5, stack[0]);
    }

    @Test
    @Timeout(8000)
    public void testPushWithFullStackExpands() throws Exception {
        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Field stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);

        Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);

        // Fill stack to capacity 32
        int capacity = 32;
        int[] fullStack = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            fullStack[i] = i;
        }
        stackField.set(jsonReader, fullStack);
        pathIndicesField.set(jsonReader, new int[capacity]);
        pathNamesField.set(jsonReader, new String[capacity]);
        stackSizeField.setInt(jsonReader, capacity);

        Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);

        // Invoke push with newTop = 99, triggers array expansion
        pushMethod.invoke(jsonReader, 99);

        // Validate stackSize incremented to 33
        int stackSize = stackSizeField.getInt(jsonReader);
        assertEquals(capacity + 1, stackSize);

        // Validate stack length doubled to 64
        int[] stack = (int[]) stackField.get(jsonReader);
        assertEquals(capacity * 2, stack.length);

        // Validate pathIndices length doubled
        int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
        assertEquals(capacity * 2, pathIndices.length);

        // Validate pathNames length doubled
        String[] pathNames = (String[]) pathNamesField.get(jsonReader);
        assertEquals(capacity * 2, pathNames.length);

        // Validate existing stack content preserved
        for (int i = 0; i < capacity; i++) {
            assertEquals(i, stack[i]);
        }

        // Validate newTop value at position 32
        assertEquals(99, stack[capacity]);
    }
}