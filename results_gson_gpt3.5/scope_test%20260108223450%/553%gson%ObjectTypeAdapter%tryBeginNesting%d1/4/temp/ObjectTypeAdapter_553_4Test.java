package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
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

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectTypeAdapter_553_4Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private JsonReader jsonReaderMock;
    private Method tryBeginNestingMethod;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to get access to private constructor and instantiate
        try {
            Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, com.google.gson.ToNumberStrategy.class);
            constructor.setAccessible(true);
            objectTypeAdapter = constructor.newInstance(null, null);
        } catch (Exception e) {
            fail("Failed to instantiate ObjectTypeAdapter: " + e.getMessage());
        }

        jsonReaderMock = mock(JsonReader.class);
        tryBeginNestingMethod = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        tryBeginNestingMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_beginArray() throws Throwable {
        // Arrange
        JsonToken token = JsonToken.BEGIN_ARRAY;

        // Act
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReaderMock, token);

        // Assert
        verify(jsonReaderMock, times(1)).beginArray();
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_beginObject() throws Throwable {
        // Arrange
        JsonToken token = JsonToken.BEGIN_OBJECT;

        // Act
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReaderMock, token);

        // Assert
        verify(jsonReaderMock, times(1)).beginObject();
        assertNotNull(result);
        assertTrue(result instanceof LinkedTreeMap);
        assertTrue(((LinkedTreeMap<?, ?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_default() throws Throwable {
        // Arrange
        JsonToken token = JsonToken.STRING; // any token other than BEGIN_ARRAY or BEGIN_OBJECT

        // Act
        Object result = tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReaderMock, token);

        // Assert
        verify(jsonReaderMock, never()).beginArray();
        verify(jsonReaderMock, never()).beginObject();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testTryBeginNesting_throwsIOException() throws Throwable {
        // Arrange
        JsonToken token = JsonToken.BEGIN_ARRAY;
        doThrow(new IOException("Test IOException")).when(jsonReaderMock).beginArray();

        // Act & Assert
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            tryBeginNestingMethod.invoke(objectTypeAdapter, jsonReaderMock, token);
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("Test IOException", thrown.getCause().getMessage());
    }
}