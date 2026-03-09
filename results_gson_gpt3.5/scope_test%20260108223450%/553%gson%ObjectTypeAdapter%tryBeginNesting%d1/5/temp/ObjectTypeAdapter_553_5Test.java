package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
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

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class ObjectTypeAdapter_553_5Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private JsonReader jsonReaderMock;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to access private constructor
        var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(com.google.gson.Gson.class, com.google.gson.ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = constructor.newInstance(null, null);

        jsonReaderMock = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    void tryBeginNesting_withBeginArray_invokesBeginArrayAndReturnsArrayList() throws Throwable {
        // Arrange
        JsonToken peeked = JsonToken.BEGIN_ARRAY;

        // Act
        Object result = invokeTryBeginNesting(jsonReaderMock, peeked);

        // Assert
        verify(jsonReaderMock).beginArray();
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    void tryBeginNesting_withBeginObject_invokesBeginObjectAndReturnsLinkedTreeMap() throws Throwable {
        // Arrange
        JsonToken peeked = JsonToken.BEGIN_OBJECT;

        // Act
        Object result = invokeTryBeginNesting(jsonReaderMock, peeked);

        // Assert
        verify(jsonReaderMock).beginObject();
        assertNotNull(result);
        assertTrue(result instanceof LinkedTreeMap);
        assertTrue(((LinkedTreeMap<?, ?>) result).isEmpty());
    }

    @Test
    @Timeout(8000)
    void tryBeginNesting_withOtherToken_returnsNull() throws Throwable {
        // Arrange
        JsonToken peeked = JsonToken.STRING; // any token other than BEGIN_ARRAY or BEGIN_OBJECT

        // Act
        Object result = invokeTryBeginNesting(jsonReaderMock, peeked);

        // Assert
        verify(jsonReaderMock, never()).beginArray();
        verify(jsonReaderMock, never()).beginObject();
        assertNull(result);
    }

    private Object invokeTryBeginNesting(JsonReader in, JsonToken peeked) throws Throwable {
        Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
        method.setAccessible(true);
        try {
            return method.invoke(objectTypeAdapter, in, peeked);
        } catch (InvocationTargetException e) {
            // unwrap the real exception
            throw e.getCause();
        }
    }
}