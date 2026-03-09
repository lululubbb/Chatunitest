package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayDeque;
import java.util.Deque;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ObjectTypeAdapter_555_2Test {

    private Gson gson;
    private ToNumberStrategy toNumberStrategy;
    private ObjectTypeAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        gson = mock(Gson.class);
        toNumberStrategy = ToNumberPolicy.DOUBLE;
        adapter = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class)
                .newInstance(gson, toNumberStrategy);
    }

    @Test
    @Timeout(8000)
    void read_terminalValueNull() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek()).thenReturn(JsonToken.NULL);
        doAnswer(invocation -> {
            // simulate reading null
            return null;
        }).when(in).nextNull();

        // Use reflection to mock readTerminal to return a terminal value
        Object result = adapter.read(in);

        verify(in).peek();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_terminalValueBooleanTrue() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek()).thenReturn(JsonToken.BOOLEAN);
        when(in.nextBoolean()).thenReturn(true);

        // Use reflection to call read to test boolean true
        Object result = adapter.read(in);

        verify(in).peek();
        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    void read_terminalValueBooleanFalse() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek()).thenReturn(JsonToken.BOOLEAN);
        when(in.nextBoolean()).thenReturn(false);

        Object result = adapter.read(in);

        verify(in).peek();
        assertEquals(false, result);
    }

    @Test
    @Timeout(8000)
    void read_terminalValueString() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("stringValue");

        Object result = adapter.read(in);

        verify(in).peek();
        assertEquals("stringValue", result);
    }

    @Test
    @Timeout(8000)
    void read_terminalValueNumber() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek()).thenReturn(JsonToken.NUMBER);
        when(in.nextString()).thenReturn("123");
        // toNumberStrategy is DOUBLE so parse to Double
        Object result = adapter.read(in);

        verify(in).peek();
        assertEquals(123.0d, result);
    }

    @Test
    @Timeout(8000)
    void read_emptyArray() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek())
                .thenReturn(JsonToken.BEGIN_ARRAY) // first peek: start array
                .thenReturn(JsonToken.END_ARRAY);  // second peek: end array

        when(in.hasNext())
                .thenReturn(false); // no elements inside array

        doNothing().when(in).endArray();

        Object result = adapter.read(in);

        assertTrue(result instanceof List);
        assertTrue(((List<?>) result).isEmpty());

        verify(in).endArray();
    }

    @Test
    @Timeout(8000)
    void read_arrayWithPrimitives() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek())
                .thenReturn(JsonToken.BEGIN_ARRAY) // start array
                .thenReturn(JsonToken.NUMBER)      // first element number
                .thenReturn(JsonToken.STRING)      // second element string
                .thenReturn(JsonToken.BOOLEAN)     // third element boolean
                .thenReturn(JsonToken.END_ARRAY);  // end array

        when(in.hasNext())
                .thenReturn(true, true, true, false);

        when(in.nextName()).thenThrow(new IllegalStateException("nextName() should not be called for arrays"));

        when(in.nextString())
                .thenReturn("1")  // for number token, nextString returns string number
                .thenReturn("stringValue");

        when(in.nextBoolean())
                .thenReturn(true);

        doNothing().when(in).endArray();

        Object result = adapter.read(in);

        assertTrue(result instanceof List);
        List<?> list = (List<?>) result;
        assertEquals(3, list.size());
        assertEquals(1.0d, list.get(0));
        assertEquals("stringValue", list.get(1));
        assertEquals(true, list.get(2));

        verify(in).endArray();
    }

    @Test
    @Timeout(8000)
    void read_emptyObject() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek())
                .thenReturn(JsonToken.BEGIN_OBJECT) // start object
                .thenReturn(JsonToken.END_OBJECT);  // end object

        when(in.hasNext())
                .thenReturn(false);

        doNothing().when(in).endObject();

        Object result = adapter.read(in);

        assertTrue(result instanceof Map);
        assertTrue(((Map<?, ?>) result).isEmpty());

        verify(in).endObject();
    }

    @Test
    @Timeout(8000)
    void read_objectWithPrimitives() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek())
                .thenReturn(JsonToken.BEGIN_OBJECT) // start object
                .thenReturn(JsonToken.NUMBER)       // first value number
                .thenReturn(JsonToken.STRING)       // second value string
                .thenReturn(JsonToken.BOOLEAN)      // third value boolean
                .thenReturn(JsonToken.END_OBJECT);  // end object

        when(in.hasNext())
                .thenReturn(true, true, true, false);

        when(in.nextName())
                .thenReturn("num")
                .thenReturn("str")
                .thenReturn("bool");

        when(in.nextString())
                .thenReturn("1")  // for number token, nextString returns string number
                .thenReturn("stringValue");

        when(in.nextBoolean())
                .thenReturn(true);

        doNothing().when(in).endObject();

        Object result = adapter.read(in);

        assertTrue(result instanceof Map);
        Map<?, ?> map = (Map<?, ?>) result;
        assertEquals(3, map.size());
        assertEquals(1.0d, map.get("num"));
        assertEquals("stringValue", map.get("str"));
        assertEquals(true, map.get("bool"));

        verify(in).endObject();
    }

    @Test
    @Timeout(8000)
    void read_nestedObjectsAndArrays() throws IOException {
        JsonReader in = mock(JsonReader.class);

        // Simulate JSON: { "array": [1, 2], "obj": {"key": "value"} }
        when(in.peek())
                // Outer object start
                .thenReturn(JsonToken.BEGIN_OBJECT)
                // first member name "array"
                .thenReturn(JsonToken.BEGIN_ARRAY)
                // array elements
                .thenReturn(JsonToken.NUMBER)
                .thenReturn(JsonToken.NUMBER)
                .thenReturn(JsonToken.END_ARRAY)
                // second member name "obj"
                .thenReturn(JsonToken.BEGIN_OBJECT)
                // inner object member
                .thenReturn(JsonToken.STRING)
                .thenReturn(JsonToken.END_OBJECT)
                // end outer object
                .thenReturn(JsonToken.END_OBJECT);

        when(in.hasNext())
                // outer object has 2 members
                .thenReturn(true, true, false)
                // array has 2 elements
                .thenReturn(true, true, false)
                // inner object has 1 member
                .thenReturn(true, false);

        when(in.nextName())
                // outer object member names
                .thenReturn("array")
                .thenReturn("obj")
                // inner object member name
                .thenReturn("key");

        when(in.nextString())
                // inner object member value
                .thenReturn("value");

        when(in.nextBoolean()).thenThrow(new IllegalStateException("No booleans here"));
        when(in.nextLong()).thenThrow(new IllegalStateException("No longs here"));

        doNothing().when(in).endArray();
        doNothing().when(in).endObject();

        // For numbers, nextString returns string numbers
        when(in.nextString())
                .thenReturn("1")
                .thenReturn("2")
                .thenReturn("value");

        Object result = adapter.read(in);

        assertTrue(result instanceof Map);
        Map<?, ?> outer = (Map<?, ?>) result;

        assertTrue(outer.get("array") instanceof List);
        List<?> array = (List<?>) outer.get("array");
        assertEquals(2, array.size());
        assertEquals(1.0d, array.get(0));
        assertEquals(2.0d, array.get(1));

        assertTrue(outer.get("obj") instanceof Map);
        Map<?, ?> inner = (Map<?, ?>) outer.get("obj");
        assertEquals("value", inner.get("key"));

        verify(in, atLeastOnce()).endArray();
        verify(in, atLeastOnce()).endObject();
    }
}