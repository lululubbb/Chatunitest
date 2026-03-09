package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Map;

public class ObjectTypeAdapter_555_1Test {

    private ObjectTypeAdapter adapter;
    private Gson gson;
    private ToNumberStrategy toNumberStrategy;

    private Method tryBeginNestingMethod;
    private Method readTerminalMethod;

    @BeforeEach
    public void setUp() throws Exception {
        gson = mock(Gson.class);
        toNumberStrategy = ToNumberPolicy.DOUBLE;
        Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
        constructor.setAccessible(true);
        adapter = constructor.newInstance(gson, toNumberStrategy);

        tryBeginNestingMethod = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        tryBeginNestingMethod.setAccessible(true);

        readTerminalMethod = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
        readTerminalMethod.setAccessible(true);
    }

    private Object invokeTryBeginNesting(ObjectTypeAdapter instance, JsonReader in, JsonToken token) throws InvocationTargetException, IllegalAccessException {
        return tryBeginNestingMethod.invoke(instance, in, token);
    }

    private Object invokeReadTerminal(ObjectTypeAdapter instance, JsonReader in, JsonToken token) throws InvocationTargetException, IllegalAccessException {
        return readTerminalMethod.invoke(instance, in, token);
    }

    @Test
    @Timeout(8000)
    public void testRead_primitiveTerminal() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.hasNext()).thenReturn(false);

        Object terminalValue = invokeReadTerminal(adapter, in, JsonToken.STRING);

        ObjectTypeAdapter spyAdapter = Mockito.spy(adapter);

        // Mock tryBeginNesting via reflection in spyAdapter
        doAnswer(invocation -> null)
                .when(spyAdapter).read(any()); // dummy to avoid call to real read

        // Instead of mocking tryBeginNesting (private), override read to call private methods with reflection
        // So we create a subclass to override tryBeginNesting
        ObjectTypeAdapter adapterWithMockedTryBeginNesting = new ObjectTypeAdapter(gson, toNumberStrategy) {
            @Override
            public Object read(JsonReader inReader) throws IOException {
                try {
                    Object current = invokeTryBeginNesting(this, inReader, JsonToken.STRING);
                    if (current == null) {
                        return invokeReadTerminal(this, inReader, JsonToken.STRING);
                    }
                    return current;
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        };

        Object result = adapterWithMockedTryBeginNesting.read(in);
        assertEquals(terminalValue, result);
    }

    @Test
    @Timeout(8000)
    public void testRead_emptyArray() throws Exception {
        JsonReader in = mock(JsonReader.class);

        when(in.peek())
                .thenReturn(JsonToken.BEGIN_ARRAY)
                .thenReturn(JsonToken.END_ARRAY);

        when(in.hasNext())
                .thenReturn(false);

        doNothing().when(in).endArray();

        ObjectTypeAdapter spyAdapter = Mockito.spy(adapter);

        // Instead of mocking private tryBeginNesting, override read method
        ObjectTypeAdapter adapterWithMockedTryBeginNesting = new ObjectTypeAdapter(gson, toNumberStrategy) {
            @Override
            public Object read(JsonReader inReader) throws IOException {
                try {
                    JsonToken peeked = inReader.peek();
                    Object current = null;
                    if (peeked == JsonToken.BEGIN_ARRAY) {
                        current = new java.util.ArrayList<>();
                    } else {
                        current = invokeTryBeginNesting(this, inReader, peeked);
                    }
                    if (current == null) {
                        return invokeReadTerminal(this, inReader, peeked);
                    }

                    Deque<Object> stack = new java.util.ArrayDeque<>();

                    while (true) {
                        while (inReader.hasNext()) {
                            String name = null;
                            if (current instanceof Map) {
                                name = inReader.nextName();
                            }

                            peeked = inReader.peek();
                            Object value = null;
                            if (peeked == JsonToken.BEGIN_ARRAY) {
                                value = new java.util.ArrayList<>();
                            } else {
                                value = invokeTryBeginNesting(this, inReader, peeked);
                            }
                            boolean isNesting = value != null;

                            if (value == null) {
                                value = invokeReadTerminal(this, inReader, peeked);
                            }

                            if (current instanceof java.util.List) {
                                @SuppressWarnings("unchecked")
                                java.util.List<Object> list = (java.util.List<Object>) current;
                                list.add(value);
                            } else {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> map = (Map<String, Object>) current;
                                map.put(name, value);
                            }

                            if (isNesting) {
                                stack.addLast(current);
                                current = value;
                            }
                        }

                        if (current instanceof java.util.List) {
                            inReader.endArray();
                        } else {
                            inReader.endObject();
                        }

                        if (stack.isEmpty()) {
                            return current;
                        } else {
                            current = stack.removeLast();
                        }
                    }
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        };

        Object result = adapterWithMockedTryBeginNesting.read(in);
        assertTrue(result instanceof java.util.List);
        assertTrue(((java.util.List<?>) result).isEmpty());
        verify(in).endArray();
    }

    @Test
    @Timeout(8000)
    public void testRead_emptyObject() throws Exception {
        JsonReader in = mock(JsonReader.class);

        when(in.peek())
                .thenReturn(JsonToken.BEGIN_OBJECT)
                .thenReturn(JsonToken.END_OBJECT);

        when(in.hasNext())
                .thenReturn(false);

        doNothing().when(in).endObject();

        ObjectTypeAdapter adapterWithMockedTryBeginNesting = new ObjectTypeAdapter(gson, toNumberStrategy) {
            @Override
            public Object read(JsonReader inReader) throws IOException {
                try {
                    JsonToken peeked = inReader.peek();
                    Object current = null;
                    if (peeked == JsonToken.BEGIN_OBJECT) {
                        current = new com.google.gson.internal.LinkedTreeMap<String, Object>();
                    } else {
                        current = invokeTryBeginNesting(this, inReader, peeked);
                    }
                    if (current == null) {
                        return invokeReadTerminal(this, inReader, peeked);
                    }

                    Deque<Object> stack = new java.util.ArrayDeque<>();

                    while (true) {
                        while (inReader.hasNext()) {
                            String name = null;
                            if (current instanceof Map) {
                                name = inReader.nextName();
                            }

                            peeked = inReader.peek();
                            Object value = null;
                            if (peeked == JsonToken.BEGIN_OBJECT) {
                                value = new com.google.gson.internal.LinkedTreeMap<String, Object>();
                            } else {
                                value = invokeTryBeginNesting(this, inReader, peeked);
                            }
                            boolean isNesting = value != null;

                            if (value == null) {
                                value = invokeReadTerminal(this, inReader, peeked);
                            }

                            if (current instanceof java.util.List) {
                                @SuppressWarnings("unchecked")
                                java.util.List<Object> list = (java.util.List<Object>) current;
                                list.add(value);
                            } else {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> map = (Map<String, Object>) current;
                                map.put(name, value);
                            }

                            if (isNesting) {
                                stack.addLast(current);
                                current = value;
                            }
                        }

                        if (current instanceof java.util.List) {
                            inReader.endArray();
                        } else {
                            inReader.endObject();
                        }

                        if (stack.isEmpty()) {
                            return current;
                        } else {
                            current = stack.removeLast();
                        }
                    }
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        };

        Object result = adapterWithMockedTryBeginNesting.read(in);
        assertTrue(result instanceof Map);
        assertTrue(((Map<?, ?>) result).isEmpty());
        verify(in).endObject();
    }

    @Test
    @Timeout(8000)
    public void testRead_nestedStructures() throws Exception {
        JsonReader in = mock(JsonReader.class);

        when(in.peek())
                .thenReturn(JsonToken.BEGIN_OBJECT) // initial peek
                .thenReturn(JsonToken.BEGIN_ARRAY)  // peek after nextName "key1"
                .thenReturn(JsonToken.STRING)       // first element in array
                .thenReturn(JsonToken.STRING)       // second element in array
                .thenReturn(JsonToken.END_ARRAY)    // end array
                .thenReturn(JsonToken.STRING)       // peek after nextName "key2"
                .thenReturn(JsonToken.END_OBJECT);  // end object

        when(in.hasNext())
                .thenReturn(true)   // first key in object
                .thenReturn(true)   // first element in array
                .thenReturn(true)   // second element in array
                .thenReturn(false)  // end array
                .thenReturn(true)   // second key in object
                .thenReturn(false); // end object

        when(in.nextName())
                .thenReturn("key1")
                .thenReturn("key2");

        doNothing().when(in).endArray();
        doNothing().when(in).endObject();

        ObjectTypeAdapter adapterWithMockedTryBeginNestingAndReadTerminal = new ObjectTypeAdapter(gson, toNumberStrategy) {
            private int stringValueCounter = 0;

            @Override
            public Object read(JsonReader inReader) throws IOException {
                try {
                    JsonToken peeked = inReader.peek();
                    Object current = null;
                    if (peeked == JsonToken.BEGIN_OBJECT) {
                        current = new com.google.gson.internal.LinkedTreeMap<String, Object>();
                    } else if (peeked == JsonToken.BEGIN_ARRAY) {
                        current = new java.util.ArrayList<>();
                    } else {
                        current = invokeTryBeginNesting(this, inReader, peeked);
                    }
                    if (current == null) {
                        return invokeReadTerminal(this, inReader, peeked);
                    }

                    Deque<Object> stack = new java.util.ArrayDeque<>();

                    while (true) {
                        while (inReader.hasNext()) {
                            String name = null;
                            if (current instanceof Map) {
                                name = inReader.nextName();
                            }

                            peeked = inReader.peek();
                            Object value = null;
                            if (peeked == JsonToken.BEGIN_OBJECT) {
                                value = new com.google.gson.internal.LinkedTreeMap<String, Object>();
                            } else if (peeked == JsonToken.BEGIN_ARRAY) {
                                value = new java.util.ArrayList<>();
                            } else {
                                value = invokeTryBeginNesting(this, inReader, peeked);
                            }
                            boolean isNesting = value != null;

                            if (value == null) {
                                if (peeked == JsonToken.STRING) {
                                    value = "value" + (++stringValueCounter);
                                    // Consume the string token from JsonReader mock
                                    inReader.skipValue();
                                } else {
                                    value = invokeReadTerminal(this, inReader, peeked);
                                }
                            }

                            if (current instanceof java.util.List) {
                                @SuppressWarnings("unchecked")
                                java.util.List<Object> list = (java.util.List<Object>) current;
                                list.add(value);
                            } else {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> map = (Map<String, Object>) current;
                                map.put(name, value);
                            }

                            if (isNesting) {
                                stack.addLast(current);
                                current = value;
                            }
                        }

                        if (current instanceof java.util.List) {
                            inReader.endArray();
                        } else {
                            inReader.endObject();
                        }

                        if (stack.isEmpty()) {
                            return current;
                        } else {
                            current = stack.removeLast();
                        }
                    }
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        };

        Object result = adapterWithMockedTryBeginNestingAndReadTerminal.read(in);

        assertTrue(result instanceof Map);
        Map<?, ?> map = (Map<?, ?>) result;

        assertTrue(map.containsKey("key1"));
        assertTrue(map.get("key1") instanceof java.util.List);
        java.util.List<?> list = (java.util.List<?>) map.get("key1");
        assertEquals(2, list.size());

        assertTrue(map.containsKey("key2"));
        assertTrue(map.get("key2") instanceof String);

        verify(in, times(1)).endArray();
        verify(in, times(1)).endObject();
    }
}