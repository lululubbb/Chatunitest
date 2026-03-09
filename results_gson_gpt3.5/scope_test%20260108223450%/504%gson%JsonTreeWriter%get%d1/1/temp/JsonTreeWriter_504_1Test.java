package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_504_1Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void get_whenStackIsEmpty_returnsProduct() throws Exception {
        // Arrange
        // By default, product is JsonNull.INSTANCE and stack is empty.
        JsonElement product = getField(writer, "product");
        List<?> stack = getField(writer, "stack");

        assertTrue(stack.isEmpty());
        assertEquals(JsonNull.INSTANCE, product);

        // Act
        JsonElement result = writer.get();

        // Assert
        assertSame(product, result);
    }

    @Test
    @Timeout(8000)
    public void get_whenStackIsNotEmpty_throwsIllegalStateException() throws Exception {
        // Arrange
        List<JsonElement> stack = getField(writer, "stack");
        stack.add(new JsonArray()); // add element to stack to simulate not empty

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.get());
        assertTrue(exception.getMessage().startsWith("Expected one JSON element but was"));
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }
}