package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonTreeReader_237_5Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Instead of passing null, pass a minimal JsonElement to avoid NPE in peek()
        JsonElement element = new JsonArray();
        jsonTreeReader = new JsonTreeReader(element);
    }

    @Test
    @Timeout(8000)
    public void testExpect_withMatchingToken() throws Exception {
        JsonTreeReader spyReader = spy(jsonTreeReader);

        // We need to mock peek() to return the expected token.
        when(spyReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY);

        Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);

        // Should not throw when peek() returns expected token
        assertDoesNotThrow(() -> expectMethod.invoke(spyReader, JsonToken.BEGIN_ARRAY));
    }

    @Test
    @Timeout(8000)
    public void testExpect_withNonMatchingToken_throwsIllegalStateException() throws Exception {
        JsonTreeReader spyReader = spy(jsonTreeReader);

        when(spyReader.peek()).thenReturn(JsonToken.STRING);

        Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            try {
                expectMethod.invoke(spyReader, JsonToken.NUMBER);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // unwrap the cause from reflection invocation
                throw e.getCause();
            }
        });

        String message = exception.getMessage();
        assertTrue(message.contains("Expected NUMBER but was STRING"));
    }
}