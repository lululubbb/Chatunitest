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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NumberTypeAdapter_303_5Test {

    private ToNumberStrategy toNumberStrategyMock;
    private NumberTypeAdapter numberTypeAdapter;
    private JsonReader jsonReaderMock;

    @BeforeEach
    public void setUp() throws Exception {
        toNumberStrategyMock = mock(ToNumberStrategy.class);
        jsonReaderMock = mock(JsonReader.class);

        Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
        constructor.setAccessible(true);
        numberTypeAdapter = constructor.newInstance(toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testRead_NullToken_ReturnsNull() throws IOException {
        when(jsonReaderMock.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(jsonReaderMock).nextNull();

        Number result = numberTypeAdapter.read(jsonReaderMock);

        verify(jsonReaderMock).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_NumberToken_ReturnsNumberFromStrategy() throws IOException {
        when(jsonReaderMock.peek()).thenReturn(JsonToken.NUMBER);
        Number expectedNumber = 123.45;
        when(toNumberStrategyMock.readNumber(jsonReaderMock)).thenReturn(expectedNumber);

        Number result = numberTypeAdapter.read(jsonReaderMock);

        verify(toNumberStrategyMock).readNumber(jsonReaderMock);
        assertEquals(expectedNumber, result);
    }

    @Test
    @Timeout(8000)
    public void testRead_StringToken_ReturnsNumberFromStrategy() throws IOException {
        when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING);
        Number expectedNumber = 678.90;
        when(toNumberStrategyMock.readNumber(jsonReaderMock)).thenReturn(expectedNumber);

        Number result = numberTypeAdapter.read(jsonReaderMock);

        verify(toNumberStrategyMock).readNumber(jsonReaderMock);
        assertEquals(expectedNumber, result);
    }

    @Test
    @Timeout(8000)
    public void testRead_InvalidToken_ThrowsJsonSyntaxException() throws IOException {
        when(jsonReaderMock.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
        when(jsonReaderMock.getPath()).thenReturn("$.somePath");

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> {
            numberTypeAdapter.read(jsonReaderMock);
        });

        String expectedMessage = "Expecting number, got: BEGIN_OBJECT; at path $.somePath";
        assertEquals(expectedMessage, exception.getMessage());
    }
}