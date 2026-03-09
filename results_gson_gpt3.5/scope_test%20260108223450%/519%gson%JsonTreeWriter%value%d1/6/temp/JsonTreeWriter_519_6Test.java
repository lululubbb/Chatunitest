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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class JsonTreeWriter_519_6Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testValue_long_shouldReturnThisAndPutJsonPrimitive() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Call value(long)
        JsonWriter returned = jsonTreeWriter.value(123L);

        // Check returned is this
        assertSame(jsonTreeWriter, returned);

        // Use reflection to access private field 'product' to verify internal state
        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        JsonElement topElement = (JsonElement) productField.get(jsonTreeWriter);

        // The top element should be JsonPrimitive with value 123
        assertTrue(topElement instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) topElement;
        assertEquals(123L, primitive.getAsLong());
    }

    @Test
    @Timeout(8000)
    public void testValue_long_withEmptyStack_shouldSetProduct() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Use reflection to get the private field 'product'
        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);

        // Initially product is JsonNull.INSTANCE
        Object initialProduct = productField.get(jsonTreeWriter);
        assertNotNull(initialProduct);

        // Call value(long)
        jsonTreeWriter.value(456L);

        // After value(long), product should be JsonPrimitive(456)
        Object updatedProduct = productField.get(jsonTreeWriter);
        assertTrue(updatedProduct instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) updatedProduct;
        assertEquals(456L, primitive.getAsLong());
    }
}