package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeReader_234_6Test {

    private JsonTreeReader reader;

    @BeforeEach
    void setUp() throws Exception {
        // Create dummy JsonElement for constructor
        JsonElement element = new JsonNull();
        reader = new JsonTreeReader(element);

        // Set stackSize and stack via reflection
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        // Initialize stack with size 32
        Object[] stackArray = new Object[32];
        stackField.set(reader, stackArray);
        stackSizeField.setInt(reader, 0);
    }

    private void setStack(Object[] stackContents, int size) throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(reader, stackContents);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, size);
    }

    private Object peekStack() throws Exception {
        Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
        peekStackMethod.setAccessible(true);
        return peekStackMethod.invoke(reader);
    }

    private void push(Object newTop) throws Exception {
        Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(reader, newTop);
    }

    @Test
    @Timeout(8000)
    void peek_stackSizeZero_returnsEndDocument() throws IOException {
        // stackSize = 0
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsIterator_hasNextTrue_isObjectTrue_returnsName() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        Iterator<?> iterator = jsonObject.entrySet().iterator();

        Object[] stack = new Object[32];
        stack[0] = iterator;
        stack[1] = jsonObject;
        setStack(stack, 2);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NAME, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsIterator_hasNextTrue_isObjectFalse_pushesNextAndRecurses() throws Exception {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("elem1"));
        jsonArray.add(new JsonPrimitive("elem2"));
        Iterator<?> iterator = jsonArray.iterator();

        Object[] stack = new Object[32];
        stack[0] = iterator;
        stack[1] = jsonArray;
        setStack(stack, 2);

        // The first call should push "elem1" and recurse
        JsonToken token = reader.peek();
        // After recursion, top of stack should be JsonPrimitive "elem1"
        Object top = peekStack();
        assertTrue(top instanceof JsonPrimitive);
        assertEquals("elem1", ((JsonPrimitive) top).getAsString());
        // The token should be STRING because elem1 is a string primitive
        assertEquals(JsonToken.STRING, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsIterator_hasNextFalse_isObjectTrue_returnsEndObject() throws Exception {
        JsonObject jsonObject = new JsonObject();
        Iterator<?> emptyIterator = Collections.emptyIterator();

        Object[] stack = new Object[32];
        stack[0] = emptyIterator;
        stack[1] = jsonObject;
        setStack(stack, 2);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsIterator_hasNextFalse_isObjectFalse_returnsEndArray() throws Exception {
        JsonArray jsonArray = new JsonArray();
        Iterator<?> emptyIterator = Collections.emptyIterator();

        Object[] stack = new Object[32];
        stack[0] = emptyIterator;
        stack[1] = jsonArray;
        setStack(stack, 2);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.END_ARRAY, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsJsonObject_returnsBeginObject() throws Exception {
        JsonObject jsonObject = new JsonObject();

        Object[] stack = new Object[32];
        stack[0] = jsonObject;
        setStack(stack, 1);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BEGIN_OBJECT, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsJsonArray_returnsBeginArray() throws Exception {
        JsonArray jsonArray = new JsonArray();

        Object[] stack = new Object[32];
        stack[0] = jsonArray;
        setStack(stack, 1);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BEGIN_ARRAY, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsJsonPrimitive_string_returnsStringToken() throws Exception {
        JsonPrimitive primitive = new JsonPrimitive("stringValue");

        Object[] stack = new Object[32];
        stack[0] = primitive;
        setStack(stack, 1);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.STRING, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsJsonPrimitive_boolean_returnsBooleanToken() throws Exception {
        JsonPrimitive primitive = new JsonPrimitive(true);

        Object[] stack = new Object[32];
        stack[0] = primitive;
        setStack(stack, 1);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BOOLEAN, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsJsonPrimitive_number_returnsNumberToken() throws Exception {
        JsonPrimitive primitive = new JsonPrimitive(123);

        Object[] stack = new Object[32];
        stack[0] = primitive;
        setStack(stack, 1);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NUMBER, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsJsonPrimitive_other_throwsAssertionError() throws Exception {
        JsonPrimitive primitive = Mockito.mock(JsonPrimitive.class);
        Mockito.when(primitive.isString()).thenReturn(false);
        Mockito.when(primitive.isBoolean()).thenReturn(false);
        Mockito.when(primitive.isNumber()).thenReturn(false);

        Object[] stack = new Object[32];
        stack[0] = primitive;
        setStack(stack, 1);

        assertThrows(AssertionError.class, () -> reader.peek());
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsJsonNull_returnsNullToken() throws Exception {
        JsonNull jsonNull = JsonNull.INSTANCE;

        Object[] stack = new Object[32];
        stack[0] = jsonNull;
        setStack(stack, 1);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NULL, token);
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsSentinelClosed_throwsIllegalStateException() throws Exception {
        Field sentinelField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
        sentinelField.setAccessible(true);
        Object sentinel = sentinelField.get(null);

        Object[] stack = new Object[32];
        stack[0] = sentinel;
        setStack(stack, 1);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> reader.peek());
        assertEquals("JsonReader is closed", ex.getMessage());
    }

    private static class CustomJsonElement extends JsonElement {
        // No additional implementation
    }

    @Test
    @Timeout(8000)
    void peek_stackTopIsCustomJsonElement_throwsMalformedJsonException() throws Exception {
        CustomJsonElement customElement = new CustomJsonElement();

        Object[] stack = new Object[32];
        stack[0] = customElement;
        setStack(stack, 1);

        MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> reader.peek());
        assertTrue(ex.getMessage().startsWith("Custom JsonElement subclass"));
        assertTrue(ex.getMessage().contains(CustomJsonElement.class.getName()));
    }
}