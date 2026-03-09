package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class JsonTreeReader_234_3Test {

    private JsonTreeReader reader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a JsonElement to pass to constructor (can be JsonNull for start)
        JsonElement element = JsonNull.INSTANCE;
        reader = new JsonTreeReader(element);

        // Using reflection to set stackSize and stack for testing
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        // Initialize stack array with nulls
        Object[] stackArray = new Object[32];
        stackField.set(reader, stackArray);
        stackSizeField.setInt(reader, 0);
    }

    private void setStack(Object... elements) throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Object[] stackArray = new Object[32];
        System.arraycopy(elements, 0, stackArray, 0, elements.length);
        stackField.set(reader, stackArray);
        stackSizeField.setInt(reader, elements.length);
    }

    private Object peekStack() throws Exception {
        Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
        peekStackMethod.setAccessible(true);
        return peekStackMethod.invoke(reader);
    }

    private void push(Object o) throws Exception {
        Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(reader, o);
    }

    @Test
    @Timeout(8000)
    public void testPeek_stackSizeZero_returnsEndDocument() throws IOException {
        // stackSize = 0 means end of document
        // Already set in setUp()
        JsonToken token = reader.peek();
        assertEquals(JsonToken.END_DOCUMENT, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsIterator_hasNextTrue_isObjectTrue_returnsName() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        Iterator<?> iterator = jsonObject.entrySet().iterator();

        // stack: [jsonObject, iterator]
        setStack(jsonObject, iterator);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NAME, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsIterator_hasNextTrue_isObjectFalse_pushesNextAndRecurses() throws Exception {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("value1"));
        jsonArray.add(new JsonPrimitive("value2"));
        Iterator<?> iterator = jsonArray.iterator();

        // stack: [jsonArray, iterator]
        setStack(jsonArray, iterator);

        // peek should push next element (JsonPrimitive) and recurse, returning STRING token
        JsonToken token = reader.peek();
        assertEquals(JsonToken.STRING, token);

        // After peek, stackSize should be 3
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(reader);
        assertEquals(3, stackSize);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsIterator_hasNextFalse_isObjectTrue_returnsEndObject() throws Exception {
        JsonObject jsonObject = new JsonObject();
        Iterator<?> emptyIterator = Collections.emptyIterator();

        // stack: [jsonObject, emptyIterator]
        setStack(jsonObject, emptyIterator);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsIterator_hasNextFalse_isObjectFalse_returnsEndArray() throws Exception {
        JsonArray jsonArray = new JsonArray();
        Iterator<?> emptyIterator = Collections.emptyIterator();

        // stack: [jsonArray, emptyIterator]
        setStack(jsonArray, emptyIterator);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.END_ARRAY, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsJsonObject_returnsBeginObject() throws Exception {
        JsonObject jsonObject = new JsonObject();

        setStack(jsonObject);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BEGIN_OBJECT, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsJsonArray_returnsBeginArray() throws Exception {
        JsonArray jsonArray = new JsonArray();

        setStack(jsonArray);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BEGIN_ARRAY, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsJsonPrimitive_string() throws Exception {
        JsonPrimitive primitive = new JsonPrimitive("string");

        setStack(primitive);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.STRING, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsJsonPrimitive_boolean() throws Exception {
        JsonPrimitive primitive = new JsonPrimitive(true);

        setStack(primitive);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.BOOLEAN, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsJsonPrimitive_number() throws Exception {
        JsonPrimitive primitive = new JsonPrimitive(123);

        setStack(primitive);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NUMBER, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsJsonPrimitive_other_throwsAssertionError() throws Exception {
        // Create a JsonPrimitive subclass that returns false for all isX methods
        JsonPrimitive primitive = mock(JsonPrimitive.class);
        when(primitive.isString()).thenReturn(false);
        when(primitive.isBoolean()).thenReturn(false);
        when(primitive.isNumber()).thenReturn(false);

        setStack(primitive);

        assertThrows(AssertionError.class, () -> reader.peek());
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsJsonNull_returnsNull() throws Exception {
        setStack(JsonNull.INSTANCE);

        JsonToken token = reader.peek();
        assertEquals(JsonToken.NULL, token);
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsSentinelClosed_throwsIllegalStateException() throws Exception {
        Field sentinelField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
        sentinelField.setAccessible(true);
        Object sentinelClosed = sentinelField.get(null);

        setStack(sentinelClosed);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> reader.peek());
        assertEquals("JsonReader is closed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testPeek_topIsUnsupportedType_throwsMalformedJsonException() throws Exception {
        Object unsupported = new Object();

        setStack(unsupported);

        MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> reader.peek());
        assertTrue(ex.getMessage().startsWith("Custom JsonElement subclass"));
    }
}