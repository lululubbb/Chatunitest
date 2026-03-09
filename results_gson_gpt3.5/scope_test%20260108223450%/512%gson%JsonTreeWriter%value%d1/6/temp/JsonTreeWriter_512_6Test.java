package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeWriter_512_6Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testValue_withNonNullString_shouldReturnThis() throws IOException {
        JsonWriter result = jsonTreeWriter.value("test");
        assertSame(jsonTreeWriter, result, "value should return this for non-null input");
        // Use reflection to verify peek() behavior
        try {
            Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
            peekMethod.setAccessible(true);
            Object peeked = peekMethod.invoke(jsonTreeWriter);

            // peeked can be JsonPrimitive wrapping "test"
            assertNotNull(peeked, "peek result should not be null after value");
            assertTrue(peeked instanceof JsonPrimitive, "peek result should be instance of JsonPrimitive");
            JsonPrimitive primitive = (JsonPrimitive) peeked;
            assertEquals("test", primitive.getAsString(), "peeked JsonPrimitive should contain the input string");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection invocation failed: " + e.getCause());
        }
    }

    @Test
    @Timeout(8000)
    public void testValue_withNullString_shouldCallNullValue() throws IOException {
        JsonTreeWriter spyWriter = spy(jsonTreeWriter);
        // Spy nullValue() to return some JsonWriter (this)
        doReturn(spyWriter).when(spyWriter).nullValue();

        // Disambiguate call by casting null to String
        JsonWriter result = spyWriter.value((String) null);
        verify(spyWriter).nullValue();
        assertSame(spyWriter, result, "value should return nullValue result on null input");
    }
}