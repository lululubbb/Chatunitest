package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ObjectTypeAdapter_553_2Test {

    private ObjectTypeAdapter objectTypeAdapter;

    @BeforeEach
    public void setUp() throws Exception {
        // Use reflection to instantiate ObjectTypeAdapter since its constructor is private
        var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(com.google.gson.Gson.class, com.google.gson.ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = constructor.newInstance(null, null);
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_BeginArray() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonReader jsonReader = mock(JsonReader.class);
        doNothing().when(jsonReader).beginArray();

        Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        method.setAccessible(true);

        Object result = method.invoke(objectTypeAdapter, jsonReader, JsonToken.BEGIN_ARRAY);

        verify(jsonReader).beginArray();
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_BeginObject() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonReader jsonReader = mock(JsonReader.class);
        doNothing().when(jsonReader).beginObject();

        Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        method.setAccessible(true);

        Object result = method.invoke(objectTypeAdapter, jsonReader, JsonToken.BEGIN_OBJECT);

        verify(jsonReader).beginObject();
        assertNotNull(result);
        assertTrue(result instanceof LinkedTreeMap);
        assertTrue(((LinkedTreeMap<?, ?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_Default() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonReader jsonReader = mock(JsonReader.class);

        Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        method.setAccessible(true);

        // Use a token that is neither BEGIN_ARRAY nor BEGIN_OBJECT
        Object result = method.invoke(objectTypeAdapter, jsonReader, JsonToken.STRING);

        // No beginArray or beginObject should be called
        verify(jsonReader, never()).beginArray();
        verify(jsonReader, never()).beginObject();
        assertNull(result);
    }
}