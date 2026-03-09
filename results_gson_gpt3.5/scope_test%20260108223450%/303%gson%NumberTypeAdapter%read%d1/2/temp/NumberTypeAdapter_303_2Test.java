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
import java.lang.reflect.Method;

class NumberTypeAdapter_303_2Test {

    private ToNumberStrategy toNumberStrategy;
    private NumberTypeAdapter numberTypeAdapter;

    @BeforeEach
    void setUp() throws Exception {
        toNumberStrategy = mock(ToNumberStrategy.class);
        Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
        constructor.setAccessible(true);
        numberTypeAdapter = constructor.newInstance(toNumberStrategy);
    }

    @Test
    @Timeout(8000)
    void read_NullToken_ReturnsNull() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        doNothing().when(jsonReader).nextNull();

        Number result = numberTypeAdapter.read(jsonReader);

        assertNull(result);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).nextNull();
        inOrder.verifyNoMoreInteractions();
        verifyNoInteractions(toNumberStrategy);
    }

    @Test
    @Timeout(8000)
    void read_NumberToken_DelegatesToToNumberStrategy() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NUMBER);
        Number expectedNumber = 123;
        when(toNumberStrategy.readNumber(jsonReader)).thenReturn(expectedNumber);

        Number result = numberTypeAdapter.read(jsonReader);

        assertSame(expectedNumber, result);

        InOrder inOrder = inOrder(jsonReader, toNumberStrategy);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(toNumberStrategy).readNumber(jsonReader);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void read_StringToken_DelegatesToToNumberStrategy() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        Number expectedNumber = 456;
        when(toNumberStrategy.readNumber(jsonReader)).thenReturn(expectedNumber);

        Number result = numberTypeAdapter.read(jsonReader);

        assertSame(expectedNumber, result);

        InOrder inOrder = inOrder(jsonReader, toNumberStrategy);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(toNumberStrategy).readNumber(jsonReader);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void read_OtherToken_ThrowsJsonSyntaxException() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
        when(jsonReader.getPath()).thenReturn("$.somePath");

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> numberTypeAdapter.read(jsonReader));
        assertEquals("Expecting number, got: BEGIN_ARRAY; at path $.somePath", exception.getMessage());

        verify(jsonReader).peek();
        verify(jsonReader).getPath();
        verifyNoInteractions(toNumberStrategy);
    }
}