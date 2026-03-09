package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
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
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_519_2Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() throws Exception {
        jsonTreeWriter = new JsonTreeWriter();

        // Initialize the stack with a JsonArray to allow adding elements
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.add(new com.google.gson.JsonArray());
    }

    @Test
    @Timeout(8000)
    public void testValue_long_addsJsonPrimitiveAndReturnsThis() throws Exception {
        // Call value(long)
        JsonTreeWriter returned = (JsonTreeWriter) jsonTreeWriter.value(123L);

        // Assert returned instance is the same
        assertSame(jsonTreeWriter, returned);

        // Use reflection to get private field 'stack'
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

        // The stack's top element is a JsonArray, check its contents
        com.google.gson.JsonArray jsonArray = (com.google.gson.JsonArray) stack.get(stack.size() - 1);
        assertEquals(1, jsonArray.size());
        JsonElement element = jsonArray.get(0);
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(new JsonPrimitive(123L), element);
    }

    @Test
    @Timeout(8000)
    public void testValue_long_withPendingName_putsInObject() throws Exception {
        // Use reflection to set private field 'pendingName'
        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        pendingNameField.set(jsonTreeWriter, "testName");

        // Use reflection to get private field 'stack' and add a JsonObject
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(new com.google.gson.JsonObject());

        // Call value(long)
        jsonTreeWriter.value(456L);

        // The JsonObject should have property "testName" with JsonPrimitive 456L
        com.google.gson.JsonObject jsonObject = (com.google.gson.JsonObject) stack.get(0);
        assertTrue(jsonObject.has("testName"));
        assertEquals(new JsonPrimitive(456L), jsonObject.get("testName"));

        // pendingName should be reset to null
        assertNull(pendingNameField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testValue_long_whenClosed_throwsIllegalStateException() throws Exception {
        // Use reflection to set private field 'product' to SENTINEL_CLOSED
        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);

        Field sentinelClosedField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
        sentinelClosedField.setAccessible(true);
        JsonElement sentinelClosed = (JsonElement) sentinelClosedField.get(null);

        productField.set(jsonTreeWriter, sentinelClosed);

        // Also clear the stack to simulate closed state properly
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();

        // value(long) should throw IllegalStateException because writer is closed
        assertThrows(IllegalStateException.class, () -> {
            jsonTreeWriter.value(789L);
        });
    }

    @Test
    @Timeout(8000)
    public void testPut_privateMethod_addsValueCorrectly() throws Exception {
        // Use reflection to get private method 'put'
        Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethod.setAccessible(true);

        // Use reflection to get private field 'stack' and add a JsonArray for correct state
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(new com.google.gson.JsonArray());

        // Call put with JsonPrimitive long value
        JsonPrimitive primitive = new JsonPrimitive(321L);
        putMethod.invoke(jsonTreeWriter, primitive);

        // Verify stack's top JsonArray contains the element
        com.google.gson.JsonArray jsonArray = (com.google.gson.JsonArray) stack.get(stack.size() - 1);
        assertEquals(1, jsonArray.size());
        assertEquals(primitive, jsonArray.get(0));
    }
}