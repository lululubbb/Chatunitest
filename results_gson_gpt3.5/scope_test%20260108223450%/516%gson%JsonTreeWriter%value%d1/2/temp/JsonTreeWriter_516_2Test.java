package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonTreeWriter_516_2Test {
    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testValue_withNonNullBoolean() throws IOException {
        JsonWriter returned = jsonTreeWriter.value(Boolean.TRUE);
        assertSame(jsonTreeWriter, returned);

        // Use reflection to get the private field 'product' and check it is a JsonPrimitive with value true
        try {
            var productField = JsonTreeWriter.class.getDeclaredField("product");
            productField.setAccessible(true);
            JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
            assertTrue(product.isJsonPrimitive());
            JsonPrimitive primitive = product.getAsJsonPrimitive();
            assertTrue(primitive.getAsBoolean());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection access failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValue_withNullBoolean() throws IOException {
        JsonWriter returned = jsonTreeWriter.value((Boolean) null);
        assertSame(jsonTreeWriter, returned);

        // product should be JsonNull.INSTANCE after nullValue call
        try {
            var productField = JsonTreeWriter.class.getDeclaredField("product");
            productField.setAccessible(true);
            JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
            // JsonNull.INSTANCE is singleton, check equality by class name
            assertEquals("com.google.gson.JsonNull", product.getClass().getName());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection access failed: " + e.getMessage());
        }
    }
}