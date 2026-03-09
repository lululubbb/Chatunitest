package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_212_2Test {

    private JsonReader jsonReader;

    @BeforeEach
    void setUp() {
        // Use a dummy Reader since push doesn't depend on it
        jsonReader = new JsonReader(mock(java.io.Reader.class));
    }

    @Test
    @Timeout(8000)
    void testPush_WhenStackHasSpace_AddsNewTopAndIncrementsStackSize() throws Exception {
        // Arrange
        // Set stackSize less than stack.length to avoid resizing
        setField(jsonReader, "stackSize", 1);
        int[] stack = new int[32];
        setField(jsonReader, "stack", stack);

        // Act
        invokePush(jsonReader, 42);

        // Assert
        int stackSize = getField(jsonReader, "stackSize");
        int[] updatedStack = getField(jsonReader, "stack");
        assertEquals(2, stackSize);
        assertEquals(42, updatedStack[1]);
    }

    @Test
    @Timeout(8000)
    void testPush_WhenStackIsFull_ResizesArraysAndAddsNewTop() throws Exception {
        // Arrange
        int[] fullStack = new int[2];
        fullStack[0] = 10;
        fullStack[1] = 20;
        setField(jsonReader, "stack", fullStack);
        setField(jsonReader, "stackSize", 2);

        int[] pathIndices = new int[2];
        pathIndices[0] = 100;
        pathIndices[1] = 200;
        setField(jsonReader, "pathIndices", pathIndices);

        String[] pathNames = new String[2];
        pathNames[0] = "a";
        pathNames[1] = "b";
        setField(jsonReader, "pathNames", pathNames);

        // Act
        invokePush(jsonReader, 99);

        // Assert
        int stackSize = getField(jsonReader, "stackSize");
        int[] updatedStack = getField(jsonReader, "stack");
        int[] updatedPathIndices = getField(jsonReader, "pathIndices");
        String[] updatedPathNames = getField(jsonReader, "pathNames");

        assertEquals(3, stackSize);
        assertEquals(10, updatedStack[0]);
        assertEquals(20, updatedStack[1]);
        assertEquals(99, updatedStack[2]);
        assertEquals(100, updatedPathIndices[0]);
        assertEquals(200, updatedPathIndices[1]);
        assertNull(updatedPathNames[2]);
        assertEquals(4, updatedStack.length);
        assertEquals(4, updatedPathIndices.length);
        assertEquals(4, updatedPathNames.length);
    }

    // Helper method to invoke private push(int) via reflection
    private void invokePush(JsonReader reader, int newTop) throws Exception {
        Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(reader, newTop);
    }

    // Helper method to set private field via reflection
    @SuppressWarnings("unchecked")
    private <T> void setField(Object target, String fieldName, T value) {
        try {
            Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to get private field via reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object target, String fieldName) {
        try {
            Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}