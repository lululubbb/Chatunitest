package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_234_1Test {

    private JsonTreeReader reader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonElement to initialize the reader
        JsonElement element = new JsonNull();
        reader = new JsonTreeReader(element);

        // Set stackSize to 0 initially (should be by default)
        setField(reader, "stackSize", 0);
    }

    @Test
    @Timeout(8000)
    public void peek_stackSizeZero_returnsEndDocument() throws IOException {
        setField(reader, "stackSize", 0);
        JsonToken token = reader.peek();
        assertEquals(JsonToken.END_DOCUMENT, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsIterator_hasNextIsTrue_objectIterator_returnsName() throws Exception {
        // Setup an Iterator that has next, and stack[stackSize-2] is JsonObject
        JsonObject jsonObject = new JsonObject();
        Iterator<String> iterator = Collections.singleton("key").iterator();

        // Setup stack with size 2: stack[0] = JsonObject, stack[1] = iterator
        setField(reader, "stackSize", 2);
        setStackElement(0, jsonObject);
        setStackElement(1, iterator);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NAME, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsIterator_hasNextIsTrue_arrayIterator_pushesNextAndRecurses() throws Exception {
        // Setup an Iterator that has next, and stack[stackSize-2] is JsonArray
        JsonArray jsonArray = new JsonArray();
        JsonPrimitive prim = new JsonPrimitive("value");
        Iterator<JsonElement> iterator = Collections.singleton(prim).iterator();

        // Setup stack with size 2: stack[0] = JsonArray, stack[1] = iterator
        setField(reader, "stackSize", 2);
        setStackElement(0, jsonArray);
        setStackElement(1, iterator);

        JsonToken token = reader.peek();
        // The recursive call should push the JsonPrimitive and return STRING token
        assertEquals(JsonToken.STRING, token);

        // After peek, stackSize should be 3 and top should be prim
        int stackSize = (int) getField(reader, "stackSize");
        assertEquals(3, stackSize);
        Object top = getStackTop();
        assertSame(prim, top);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsIterator_hasNextIsFalse_objectIterator_returnsEndObject() throws Exception {
        JsonObject jsonObject = new JsonObject();
        Iterator<?> emptyIterator = Collections.emptyIterator();

        setField(reader, "stackSize", 2);
        setStackElement(0, jsonObject);
        setStackElement(1, emptyIterator);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsIterator_hasNextIsFalse_arrayIterator_returnsEndArray() throws Exception {
        JsonArray jsonArray = new JsonArray();
        Iterator<?> emptyIterator = Collections.emptyIterator();

        setField(reader, "stackSize", 2);
        setStackElement(0, jsonArray);
        setStackElement(1, emptyIterator);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.END_ARRAY, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsJsonObject_returnsBeginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        setField(reader, "stackSize", 1);
        setStackElement(0, jsonObject);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BEGIN_OBJECT, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsJsonArray_returnsBeginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        setField(reader, "stackSize", 1);
        setStackElement(0, jsonArray);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BEGIN_ARRAY, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsJsonPrimitive_string() throws IOException {
        JsonPrimitive primitive = new JsonPrimitive("string");
        setField(reader, "stackSize", 1);
        setStackElement(0, primitive);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.STRING, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsJsonPrimitive_boolean() throws IOException {
        JsonPrimitive primitive = new JsonPrimitive(true);
        setField(reader, "stackSize", 1);
        setStackElement(0, primitive);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BOOLEAN, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsJsonPrimitive_number() throws IOException {
        JsonPrimitive primitive = new JsonPrimitive(123);
        setField(reader, "stackSize", 1);
        setStackElement(0, primitive);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NUMBER, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsJsonPrimitive_other_throwsAssertionError() throws IOException {
        JsonPrimitive primitive = mock(JsonPrimitive.class);
        when(primitive.isString()).thenReturn(false);
        when(primitive.isBoolean()).thenReturn(false);
        when(primitive.isNumber()).thenReturn(false);

        setField(reader, "stackSize", 1);
        setStackElement(0, primitive);

        assertThrows(AssertionError.class, () -> reader.peek());
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsJsonNull_returnsNull() throws IOException {
        JsonNull jsonNull = JsonNull.INSTANCE;
        setField(reader, "stackSize", 1);
        setStackElement(0, jsonNull);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NULL, token);
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsSentinelClosed_throwsIllegalStateException() throws IOException {
        Object sentinelClosed = getStaticField("SENTINEL_CLOSED");
        setField(reader, "stackSize", 1);
        setStackElement(0, sentinelClosed);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> reader.peek());
        assertEquals("JsonReader is closed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void peek_stackTopIsUnsupportedType_throwsMalformedJsonException() throws IOException {
        Object unsupported = new Object();
        setField(reader, "stackSize", 1);
        setStackElement(0, unsupported);

        MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> reader.peek());
        assertTrue(ex.getMessage().contains("Custom JsonElement subclass"));
        assertTrue(ex.getMessage().contains(unsupported.getClass().getName()));
    }

    // Helper methods for reflection and stack manipulation

    private void setStackElement(int index, Object value) throws Exception {
        Object[] stack = (Object[]) getField(reader, "stack");
        stack[index] = value;
    }

    private Object getStackTop() throws Exception {
        int stackSize = (int) getField(reader, "stackSize");
        Object[] stack = (Object[]) getField(reader, "stack");
        return stack[stackSize - 1];
    }

    private Object getField(Object target, String name) throws Exception {
        Field field = JsonTreeReader.class.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field field = JsonTreeReader.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Object getStaticField(String name) throws Exception {
        Field field = JsonTreeReader.class.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(null);
    }
}