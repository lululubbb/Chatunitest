package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
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
import java.lang.reflect.Field;
import java.util.List;

class JsonTreeWriter_value_Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void value_null_returnsNullValue() throws IOException {
        JsonWriter result = writer.value((Boolean) null);
        assertNotNull(result);
        assertEquals(writer, result);
        // Verify that nullValue() sets the product to JsonNull.INSTANCE
        JsonElement product = getPrivateField(writer, "product");
        assertEquals(JsonNull.INSTANCE, product);
    }

    @Test
    @Timeout(8000)
    public void value_true_putsJsonPrimitiveTrue() throws Exception {
        JsonWriter result = writer.value(Boolean.TRUE);
        assertEquals(writer, result);

        List<JsonElement> stack = getPrivateField(writer, "stack");
        assertFalse(stack.isEmpty());
        JsonElement last = stack.get(stack.size() - 1);
        assertTrue(last.isJsonPrimitive());
        assertEquals(Boolean.TRUE, last.getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void value_false_putsJsonPrimitiveFalse() throws Exception {
        JsonWriter result = writer.value(Boolean.FALSE);
        assertEquals(writer, result);

        List<JsonElement> stack = getPrivateField(writer, "stack");
        assertFalse(stack.isEmpty());
        JsonElement last = stack.get(stack.size() - 1);
        assertTrue(last.isJsonPrimitive());
        assertEquals(Boolean.FALSE, last.getAsBoolean());
    }

    // Helper to access private fields via reflection
    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object instance, String fieldName) {
        try {
            Field field = JsonTreeWriter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}