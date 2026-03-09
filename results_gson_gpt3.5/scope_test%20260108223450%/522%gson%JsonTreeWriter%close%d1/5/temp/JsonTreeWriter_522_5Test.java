package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_522_5Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void close_shouldAddSentinelClosedWhenStackIsEmpty() throws Exception {
        // Ensure stack is empty
        List<JsonElement> stack = getStack(writer);
        stack.clear();

        writer.close();

        stack = getStack(writer);
        assertFalse(stack.isEmpty());
        JsonElement last = stack.get(stack.size() - 1);
        assertTrue(last instanceof JsonPrimitive);
        assertEquals("closed", ((JsonPrimitive) last).getAsString());
    }

    @Test
    @Timeout(8000)
    public void close_shouldThrowIOExceptionWhenStackIsNotEmpty() {
        List<JsonElement> stack = getStack(writer);
        stack.add(new JsonPrimitive("not empty"));

        IOException exception = assertThrows(IOException.class, () -> writer.close());
        assertEquals("Incomplete document", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    private List<JsonElement> getStack(JsonTreeWriter writer) {
        try {
            Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
            stackField.setAccessible(true);
            return (List<JsonElement>) stackField.get(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}