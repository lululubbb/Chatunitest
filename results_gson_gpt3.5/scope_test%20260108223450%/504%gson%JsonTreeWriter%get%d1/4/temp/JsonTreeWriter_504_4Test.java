package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_504_4Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testGet_whenStackIsEmpty_returnsProduct() throws Exception {
        // Initially product is JsonNull.INSTANCE
        JsonElement result = writer.get();
        assertNotNull(result);
        assertTrue(result instanceof JsonNull);
    }

    @Test
    @Timeout(8000)
    public void testGet_whenStackIsNotEmpty_throwsIllegalStateException() throws Exception {
        // Use reflection to add element to private stack field
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
        stack.add(new JsonArray());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            writer.get();
        });
        assertTrue(thrown.getMessage().startsWith("Expected one JSON element but was "));
    }
}