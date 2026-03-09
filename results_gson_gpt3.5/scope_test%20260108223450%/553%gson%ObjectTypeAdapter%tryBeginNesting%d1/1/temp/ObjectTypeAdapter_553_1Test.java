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

class ObjectTypeAdapter_553_1Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private Method tryBeginNestingMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Use null for Gson and ToNumberStrategy arguments since the constructor is private and we only test private method
        objectTypeAdapter = Mockito.mock(ObjectTypeAdapter.class, Mockito.CALLS_REAL_METHODS);

        // Access private method tryBeginNesting using reflection
        tryBeginNestingMethod = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        tryBeginNestingMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_BeginArray() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);

        // Call private method with BEGIN_ARRAY token
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReader, JsonToken.BEGIN_ARRAY);

        // Verify beginArray is called
        verify(jsonReader).beginArray();

        // Assert result is an ArrayList
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertEquals(0, ((ArrayList<?>) result).size());
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_BeginObject() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);

        // Call private method with BEGIN_OBJECT token
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReader, JsonToken.BEGIN_OBJECT);

        // Verify beginObject is called
        verify(jsonReader).beginObject();

        // Assert result is a LinkedTreeMap
        assertNotNull(result);
        assertTrue(result instanceof LinkedTreeMap);
        assertEquals(0, ((LinkedTreeMap<?, ?>) result).size());
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_Default() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);

        // Test with a token that is neither BEGIN_ARRAY nor BEGIN_OBJECT
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReader, JsonToken.STRING);

        // Verify no interaction with beginArray or beginObject
        verify(jsonReader, never()).beginArray();
        verify(jsonReader, never()).beginObject();

        // Assert result is null
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_ThrowsIOException() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        doThrow(new IOException("Test IO Exception")).when(jsonReader).beginArray();

        // InvocationTargetException expected because IOException is wrapped
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReader, JsonToken.BEGIN_ARRAY);
        });

        // Cause should be IOException
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("Test IO Exception", thrown.getCause().getMessage());
    }
}