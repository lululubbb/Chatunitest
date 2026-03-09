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

public class ObjectTypeAdapter_554_3Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private ToNumberStrategy toNumberStrategyMock;
    private JsonReader jsonReaderMock;
    private Method readTerminalMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        toNumberStrategyMock = mock(ToNumberStrategy.class);
        jsonReaderMock = mock(JsonReader.class);
        // Use reflection to access private constructor
        try {
            var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(com.google.gson.Gson.class, ToNumberStrategy.class);
            constructor.setAccessible(true);
            objectTypeAdapter = (ObjectTypeAdapter) constructor.newInstance(null, toNumberStrategyMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        readTerminalMethod = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
        readTerminalMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_String() throws Throwable {
        when(jsonReaderMock.nextString()).thenReturn("testString");

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.STRING);
        assertEquals("testString", result);

        verify(jsonReaderMock, times(1)).nextString();
        verifyNoMoreInteractions(jsonReaderMock);
        verifyNoInteractions(toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_Number() throws Throwable {
        Number expectedNumber = 123.45;
        when(toNumberStrategyMock.readNumber(jsonReaderMock)).thenReturn(expectedNumber);

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.NUMBER);
        assertEquals(expectedNumber, result);

        verify(toNumberStrategyMock, times(1)).readNumber(jsonReaderMock);
        verifyNoInteractions(jsonReaderMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_Boolean() throws Throwable {
        when(jsonReaderMock.nextBoolean()).thenReturn(true);

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.BOOLEAN);
        assertEquals(true, result);

        verify(jsonReaderMock, times(1)).nextBoolean();
        verifyNoMoreInteractions(jsonReaderMock);
        verifyNoInteractions(toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_Null() throws Throwable {
        doNothing().when(jsonReaderMock).nextNull();

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.NULL);
        assertNull(result);

        verify(jsonReaderMock, times(1)).nextNull();
        verifyNoMoreInteractions(jsonReaderMock);
        verifyNoInteractions(toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_UnexpectedToken() {
        JsonToken invalidToken = JsonToken.BEGIN_ARRAY;

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            invokeReadTerminal(jsonReaderMock, invalidToken);
        });
        assertTrue(thrown.getMessage().contains("Unexpected token: " + invalidToken));

        verifyNoInteractions(jsonReaderMock);
        verifyNoInteractions(toNumberStrategyMock);
    }

    private Object invokeReadTerminal(JsonReader in, JsonToken peeked) throws Throwable {
        try {
            return readTerminalMethod.invoke(objectTypeAdapter, in, peeked);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}