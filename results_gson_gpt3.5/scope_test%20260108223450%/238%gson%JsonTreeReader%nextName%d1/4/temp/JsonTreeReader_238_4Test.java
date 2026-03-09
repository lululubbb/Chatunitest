package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_238_4Test {

    private JsonTreeReader jsonTreeReader;

    // Helper class to create Map.Entry mock
    private static class SimpleEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        jsonTreeReader = Mockito.spy(new JsonTreeReader(null));

        // Reset internal stack and stackSize properly
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);

        // Clear stack array
        for (int i = 0; i < stackArray.length; i++) {
            stackArray[i] = null;
        }

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 0);

        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
        for (int i = 0; i < pathNames.length; i++) {
            pathNames[i] = null;
        }

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        for (int i = 0; i < pathIndices.length; i++) {
            pathIndices[i] = 0;
        }
    }

    private void pushToStack(Object obj) throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(jsonTreeReader);

        stackArray[stackSize] = obj;
        stackSizeField.setInt(jsonTreeReader, stackSize + 1);
    }

    private void pushStackSize(int newStackSize) throws Exception {
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, newStackSize);
    }

    private int getStackSize() throws Exception {
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        return stackSizeField.getInt(jsonTreeReader);
    }

    private String[] getPathNames() throws Exception {
        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        return (String[]) pathNamesField.get(jsonTreeReader);
    }

    private Object[] getStackArray() throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        return (Object[]) stackField.get(jsonTreeReader);
    }

    private void push(Object obj) throws Exception {
        Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(jsonTreeReader, obj);
    }

    private void expect(JsonToken token) throws Exception {
        Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);
        expectMethod.invoke(jsonTreeReader, token);
    }

    private void setStackSize(int size) throws Exception {
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, size);
    }

    @Test
    @Timeout(8000)
    public void testNextName_skipNameFalse_returnsKeyAndSetsPathName() throws Throwable {
        // Prepare a Map.Entry mock with key "testKey" and value "testValue"
        Map.Entry<String, String> entry = new SimpleEntry<>("testKey", "testValue");

        // Prepare an Iterator mock that returns the above entry once
        Iterator<Map.Entry<String, String>> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(entry);

        // Push the iterator onto the stack
        pushToStack(iterator);

        // Set stackSize to 1 to simulate iterator on stack
        setStackSize(1);

        // Clear pathNames
        String[] pathNames = getPathNames();
        for (int i = 0; i < pathNames.length; i++) {
            pathNames[i] = null;
        }

        // Mock peekStack to return the iterator
        // Since we can't mock private peekStack easily, ensure stack top is iterator

        // Set up peek to return JsonToken.NAME
        doReturn(JsonToken.NAME).when(jsonTreeReader).peek();

        int stackSizeBefore = getStackSize();

        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        String result = (String) nextNameMethod.invoke(jsonTreeReader, false);

        assertEquals("testKey", result);

        int stackSizeAfter = getStackSize();
        assertEquals(stackSizeBefore + 1, stackSizeAfter);

        assertEquals("testKey", pathNames[stackSizeBefore - 1]);

        Object[] stackArray = getStackArray();

        assertEquals("testValue", stackArray[stackSizeAfter - 1]);
    }

    @Test
    @Timeout(8000)
    public void testNextName_skipNameTrue_setsPathNameToSkipped() throws Throwable {
        Map.Entry<String, String> entry = new SimpleEntry<>("testKey", "testValue");

        Iterator<Map.Entry<String, String>> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(entry);

        pushToStack(iterator);

        // Set stackSize to 1 to simulate iterator on stack
        setStackSize(1);

        String[] pathNames = getPathNames();
        for (int i = 0; i < pathNames.length; i++) {
            pathNames[i] = null;
        }

        // Mock peek to return JsonToken.NAME
        doReturn(JsonToken.NAME).when(jsonTreeReader).peek();

        int stackSizeBefore = getStackSize();

        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        String result = (String) nextNameMethod.invoke(jsonTreeReader, true);

        assertEquals("testKey", result);

        int stackSizeAfter = getStackSize();
        assertEquals(stackSizeBefore + 1, stackSizeAfter);

        assertEquals("<skipped>", pathNames[stackSizeBefore - 1]);

        Object[] stackArray = getStackArray();

        assertEquals("testValue", stackArray[stackSizeAfter - 1]);
    }

    @Test
    @Timeout(8000)
    public void testNextName_iteratorNextThrowsException_propagates() throws Throwable {
        Iterator<?> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenThrow(new RuntimeException("iterator failure"));

        pushToStack(iterator);

        // Set stackSize to 1 to simulate iterator on stack
        setStackSize(1);

        // Mock peek to return JsonToken.NAME
        doReturn(JsonToken.NAME).when(jsonTreeReader).peek();

        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        try {
            nextNameMethod.invoke(jsonTreeReader, false);
            fail("Expected RuntimeException to be thrown");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertNotNull(cause);
            assertEquals("iterator failure", cause.getMessage());
        }
    }
}