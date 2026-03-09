package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonTreeWriter_516_3Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
        // Initialize the writer with a beginObject to ensure stack is not empty
        try {
            writer.beginObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testValue_withNonNullBoolean_returnsThisAndStoresJsonPrimitive() throws IOException, Exception {
        // Set a name before calling value() to properly add the value to the object
        writer.name("key");
        JsonWriter result = writer.value(Boolean.TRUE);
        assertSame(writer, result);

        // Use reflection to get private method peek() to inspect the top element in the stack
        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        JsonElement topElement = (JsonElement) peekMethod.invoke(writer);

        assertTrue(topElement instanceof JsonObject);
        // The JsonObject should have the key with value true
        assertEquals(Boolean.TRUE, ((JsonPrimitive) ((JsonElement) ((com.google.gson.JsonObject) topElement).get("key"))).getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void testValue_withNullBoolean_callsNullValueAndReturnsThis() throws IOException, Exception {
        // Set a name before calling value() to properly add the value to the object
        writer.name("key");
        JsonWriter result = writer.value((Boolean) null);
        assertSame(writer, result);

        // Use reflection to get private method peek() to inspect the top element in the stack
        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        JsonElement topElement = (JsonElement) peekMethod.invoke(writer);

        assertTrue(topElement instanceof com.google.gson.JsonObject);
        // The JsonObject should have the key with value JsonNull.INSTANCE
        assertEquals(JsonNull.INSTANCE, ((com.google.gson.JsonObject) topElement).get("key"));
    }
}