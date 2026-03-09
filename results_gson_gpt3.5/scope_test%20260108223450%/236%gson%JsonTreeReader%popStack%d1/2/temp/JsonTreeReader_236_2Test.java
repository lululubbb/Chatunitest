package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.gson.JsonNull;

public class JsonTreeReader_236_2Test {

    private JsonTreeReader jsonTreeReader;
    private Method popStackMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        // Provide a non-null JsonElement to satisfy constructor requirement
        jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);

        // Access private popStack method via reflection
        popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
        popStackMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPopStack_whenStackIsEmpty_shouldThrowArrayIndexOutOfBoundsException() {
        // Set stackSize to 0 explicitly to ensure empty stack
        setField(jsonTreeReader, "stackSize", 0);

        // stack is empty, so popStack should throw ArrayIndexOutOfBoundsException
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            invokePopStack();
        });
    }

    @Test
    @Timeout(8000)
    public void testPopStack_whenStackHasOneElement_shouldReturnThatElementAndNullifyStackSlot() throws Throwable {
        // Use reflection to set stack and stackSize fields
        Object[] stack = new Object[32];
        stack[0] = "testElement";

        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 1);

        Object result = invokePopStack();

        assertEquals("testElement", result);

        Object[] updatedStack = (Object[]) getField(jsonTreeReader, "stack");
        int updatedStackSize = (int) getField(jsonTreeReader, "stackSize");

        assertEquals(0, updatedStackSize);
        assertNull(updatedStack[0]);
    }

    @Test
    @Timeout(8000)
    public void testPopStack_whenStackHasMultipleElements_shouldReturnTopElementAndAdjustStack() throws Throwable {
        Object[] stack = new Object[32];
        stack[0] = "first";
        stack[1] = "second";
        stack[2] = "third";

        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 3);

        Object result = invokePopStack();

        assertEquals("third", result);

        Object[] updatedStack = (Object[]) getField(jsonTreeReader, "stack");
        int updatedStackSize = (int) getField(jsonTreeReader, "stackSize");

        assertEquals(2, updatedStackSize);
        assertNull(updatedStack[2]);
        assertEquals("first", updatedStack[0]);
        assertEquals("second", updatedStack[1]);
    }

    private Object invokePopStack() throws Throwable {
        try {
            return popStackMethod.invoke(jsonTreeReader);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = JsonTreeReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getField(Object target, String fieldName) {
        try {
            var field = JsonTreeReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}