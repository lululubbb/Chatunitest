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

public class JsonReader_212_4Test {

    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() throws Exception {
        Reader mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
        // reset stack and stackSize for each test
        Field stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(jsonReader, new int[32]);

        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonReader, 0);

        Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        pathIndicesField.set(jsonReader, new int[32]);

        Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        pathNamesField.set(jsonReader, new String[32]);
    }

    @Test
    @Timeout(8000)
    public void testPush_WhenStackNotFull_AddsNewTop() throws Exception {
        // stackSize < stack.length
        setStackSize(1);
        int newTop = 42;

        invokePush(newTop);

        int[] stack = getStack();
        int stackSize = getStackSize();

        assertEquals(2, stackSize);
        assertEquals(newTop, stack[1]);
    }

    @Test
    @Timeout(8000)
    public void testPush_WhenStackIsFull_ExpandsArraysAndAddsNewTop() throws Exception {
        int[] fullStack = new int[2];
        fullStack[0] = 1;
        fullStack[1] = 2;
        setStack(fullStack);
        setStackSize(2);

        setPathIndices(new int[2]);
        setPathNames(new String[2]);

        int newTop = 99;

        invokePush(newTop);

        int[] stack = getStack();
        int stackSize = getStackSize();
        int[] pathIndices = getPathIndices();
        String[] pathNames = getPathNames();

        assertEquals(3, stackSize);
        assertTrue(stack.length >= 4); // doubled from 2 to 4
        assertTrue(pathIndices.length >= 4);
        assertTrue(pathNames.length >= 4);
        assertEquals(newTop, stack[2]);
    }

    // Helper methods to use reflection

    private void invokePush(int newTop) throws Exception {
        Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(jsonReader, newTop);
    }

    private int[] getStack() throws Exception {
        Field stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        return (int[]) stackField.get(jsonReader);
    }

    private void setStack(int[] newStack) throws Exception {
        Field stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(jsonReader, newStack);
    }

    private int getStackSize() throws Exception {
        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        return stackSizeField.getInt(jsonReader);
    }

    private void setStackSize(int size) throws Exception {
        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonReader, size);
    }

    private int[] getPathIndices() throws Exception {
        Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        return (int[]) pathIndicesField.get(jsonReader);
    }

    private void setPathIndices(int[] pathIndices) throws Exception {
        Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        pathIndicesField.set(jsonReader, pathIndices);
    }

    private String[] getPathNames() throws Exception {
        Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        return (String[]) pathNamesField.get(jsonReader);
    }

    private void setPathNames(String[] pathNames) throws Exception {
        Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        pathNamesField.set(jsonReader, pathNames);
    }
}