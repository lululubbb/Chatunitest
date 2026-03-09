package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_237_6Test {

    private JsonTreeReader jsonTreeReader;
    private Method expectMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a minimal JsonElement mock or subclass since constructor requires it
        // Using Mockito mock for JsonElement as it is abstract
        var jsonElementMock = mock(com.google.gson.JsonElement.class);
        jsonTreeReader = new JsonTreeReader(jsonElementMock);

        // Access private expect method via reflection
        expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void expect_shouldNotThrow_whenPeekMatchesExpected() throws Throwable {
        // Spy the jsonTreeReader to mock peek()
        JsonTreeReader spyReader = spy(jsonTreeReader);
        JsonToken expectedToken = JsonToken.BEGIN_OBJECT;
        doReturn(expectedToken).when(spyReader).peek();

        // invoke expect with matching token
        expectMethod.invoke(spyReader, expectedToken);
    }

    @Test
    @Timeout(8000)
    public void expect_shouldThrowIllegalStateException_whenPeekDoesNotMatchExpected() throws Throwable {
        JsonTreeReader spyReader = spy(jsonTreeReader);
        JsonToken expectedToken = JsonToken.BEGIN_ARRAY;
        JsonToken actualToken = JsonToken.END_OBJECT;
        doReturn(actualToken).when(spyReader).peek();

        try {
            expectMethod.invoke(spyReader, expectedToken);
            fail("Expected IllegalStateException");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IllegalStateException);
            String message = cause.getMessage();
            assertTrue(message.contains("Expected " + expectedToken));
            assertTrue(message.contains("but was " + actualToken));
        }
    }
}