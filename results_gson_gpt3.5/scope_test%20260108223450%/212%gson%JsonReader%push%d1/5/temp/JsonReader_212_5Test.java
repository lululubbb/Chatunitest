package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_212_5Test {

    private JsonReader jsonReader;

    @BeforeEach
    void setUp() throws Exception {
        Reader mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);

        // Reset private fields stack and stackSize to known state
        setPrivateField(jsonReader, "stack", new int[32]);
        setPrivateField(jsonReader, "pathIndices", new int[32]);
        setPrivateField(jsonReader, "pathNames", new String[32]);
        setPrivateField(jsonReader, "stackSize", 0);
    }

    @Test
    @Timeout(8000)
    void testPushAddsNewTopWhenStackNotFull() throws Exception {
        // stackSize < stack.length (default 0 < 32)
        int initialStackSize = (int) getPrivateField(jsonReader, "stackSize");
        int newTop = 42;

        invokePush(jsonReader, newTop);

        int[] stack = (int[]) getPrivateField(jsonReader, "stack");
        int stackSize = (int) getPrivateField(jsonReader, "stackSize");

        assertEquals(initialStackSize + 1, stackSize);
        assertEquals(newTop, stack[stackSize - 1]);
    }

    @Test
    @Timeout(8000)
    void testPushExpandsStackWhenFull() throws Exception {
        // Fill stack to capacity
        int capacity = 32;
        int[] fullStack = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            fullStack[i] = i;
        }
        setPrivateField(jsonReader, "stack", fullStack);
        setPrivateField(jsonReader, "stackSize", capacity);

        int[] pathIndices = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            pathIndices[i] = i + 100;
        }
        setPrivateField(jsonReader, "pathIndices", pathIndices);

        String[] pathNames = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            pathNames[i] = "name" + i;
        }
        setPrivateField(jsonReader, "pathNames", pathNames);

        int newTop = 99;

        invokePush(jsonReader, newTop);

        int[] stack = (int[]) getPrivateField(jsonReader, "stack");
        int[] newPathIndices = (int[]) getPrivateField(jsonReader, "pathIndices");
        String[] newPathNames = (String[]) getPrivateField(jsonReader, "pathNames");
        int stackSize = (int) getPrivateField(jsonReader, "stackSize");

        assertEquals(capacity * 2, stack.length);
        assertEquals(capacity * 2, newPathIndices.length);
        assertEquals(capacity * 2, newPathNames.length);

        // Original values preserved
        for (int i = 0; i < capacity; i++) {
            assertEquals(i, stack[i]);
            assertEquals(i + 100, newPathIndices[i]);
            assertEquals("name" + i, newPathNames[i]);
        }

        // New value added at old capacity index
        assertEquals(newTop, stack[capacity]);
        assertEquals(capacity + 1, stackSize);
    }

    // Helper to invoke private push method
    private void invokePush(JsonReader reader, int newTop) throws Exception {
        Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(reader, newTop);
    }

    // Helper to get private field value
    private Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    // Helper to set private field value
    private void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}