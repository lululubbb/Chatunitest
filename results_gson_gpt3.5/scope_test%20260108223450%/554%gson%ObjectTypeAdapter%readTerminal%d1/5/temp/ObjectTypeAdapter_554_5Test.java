package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
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

import com.google.gson.ToNumberStrategy;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ObjectTypeAdapter_554_5Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private ToNumberStrategy toNumberStrategyMock;

    @BeforeEach
    void setUp() throws Exception {
        toNumberStrategyMock = mock(ToNumberStrategy.class);
        // Use reflection to invoke private constructor
        var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(com.google.gson.Gson.class, ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = (ObjectTypeAdapter) constructor.newInstance(null, toNumberStrategyMock);
    }

    private Object invokeReadTerminal(JsonReader in, JsonToken token) throws Throwable {
        Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
        method.setAccessible(true);
        try {
            return method.invoke(objectTypeAdapter, in, token);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testReadTerminal_string() throws Throwable {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn("testString");

        Object result = invokeReadTerminal(in, JsonToken.STRING);

        verify(in).nextString();
        assertEquals("testString", result);
    }

    @Test
    @Timeout(8000)
    void testReadTerminal_number() throws Throwable, IOException {
        JsonReader in = mock(JsonReader.class);
        Number numberValue = 42.0;
        when(toNumberStrategyMock.readNumber(in)).thenReturn(numberValue);

        Object result = invokeReadTerminal(in, JsonToken.NUMBER);

        verify(toNumberStrategyMock).readNumber(in);
        assertEquals(numberValue, result);
    }

    @Test
    @Timeout(8000)
    void testReadTerminal_boolean() throws Throwable {
        JsonReader in = mock(JsonReader.class);
        when(in.nextBoolean()).thenReturn(true);

        Object result = invokeReadTerminal(in, JsonToken.BOOLEAN);

        verify(in).nextBoolean();
        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    void testReadTerminal_null() throws Throwable {
        JsonReader in = mock(JsonReader.class);
        doNothing().when(in).nextNull();

        Object result = invokeReadTerminal(in, JsonToken.NULL);

        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testReadTerminal_unexpectedToken() {
        JsonReader in = mock(JsonReader.class);

        JsonToken unexpectedToken = JsonToken.BEGIN_ARRAY;

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> invokeReadTerminal(in, unexpectedToken));
        assertTrue(thrown.getMessage().contains("Unexpected token"));
    }
}