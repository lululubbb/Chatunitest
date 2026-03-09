package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_212_6Test {
    private JsonReader jsonReader;

    @BeforeEach
    void setUp() {
        Reader reader = new StringReader("");
        jsonReader = new JsonReader(reader);
    }

    @Test
    @Timeout(8000)
    void testPushAddsNewTopWhenStackNotFull() throws Exception {
        // Access stackSize field and stack array
        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        Field stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        // Set initial stackSize less than stack length (32)
        stackSizeField.setInt(jsonReader, 1);
        int[] stack = (int[]) stackField.get(jsonReader);
        int initialStackLength = stack.length;

        // Invoke private method push with newTop=5
        Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(jsonReader, 5);

        // Verify stackSize incremented by 1
        int newStackSize = stackSizeField.getInt(jsonReader);
        assertEquals(2, newStackSize);

        // Verify stack[newStackSize-1] == newTop (5)
        int[] updatedStack = (int[]) stackField.get(jsonReader);
        assertEquals(5, updatedStack[newStackSize - 1]);

        // Verify stack array length unchanged
        assertEquals(initialStackLength, updatedStack.length);
    }

    @Test
    @Timeout(8000)
    void testPushDoublesStackAndAddsNewTopWhenStackFull() throws Exception {
        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        Field stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);

        // Fill stack fully: stackSize == stack.length
        int[] fullStack = new int[4];
        int[] fullPathIndices = new int[4];
        String[] fullPathNames = new String[4];
        for (int i = 0; i < 4; i++) {
            fullStack[i] = i;
            fullPathIndices[i] = i * 10;
            fullPathNames[i] = "name" + i;
        }
        stackField.set(jsonReader, fullStack);
        pathIndicesField.set(jsonReader, fullPathIndices);
        pathNamesField.set(jsonReader, fullPathNames);
        stackSizeField.setInt(jsonReader, fullStack.length);

        // Invoke push with newTop=99
        Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(jsonReader, 99);

        // After push, stackSize should be increased by 1
        int newStackSize = stackSizeField.getInt(jsonReader);
        assertEquals(fullStack.length + 1, newStackSize);

        // stack arrays should be doubled in length
        int[] newStack = (int[]) stackField.get(jsonReader);
        int[] newPathIndices = (int[]) pathIndicesField.get(jsonReader);
        String[] newPathNames = (String[]) pathNamesField.get(jsonReader);

        assertEquals(fullStack.length * 2, newStack.length);
        assertEquals(fullPathIndices.length * 2, newPathIndices.length);
        assertEquals(fullPathNames.length * 2, newPathNames.length);

        // Verify old elements preserved
        for (int i = 0; i < fullStack.length; i++) {
            assertEquals(fullStack[i], newStack[i]);
            assertEquals(fullPathIndices[i], newPathIndices[i]);
            assertEquals(fullPathNames[i], newPathNames[i]);
        }
        // Verify newTop added at last position (stackSize-1)
        assertEquals(99, newStack[newStackSize - 1]);
    }

    @Test
    @Timeout(8000)
    void testPushWithZeroStackSize() throws Exception {
        // Set stackSize to 0
        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonReader, 0);

        // Invoke push with newTop=7
        Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(jsonReader, 7);

        // stackSize should be 1
        int newStackSize = stackSizeField.getInt(jsonReader);
        assertEquals(1, newStackSize);

        // stack[0] should be 7
        Field stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonReader);
        assertEquals(7, stack[0]);
    }
}