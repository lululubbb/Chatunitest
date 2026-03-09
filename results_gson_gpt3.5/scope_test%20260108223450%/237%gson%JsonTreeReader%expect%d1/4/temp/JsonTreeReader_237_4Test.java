package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_237_4Test {

    private JsonTreeReader jsonTreeReader;
    private Method expectMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a minimal JsonElement to instantiate JsonTreeReader
        JsonElement element = new JsonNull();

        jsonTreeReader = new JsonTreeReader(element);

        // Access private expect method via reflection
        expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void expect_whenTokenMatches_doesNotThrow() throws Exception {
        // Arrange
        // We need to mock peek() to return the expected token.
        // Since peek() is public, override it with a spy.
        JsonTreeReader spyReader = spy(jsonTreeReader);
        JsonToken expected = JsonToken.NULL;
        doReturn(expected).when(spyReader).peek();

        // Act & Assert
        expectMethod.invoke(spyReader, expected);
    }

    @Test
    @Timeout(8000)
    public void expect_whenTokenDoesNotMatch_throwsIllegalStateException() throws Exception {
        // Arrange
        JsonToken expected = JsonToken.BEGIN_OBJECT;
        JsonToken actual = JsonToken.END_ARRAY;

        // Create a spy on the original jsonTreeReader
        JsonTreeReader spyReader = spy(jsonTreeReader);

        // Mock peek() to return the actual token (mismatch)
        doReturn(actual).when(spyReader).peek();

        // Also mock locationString() to return " at path $"
        Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
        locationStringMethod.setAccessible(true);
        doReturn(" at path $").when(spyReader).locationString();

        // Act & Assert
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> expectMethod.invoke(spyReader, expected));

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalStateException);
        assertEquals("Expected " + expected + " but was " + actual + " at path $", cause.getMessage());
    }
}