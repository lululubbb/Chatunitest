package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectTypeAdapter_555_3Test {

    private ObjectTypeAdapter adapter;
    private Gson gsonMock;
    private ToNumberStrategy toNumberStrategyMock;

    @BeforeEach
    public void setUp() throws Exception {
        gsonMock = mock(Gson.class);
        toNumberStrategyMock = ToNumberPolicy.DOUBLE; // Use real policy for simplicity

        Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
        constructor.setAccessible(true);
        adapter = constructor.newInstance(gsonMock, toNumberStrategyMock);
    }

    /**
     * Helper to invoke private method tryBeginNesting
     */
    private Object invokeTryBeginNesting(JsonReader in, JsonToken peeked) throws Exception {
        Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        method.setAccessible(true);
        return method.invoke(adapter, in, peeked);
    }

    /**
     * Helper to invoke private method readTerminal
     */
    private Object invokeReadTerminal(JsonReader in, JsonToken peeked) throws Exception {
        Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
        method.setAccessible(true);
        return method.invoke(adapter, in, peeked);
    }

    @Test
    @Timeout(8000)
    public void testRead_nullValue() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(in).nextNull();

        // readTerminal should return null for JsonToken.NULL
        Object result = adapter.read(in);

        verify(in).peek();
        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_primitiveString() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("test string");

        Object result = adapter.read(in);

        verify(in).peek();
        verify(in).nextString();
        assertEquals("test string", result);
    }

    @Test
    @Timeout(8000)
    public void testRead_primitiveBooleanTrue() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.BOOLEAN);
        when(in.nextBoolean()).thenReturn(true);

        Object result = adapter.read(in);

        verify(in).peek();
        verify(in).nextBoolean();
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    @Timeout(8000)
    public void testRead_primitiveBooleanFalse() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.BOOLEAN);
        when(in.nextBoolean()).thenReturn(false);

        Object result = adapter.read(in);

        verify(in).peek();
        verify(in).nextBoolean();
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    @Timeout(8000)
    public void testRead_primitiveNumber() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NUMBER);
        when(in.nextString()).thenReturn("123.45");

        // Using real ToNumberPolicy.DOUBLE, so number is Double
        Object result = adapter.read(in);

        verify(in).peek();
        verify(in).nextString();
        assertTrue(result instanceof Number);
        assertEquals(123.45d, ((Number) result).doubleValue());
    }

    @Test
    @Timeout(8000)
    public void testRead_emptyArray() throws Exception {
        JsonReader in = mock(JsonReader.class);

        // Simulate: peek() returns BEGIN_ARRAY, tryBeginNesting returns a List, then hasNext false, endArray called
        when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
        // Mock tryBeginNesting to return new ArrayList when called with BEGIN_ARRAY
        // We cannot mock private method directly, so we rely on actual implementation

        // Setup hasNext to false to simulate empty array
        when(in.hasNext()).thenReturn(false);
        doNothing().when(in).endArray();

        Object result = adapter.read(in);

        verify(in).peek();
        verify(in).hasNext();
        verify(in).endArray();

        assertTrue(result instanceof List);
        assertTrue(((List<?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testRead_emptyObject() throws Exception {
        JsonReader in = mock(JsonReader.class);

        // Simulate: peek() returns BEGIN_OBJECT, tryBeginNesting returns a Map, hasNext false, endObject called
        when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
        when(in.hasNext()).thenReturn(false);
        doNothing().when(in).endObject();

        Object result = adapter.read(in);

        verify(in).peek();
        verify(in).hasNext();
        verify(in).endObject();

        assertTrue(result instanceof Map);
        assertTrue(((Map<?, ?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testRead_nestedArrayAndObject() throws Exception, IOException {
        JsonReader in = mock(JsonReader.class);

        // Setup a nested JSON: [{ "key": "value" }]
        // Simulate peek() and hasNext() calls to drive the nested structure

        // Sequence of peek calls:
        // 1: BEGIN_ARRAY
        // 2: BEGIN_OBJECT
        // 3: NAME
        // 4: STRING
        // 5: END_OBJECT
        // 6: END_ARRAY

        // We will simulate calls in order using Mockito's thenReturn with multiple values

        when(in.peek())
                .thenReturn(JsonToken.BEGIN_ARRAY)   // outer array start
                .thenReturn(JsonToken.BEGIN_OBJECT)  // object start inside array
                .thenReturn(JsonToken.NAME)          // name inside object
                .thenReturn(JsonToken.STRING)        // value inside object
                .thenReturn(JsonToken.END_OBJECT)    // end object
                .thenReturn(JsonToken.END_ARRAY);    // end array

        // hasNext returns true for array and object elements, then false when done
        when(in.hasNext())
                .thenReturn(true)  // for array: has one element
                .thenReturn(true)  // for object: has one member
                .thenReturn(false) // for object: no more members
                .thenReturn(false); // for array: no more elements

        // nextName returns "key"
        when(in.nextName()).thenReturn("key");

        // nextString returns "value"
        when(in.nextString()).thenReturn("value");

        // endObject and endArray do nothing
        doNothing().when(in).endObject();
        doNothing().when(in).endArray();

        Object result = adapter.read(in);

        // Verify interactions
        InOrder inOrder = inOrder(in);
        inOrder.verify(in).peek(); // BEGIN_ARRAY
        inOrder.verify(in).hasNext(); // true
        inOrder.verify(in).nextName(); // "key" for object member
        inOrder.verify(in).peek(); // BEGIN_OBJECT or NAME
        inOrder.verify(in).peek(); // STRING
        inOrder.verify(in).hasNext(); // true for object member
        inOrder.verify(in).hasNext(); // false for object members end
        inOrder.verify(in).endObject();
        inOrder.verify(in).hasNext(); // false for array elements end
        inOrder.verify(in).endArray();

        assertTrue(result instanceof List);
        List<?> outerList = (List<?>) result;
        assertEquals(1, outerList.size());
        Object innerObject = outerList.get(0);
        assertTrue(innerObject instanceof Map);
        Map<?, ?> innerMap = (Map<?, ?>) innerObject;
        assertEquals(1, innerMap.size());
        assertEquals("value", innerMap.get("key"));
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_returnsNullOnNonNestingTokens() throws Exception {
        JsonReader in = mock(JsonReader.class);

        // Tokens that are terminal: STRING, NUMBER, BOOLEAN, NULL
        JsonToken[] tokens = {JsonToken.STRING, JsonToken.NUMBER, JsonToken.BOOLEAN, JsonToken.NULL};

        for (JsonToken token : tokens) {
            Object result = invokeTryBeginNesting(in, token);
            assertNull(result, "tryBeginNesting should return null for token: " + token);
        }
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_returnsListForBeginArray() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.beginArray()).thenAnswer(invocation -> null);

        Object result = invokeTryBeginNesting(in, JsonToken.BEGIN_ARRAY);

        assertNotNull(result);
        assertTrue(result instanceof List);
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_returnsMapForBeginObject() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.beginObject()).thenAnswer(invocation -> null);

        Object result = invokeTryBeginNesting(in, JsonToken.BEGIN_OBJECT);

        assertNotNull(result);
        assertTrue(result instanceof Map);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_handlesAllTerminalTokens() throws Exception {
        JsonReader in = mock(JsonReader.class);

        // NULL
        when(in.nextNull()).thenAnswer(invocation -> null);
        Object nullResult = invokeReadTerminal(in, JsonToken.NULL);
        verify(in).nextNull();
        assertNull(nullResult);

        reset(in);

        // BOOLEAN true
        when(in.nextBoolean()).thenReturn(true);
        Object trueResult = invokeReadTerminal(in, JsonToken.BOOLEAN);
        verify(in).nextBoolean();
        assertEquals(Boolean.TRUE, trueResult);

        reset(in);

        // STRING
        when(in.nextString()).thenReturn("stringValue");
        Object stringResult = invokeReadTerminal(in, JsonToken.STRING);
        verify(in).nextString();
        assertEquals("stringValue", stringResult);

        reset(in);

        // NUMBER returns string first, then parsed by toNumberStrategy
        when(in.nextString()).thenReturn("42");
        Object numberResult = invokeReadTerminal(in, JsonToken.NUMBER);
        verify(in).nextString();
        assertTrue(numberResult instanceof Number);

        reset(in);

        // Default fallback: throw IllegalStateException for unknown token
        JsonToken unknownToken = JsonToken.END_DOCUMENT;
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            invokeReadTerminal(in, unknownToken);
        });
        assertTrue(thrown.getMessage().contains("Unexpected token"));
    }
}