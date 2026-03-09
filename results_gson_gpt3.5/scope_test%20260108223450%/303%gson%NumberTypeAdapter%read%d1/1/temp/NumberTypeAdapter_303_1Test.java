package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NumberTypeAdapter_303_1Test {

    private ToNumberStrategy toNumberStrategy;
    private NumberTypeAdapter adapter;
    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() throws Exception {
        toNumberStrategy = mock(ToNumberStrategy.class);
        jsonReader = mock(JsonReader.class);

        Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
        constructor.setAccessible(true);
        adapter = constructor.newInstance(toNumberStrategy);
    }

    @Test
    @Timeout(8000)
    public void testRead_NullToken_ReturnsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        // nextNull() should be called
        doNothing().when(jsonReader).nextNull();

        Number result = adapter.read(jsonReader);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_NumberToken_ReturnsNumberFromStrategy() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NUMBER);
        Number expectedNumber = 42;
        when(toNumberStrategy.readNumber(jsonReader)).thenReturn(expectedNumber);

        Number result = adapter.read(jsonReader);

        InOrder inOrder = inOrder(jsonReader, toNumberStrategy);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(toNumberStrategy).readNumber(jsonReader);
        assertEquals(expectedNumber, result);
    }

    @Test
    @Timeout(8000)
    public void testRead_StringToken_ReturnsNumberFromStrategy() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        Number expectedNumber = 3.14;
        when(toNumberStrategy.readNumber(jsonReader)).thenReturn(expectedNumber);

        Number result = adapter.read(jsonReader);

        InOrder inOrder = inOrder(jsonReader, toNumberStrategy);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(toNumberStrategy).readNumber(jsonReader);
        assertEquals(expectedNumber, result);
    }

    @Test
    @Timeout(8000)
    public void testRead_UnexpectedToken_ThrowsJsonSyntaxException() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
        when(jsonReader.getPath()).thenReturn("$.somePath");

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> {
            adapter.read(jsonReader);
        });

        assertTrue(exception.getMessage().contains("Expecting number, got: BEGIN_ARRAY; at path $.somePath"));
    }
}