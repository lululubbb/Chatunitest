package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_237_2Test {

    private JsonTreeReader jsonTreeReader;
    private Method expectMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        // Create JsonTreeReader with a dummy JsonElement (null for test, as we only test expect)
        jsonTreeReader = new JsonTreeReader(null);

        // Access private expect method
        expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void expect_whenPeekReturnsExpected_doesNotThrow() throws Throwable {
        // Mock peek() to return expected token
        JsonToken expected = JsonToken.BEGIN_OBJECT;
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(expected).when(spyReader).peek();

        // Invoke expect with expected token, should not throw
        expectMethod.invoke(spyReader, expected);
    }

    @Test
    @Timeout(8000)
    public void expect_whenPeekReturnsDifferent_throwsIllegalStateException() throws Throwable {
        JsonToken expected = JsonToken.BEGIN_ARRAY;
        JsonToken actual = JsonToken.END_ARRAY;

        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(actual).when(spyReader).peek();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                expectMethod.invoke(spyReader, expected);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
        String message = thrown.getMessage();
        assertTrue(message.contains("Expected " + expected));
        assertTrue(message.contains("but was " + actual));
        assertTrue(message.contains(" at path "));
    }
}