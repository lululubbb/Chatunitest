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
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.ToNumberStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NumberTypeAdapter_303_3Test {

    private ToNumberStrategy toNumberStrategy;
    private NumberTypeAdapter numberTypeAdapter;
    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() throws Exception {
        toNumberStrategy = mock(ToNumberStrategy.class);
        // Use reflection to instantiate private constructor
        Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
        constructor.setAccessible(true);
        numberTypeAdapter = constructor.newInstance(toNumberStrategy);

        jsonReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    public void read_returnsNull_whenJsonTokenIsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        // nextNull() should be called
        doNothing().when(jsonReader).nextNull();

        Number result = numberTypeAdapter.read(jsonReader);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).nextNull();

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void read_returnsNumber_whenJsonTokenIsNumber() throws IOException {
        Number expectedNumber = 123;
        when(jsonReader.peek()).thenReturn(JsonToken.NUMBER);
        when(toNumberStrategy.readNumber(jsonReader)).thenReturn(expectedNumber);

        Number result = numberTypeAdapter.read(jsonReader);

        InOrder inOrder = inOrder(jsonReader, toNumberStrategy);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(toNumberStrategy).readNumber(jsonReader);

        assertEquals(expectedNumber, result);
    }

    @Test
    @Timeout(8000)
    public void read_returnsNumber_whenJsonTokenIsString() throws IOException {
        Number expectedNumber = 456.78;
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(toNumberStrategy.readNumber(jsonReader)).thenReturn(expectedNumber);

        Number result = numberTypeAdapter.read(jsonReader);

        InOrder inOrder = inOrder(jsonReader, toNumberStrategy);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(toNumberStrategy).readNumber(jsonReader);

        assertEquals(expectedNumber, result);
    }

    @Test
    @Timeout(8000)
    public void read_throwsJsonSyntaxException_whenJsonTokenIsUnexpected() throws IOException {
        JsonToken unexpectedToken = JsonToken.BEGIN_ARRAY;
        when(jsonReader.peek()).thenReturn(unexpectedToken);
        when(jsonReader.getPath()).thenReturn("$.somePath");

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> {
            numberTypeAdapter.read(jsonReader);
        });

        String expectedMessage = "Expecting number, got: " + unexpectedToken + "; at path " + "$.somePath";
        assertEquals(expectedMessage, exception.getMessage());

        verify(jsonReader).peek();
        verify(jsonReader).getPath();
    }
}