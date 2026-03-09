package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_504_2Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void get_whenStackIsEmpty_returnsProduct() throws Exception {
        // By default, product is JsonNull.INSTANCE and stack is empty
        JsonElement result = writer.get();
        assertNotNull(result);
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    @Timeout(8000)
    public void get_whenStackIsNotEmpty_throwsIllegalStateException() throws Exception {
        // Use reflection to add an element to the private stack field
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
        stack.add(new JsonPrimitive("element"));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            writer.get();
        });
        assertTrue(thrown.getMessage().startsWith("Expected one JSON element but was"));
    }

}