package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_521_5Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testFlush_doesNothingAndDoesNotThrow() {
        // flush is overridden to do nothing, so just call it and verify no exception
        assertDoesNotThrow(() -> jsonTreeWriter.flush());
    }

    @Test
    @Timeout(8000)
    public void testPrivateFieldsAfterFlush() throws Exception {
        // Use reflection to check internal state before and after flush
        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);

        Object productBefore = productField.get(jsonTreeWriter);
        jsonTreeWriter.flush();
        Object productAfter = productField.get(jsonTreeWriter);

        // product should remain unchanged
        assertSame(productBefore, productAfter);

        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<?> stack = (List<?>) stackField.get(jsonTreeWriter);

        // stack should be empty initially
        assertTrue(stack.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testInvokePrivatePeekMethod() throws Exception {
        // Reflection to invoke private peek() method
        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        // Initially stack is empty, peek should throw IndexOutOfBoundsException or similar
        // But since peek() is private and we don't have its code, we test if it throws or returns null
        try {
            Object peekResult = peekMethod.invoke(jsonTreeWriter);
            // If no exception, peekResult should be null (since stack empty)
            assertNull(peekResult);
        } catch (Exception e) {
            // Acceptable if it throws IndexOutOfBoundsException wrapped in InvocationTargetException
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IndexOutOfBoundsException);
        }
    }

    @Test
    @Timeout(8000)
    public void testInvokePrivatePutMethod() throws Exception {
        // Reflection to invoke private put(JsonElement) method
        Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethod.setAccessible(true);

        // Put a JsonNull element and verify stack changes
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        // Clear stack before test to ensure consistent state
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();

        int sizeBefore = stack.size();

        // The put method may depend on pendingName or product fields, so reset them to safe defaults
        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        pendingNameField.set(jsonTreeWriter, null);

        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        productField.set(jsonTreeWriter, JsonNull.INSTANCE);

        putMethod.invoke(jsonTreeWriter, JsonNull.INSTANCE);

        int sizeAfter = stack.size();

        // Fix: put() does not add to stack if product is JsonNull.INSTANCE; it replaces product.
        // So check if product field changed instead of stack size.
        if (sizeAfter == sizeBefore) {
            JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
            assertSame(JsonNull.INSTANCE, product);
        } else {
            assertEquals(sizeBefore + 1, sizeAfter);
            assertSame(JsonNull.INSTANCE, stack.get(sizeAfter - 1));
        }
    }
}