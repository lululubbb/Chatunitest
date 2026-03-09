package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_519_5Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testValue_long_addsJsonPrimitiveToStackAndReturnsThis() throws Exception {
        long testValue = 123456789L;

        // Push a JsonArray on stack to allow adding a value
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.add(new com.google.gson.JsonArray());

        // Change variable type to JsonWriter to match method signature
        // Cast returned JsonWriter to JsonTreeWriter for assertions
        JsonWriter returnedWriter = jsonTreeWriter.value(testValue);
        assertSame(jsonTreeWriter, returnedWriter);
        JsonTreeWriter returned = (JsonTreeWriter) returnedWriter;

        assertFalse(stack.isEmpty());
        JsonElement element = stack.get(stack.size() - 1);
        assertTrue(element instanceof com.google.gson.JsonArray);
        com.google.gson.JsonArray array = (com.google.gson.JsonArray) element;
        assertFalse(array.isEmpty());
        JsonElement addedElement = array.get(array.size() - 1);
        assertTrue(addedElement instanceof JsonPrimitive);
        JsonPrimitive prim = (JsonPrimitive) addedElement;
        assertEquals(testValue, prim.getAsLong());
    }

    @Test
    @Timeout(8000)
    public void testValue_long_withPendingNameAddsToObject() throws Exception {
        // Use reflection to set private field 'pendingName'
        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        pendingNameField.set(jsonTreeWriter, "testName");

        // Use reflection to push a JsonObject on stack
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.add(new com.google.gson.JsonObject());

        long testValue = 42L;

        // Change variable type to JsonWriter to match method signature
        JsonWriter returnedWriter = jsonTreeWriter.value(testValue);
        assertSame(jsonTreeWriter, returnedWriter);
        JsonTreeWriter returned = (JsonTreeWriter) returnedWriter;

        // The JsonObject on top of stack should now have the property "testName" with value 42
        com.google.gson.JsonObject obj = (com.google.gson.JsonObject) stack.get(stack.size() - 1);
        assertTrue(obj.has("testName"));
        assertEquals(testValue, obj.get("testName").getAsLong());

        // pendingName should be cleared
        assertNull(pendingNameField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testValue_long_withClosedProductThrows() throws Exception {
        // Use reflection to set private field 'product' to SENTINEL_CLOSED
        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        Field sentinelClosedField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
        sentinelClosedField.setAccessible(true);
        productField.set(jsonTreeWriter, sentinelClosedField.get(null));

        // Also clear the stack to avoid put() silently succeeding
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();

        // Do NOT add any JsonArray or JsonObject to stack to ensure put() will throw IOException due to closed product

        IOException thrown = assertThrows(IOException.class, () -> jsonTreeWriter.value(1L));
        // The actual method put(...) throws IOException if closed, so we expect that
    }
}