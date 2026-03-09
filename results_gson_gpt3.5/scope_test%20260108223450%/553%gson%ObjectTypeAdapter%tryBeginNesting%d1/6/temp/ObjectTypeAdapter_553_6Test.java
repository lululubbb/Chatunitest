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
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ObjectTypeAdapter_553_6Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private Method tryBeginNestingMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        // Create a mock Gson and ToNumberStrategy since constructor is private and not used in test
        objectTypeAdapter = Mockito.mock(ObjectTypeAdapter.class, Mockito.CALLS_REAL_METHODS);

        tryBeginNestingMethod = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        tryBeginNestingMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_beginArray() throws Throwable {
        JsonReader jsonReader = mock(JsonReader.class);

        // Invoke private method with BEGIN_ARRAY token
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReader, JsonToken.BEGIN_ARRAY);

        // Verify beginArray() called on JsonReader
        verify(jsonReader).beginArray();

        // Assert result is an ArrayList instance
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_beginObject() throws Throwable {
        JsonReader jsonReader = mock(JsonReader.class);

        // Invoke private method with BEGIN_OBJECT token
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReader, JsonToken.BEGIN_OBJECT);

        // Verify beginObject() called on JsonReader
        verify(jsonReader).beginObject();

        // Assert result is a LinkedTreeMap instance
        assertNotNull(result);
        assertTrue(result instanceof LinkedTreeMap);
    }

    @Test
    @Timeout(8000)
    public void testTryBeginNesting_default() throws Throwable {
        JsonReader jsonReader = mock(JsonReader.class);

        // Use a token that is neither BEGIN_ARRAY nor BEGIN_OBJECT
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReader, JsonToken.STRING);

        // Verify no interaction with beginArray or beginObject
        verify(jsonReader, never()).beginArray();
        verify(jsonReader, never()).beginObject();

        // Assert result is null
        assertNull(result);
    }
}