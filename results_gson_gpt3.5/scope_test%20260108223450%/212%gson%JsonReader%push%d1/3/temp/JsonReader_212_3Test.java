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

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.Reader;

class JsonReaderPushTest {

    private JsonReader jsonReader;

    @BeforeEach
    void setUp() throws Exception {
        // Create a dummy Reader to pass to the constructor
        Reader dummyReader = mock(Reader.class);
        jsonReader = new JsonReader(dummyReader);

        // Reset stack and stackSize fields via reflection
        setField(jsonReader, "stack", new int[32]);
        setField(jsonReader, "pathIndices", new int[32]);
        setField(jsonReader, "pathNames", new String[32]);
        setField(jsonReader, "stackSize", 0);
    }

    @Test
    @Timeout(8000)
    void testPush_whenStackNotFull_shouldAddNewTop() throws Exception {
        // Arrange
        int newTop = 42;

        // Act
        invokePush(jsonReader, newTop);

        // Assert
        int stackSize = (int) getField(jsonReader, "stackSize");
        assertEquals(1, stackSize);

        int[] stack = (int[]) getField(jsonReader, "stack");
        assertEquals(newTop, stack[0]);
    }

    @Test
    @Timeout(8000)
    void testPush_whenStackFull_shouldResizeAndAddNewTop() throws Exception {
        // Arrange
        int initialCapacity = 5;
        // Set stack arrays with small capacity to test resizing
        setField(jsonReader, "stack", new int[initialCapacity]);
        setField(jsonReader, "pathIndices", new int[initialCapacity]);
        setField(jsonReader, "pathNames", new String[initialCapacity]);
        setField(jsonReader, "stackSize", initialCapacity);

        // Fill stack with dummy values
        int[] stack = (int[]) getField(jsonReader, "stack");
        for (int i = 0; i < initialCapacity; i++) {
            stack[i] = i + 1;
        }
        setField(jsonReader, "stack", stack);

        int newTop = 99;

        // Act
        invokePush(jsonReader, newTop);

        // Assert
        int stackSize = (int) getField(jsonReader, "stackSize");
        assertEquals(initialCapacity + 1, stackSize);

        int[] resizedStack = (int[]) getField(jsonReader, "stack");
        int[] resizedPathIndices = (int[]) getField(jsonReader, "pathIndices");
        String[] resizedPathNames = (String[]) getField(jsonReader, "pathNames");

        // Capacity should be doubled
        assertEquals(initialCapacity * 2, resizedStack.length);
        assertEquals(initialCapacity * 2, resizedPathIndices.length);
        assertEquals(initialCapacity * 2, resizedPathNames.length);

        // Existing elements preserved
        for (int i = 0; i < initialCapacity; i++) {
            assertEquals(i + 1, resizedStack[i]);
        }

        // New top element added at the end
        assertEquals(newTop, resizedStack[initialCapacity]);
    }

    // Helper method to invoke private push(int) method using reflection
    private void invokePush(JsonReader instance, int newTop) throws Exception {
        Method pushMethod = findMethod(instance.getClass(), "push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(instance, newTop);
    }

    // Helper method to get private field value using reflection
    private Object getField(Object instance, String fieldName) throws Exception {
        Field field = findField(instance.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    // Helper method to set private field value using reflection
    private void setField(Object instance, String fieldName, Object value) throws Exception {
        Field field = findField(instance.getClass(), fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    // Helper method to find field in class hierarchy
    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy.");
    }

    // Helper method to find method in class hierarchy
    private Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchMethodException("Method '" + methodName + "' not found in class hierarchy.");
    }
}