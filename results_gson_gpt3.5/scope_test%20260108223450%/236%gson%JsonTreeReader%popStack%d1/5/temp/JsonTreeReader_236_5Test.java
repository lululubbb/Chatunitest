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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_236_5Test {

    private JsonTreeReader jsonTreeReader;
    private Method popStackMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonElement to construct JsonTreeReader instance
        // Using JsonNull.INSTANCE as a simple JsonElement
        jsonTreeReader = new JsonTreeReader(com.google.gson.JsonNull.INSTANCE);

        // Access private method popStack via reflection
        popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
        popStackMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPopStack_withEmptyStack_shouldThrowArrayIndexOutOfBoundsException() throws Exception {
        // stackSize is initially 0, popStack will decrement it to -1 and access stack[-1]
        // This causes ArrayIndexOutOfBoundsException wrapped in InvocationTargetException

        // Set stackSize to 0 explicitly
        setField(jsonTreeReader, "stackSize", 0);

        // Set stack array to all null
        setField(jsonTreeReader, "stack", new Object[32]);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> popStackMethod.invoke(jsonTreeReader));
        assertTrue(thrown.getCause() instanceof ArrayIndexOutOfBoundsException);
    }

    @Test
    @Timeout(8000)
    public void testPopStack_withOneElement_shouldPopAndReturnElement() throws Exception {
        Object[] stack = new Object[32];
        stack[0] = "testElement";
        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 1);

        Object result = popStackMethod.invoke(jsonTreeReader);

        assertEquals("testElement", result);

        // stackSize should be decremented to 0
        int stackSize = (int) getField(jsonTreeReader, "stackSize");
        assertEquals(0, stackSize);

        // stack[0] should be set to null after pop
        Object[] updatedStack = (Object[]) getField(jsonTreeReader, "stack");
        assertNull(updatedStack[0]);
    }

    @Test
    @Timeout(8000)
    public void testPopStack_withMultipleElements_shouldPopLastElement() throws Exception {
        Object[] stack = new Object[32];
        stack[0] = "first";
        stack[1] = "second";
        stack[2] = "third";
        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 3);

        Object result = popStackMethod.invoke(jsonTreeReader);

        assertEquals("third", result);

        int stackSize = (int) getField(jsonTreeReader, "stackSize");
        assertEquals(2, stackSize);

        Object[] updatedStack = (Object[]) getField(jsonTreeReader, "stack");
        assertNull(updatedStack[2]);
        assertEquals("first", updatedStack[0]);
        assertEquals("second", updatedStack[1]);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = JsonTreeReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Object getField(Object target, String fieldName) throws Exception {
        java.lang.reflect.Field field = JsonTreeReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}