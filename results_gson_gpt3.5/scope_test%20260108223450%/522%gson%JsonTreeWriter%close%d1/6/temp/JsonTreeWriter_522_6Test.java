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

public class JsonTreeWriter_522_6Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void close_whenStackIsEmpty_addsSentinelClosed() throws Exception {
        // Initially stack is empty, call close should add SENTINEL_CLOSED

        jsonTreeWriter.close();

        // Access private field stack via reflection
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<?> stack = (List<?>) stackField.get(jsonTreeWriter);

        // Access SENTINEL_CLOSED static field via reflection
        Field sentinelField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
        sentinelField.setAccessible(true);
        JsonPrimitive sentinelClosed = (JsonPrimitive) sentinelField.get(null);

        assertEquals(1, stack.size());
        assertSame(sentinelClosed, stack.get(0));
    }

    @Test
    @Timeout(8000)
    public void close_whenStackIsNotEmpty_throwsIOException() throws Exception {
        // Access private field stack and add an element
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Object> stack = (List<Object>) stackField.get(jsonTreeWriter);
        stack.add(new JsonPrimitive("not empty"));

        IOException thrown = assertThrows(IOException.class, () -> jsonTreeWriter.close());
        assertEquals("Incomplete document", thrown.getMessage());
    }
}