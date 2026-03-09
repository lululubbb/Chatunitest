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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectTypeAdapter_554_4Test {

    private ObjectTypeAdapter objectTypeAdapter;
    private ToNumberStrategy toNumberStrategyMock;
    private JsonReader jsonReaderMock;
    private Method readTerminalMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        toNumberStrategyMock = mock(ToNumberStrategy.class);
        // Create real instance via reflection since constructor is private
        try {
            var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(com.google.gson.Gson.class, ToNumberStrategy.class);
            constructor.setAccessible(true);
            objectTypeAdapter = constructor.newInstance(null, toNumberStrategyMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        jsonReaderMock = mock(JsonReader.class);

        readTerminalMethod = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
        readTerminalMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_string() throws Throwable {
        when(jsonReaderMock.nextString()).thenReturn("testString");
        Object result = readTerminalMethod.invoke(objectTypeAdapter, jsonReaderMock, JsonToken.STRING);
        assertEquals("testString", result);
        verify(jsonReaderMock).nextString();
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_number() throws Throwable, IOException {
        Number numberValue = 12345;
        when(toNumberStrategyMock.readNumber(jsonReaderMock)).thenReturn(numberValue);
        Object result = readTerminalMethod.invoke(objectTypeAdapter, jsonReaderMock, JsonToken.NUMBER);
        assertEquals(numberValue, result);
        verify(toNumberStrategyMock).readNumber(jsonReaderMock);
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_booleanTrue() throws Throwable {
        when(jsonReaderMock.nextBoolean()).thenReturn(true);
        Object result = readTerminalMethod.invoke(objectTypeAdapter, jsonReaderMock, JsonToken.BOOLEAN);
        assertEquals(true, result);
        verify(jsonReaderMock).nextBoolean();
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_booleanFalse() throws Throwable {
        when(jsonReaderMock.nextBoolean()).thenReturn(false);
        Object result = readTerminalMethod.invoke(objectTypeAdapter, jsonReaderMock, JsonToken.BOOLEAN);
        assertEquals(false, result);
        verify(jsonReaderMock).nextBoolean();
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_null() throws Throwable {
        doNothing().when(jsonReaderMock).nextNull();
        Object result = readTerminalMethod.invoke(objectTypeAdapter, jsonReaderMock, JsonToken.NULL);
        assertNull(result);
        verify(jsonReaderMock).nextNull();
    }

    @Test
    @Timeout(8000)
    public void testReadTerminal_unexpectedToken() {
        JsonToken unexpectedToken = JsonToken.BEGIN_ARRAY;
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            readTerminalMethod.invoke(objectTypeAdapter, jsonReaderMock, unexpectedToken);
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertEquals("Unexpected token: " + unexpectedToken, cause.getMessage());
    }
}