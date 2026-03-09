package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ToNumberStrategy;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectTypeAdapter_554_1Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private ToNumberStrategy toNumberStrategyMock;

    @BeforeEach
    public void setUp() throws Exception {
        toNumberStrategyMock = mock(ToNumberStrategy.class);

        // Use reflection to invoke private constructor
        var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(com.google.gson.Gson.class, ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = (ObjectTypeAdapter) constructor.newInstance(null, toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_withString_returnsString() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);
        when(jsonReaderMock.nextString()).thenReturn("testString");

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.STRING);

        assertEquals("testString", result);
        verify(jsonReaderMock, times(1)).nextString();
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_withNumber_returnsNumber() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);
        Number expectedNumber = 123.45;
        when(toNumberStrategyMock.readNumber(jsonReaderMock)).thenReturn(expectedNumber);

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.NUMBER);

        assertEquals(expectedNumber, result);
        verify(toNumberStrategyMock, times(1)).readNumber(jsonReaderMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_withBoolean_returnsBoolean() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);
        when(jsonReaderMock.nextBoolean()).thenReturn(true);

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.BOOLEAN);

        assertEquals(true, result);
        verify(jsonReaderMock, times(1)).nextBoolean();
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_withNull_returnsNull() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);
        doNothing().when(jsonReaderMock).nextNull();

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.NULL);

        assertNull(result);
        verify(jsonReaderMock, times(1)).nextNull();
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_withUnexpectedToken_throwsIllegalStateException() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);

        JsonToken unexpectedToken = JsonToken.BEGIN_OBJECT;

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () ->
                invokeReadTerminal(jsonReaderMock, unexpectedToken));

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalStateException);
        assertTrue(cause.getMessage().contains("Unexpected token: " + unexpectedToken));
    }

    private Object invokeReadTerminal(JsonReader in, JsonToken peeked) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
        method.setAccessible(true);
        return method.invoke(objectTypeAdapter, in, peeked);
    }
}