package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class ObjectTypeAdapter_553_3Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private JsonReader jsonReaderMock;
    private Method tryBeginNestingMethod;

    @BeforeEach
    void setUp() throws Throwable {
        jsonReaderMock = mock(JsonReader.class);

        // Use reflection to instantiate ObjectTypeAdapter since its constructor is private
        Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, com.google.gson.ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = constructor.newInstance(null, null);

        tryBeginNestingMethod = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        tryBeginNestingMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_BeginArray() throws Throwable {
        JsonToken token = JsonToken.BEGIN_ARRAY;

        Object result = invokeTryBeginNesting(jsonReaderMock, token);

        verify(jsonReaderMock).beginArray();
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_BeginObject() throws Throwable {
        JsonToken token = JsonToken.BEGIN_OBJECT;

        Object result = invokeTryBeginNesting(jsonReaderMock, token);

        verify(jsonReaderMock).beginObject();
        assertTrue(result instanceof LinkedTreeMap);
        assertTrue(((LinkedTreeMap<?, ?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_Default() throws Throwable {
        // Test with a token that is not BEGIN_ARRAY or BEGIN_OBJECT
        JsonToken token = JsonToken.STRING;

        Object result = invokeTryBeginNesting(jsonReaderMock, token);

        verify(jsonReaderMock, never()).beginArray();
        verify(jsonReaderMock, never()).beginObject();
        assertNull(result);
    }

    private Object invokeTryBeginNesting(JsonReader in, JsonToken peeked) throws Throwable {
        try {
            return tryBeginNestingMethod.invoke(objectTypeAdapter, in, peeked);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}