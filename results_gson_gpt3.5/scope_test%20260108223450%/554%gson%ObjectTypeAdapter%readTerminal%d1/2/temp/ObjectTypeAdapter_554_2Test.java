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
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectTypeAdapter_554_2Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private ToNumberStrategy toNumberStrategyMock;

    @BeforeEach
    public void setUp() throws Exception {
        toNumberStrategyMock = mock(ToNumberStrategy.class);

        Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(com.google.gson.Gson.class, ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = constructor.newInstance(null, toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_string() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);
        when(jsonReaderMock.nextString()).thenReturn("testString");

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.STRING);

        assertEquals("testString", result);
        verify(jsonReaderMock).nextString();
        verifyNoMoreInteractions(jsonReaderMock);
        verifyNoInteractions(toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_number() throws Exception, IOException {
        JsonReader jsonReaderMock = mock(JsonReader.class);
        when(toNumberStrategyMock.readNumber(jsonReaderMock)).thenReturn(12345);

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.NUMBER);

        assertEquals(12345, result);
        verify(toNumberStrategyMock).readNumber(jsonReaderMock);
        verifyNoMoreInteractions(toNumberStrategyMock);
        verifyNoInteractions(jsonReaderMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_boolean() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);
        when(jsonReaderMock.nextBoolean()).thenReturn(true);

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.BOOLEAN);

        assertEquals(true, result);
        verify(jsonReaderMock).nextBoolean();
        verifyNoMoreInteractions(jsonReaderMock);
        verifyNoInteractions(toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_null() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);
        doNothing().when(jsonReaderMock).nextNull();

        Object result = invokeReadTerminal(jsonReaderMock, JsonToken.NULL);

        assertNull(result);
        verify(jsonReaderMock).nextNull();
        verifyNoMoreInteractions(jsonReaderMock);
        verifyNoInteractions(toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_unexpectedToken() throws Exception {
        JsonReader jsonReaderMock = mock(JsonReader.class);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            invokeReadTerminal(jsonReaderMock, JsonToken.BEGIN_ARRAY);
        });

        assertTrue(exception.getMessage().contains("Unexpected token"));
        verifyNoInteractions(jsonReaderMock);
        verifyNoInteractions(toNumberStrategyMock);
    }

    private Object invokeReadTerminal(JsonReader in, JsonToken peeked) throws Exception {
        Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
        method.setAccessible(true);
        try {
            return method.invoke(objectTypeAdapter, in, peeked);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }
}