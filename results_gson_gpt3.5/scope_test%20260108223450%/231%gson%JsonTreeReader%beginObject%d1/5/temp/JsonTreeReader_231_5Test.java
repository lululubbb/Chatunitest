package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class JsonTreeReader_231_5Test {

    private JsonTreeReader jsonTreeReader;
    private JsonObject jsonObject;

    @BeforeEach
    void setUp() throws Exception {
        // Create a JsonObject with some entries
        jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        jsonObject.addProperty("key2", "value2");

        // Instantiate JsonTreeReader with the JsonObject
        jsonTreeReader = new JsonTreeReader(jsonObject);

        // Using reflection to set stack and stackSize to simulate peekStack returning jsonObject
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        stack[0] = jsonObject;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);
    }

    @Test
    @Timeout(8000)
    void beginObject_shouldPushIteratorAndExpectBeginObject() throws Exception {
        // Spy on JsonTreeReader to verify beginObject behavior
        JsonTreeReader spyReader = spy(jsonTreeReader);

        // Replace stack and stackSize in spy as well
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(spyReader);
        stack[0] = jsonObject;
        stackField.set(spyReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(spyReader, 1);

        // Call beginObject
        spyReader.beginObject();

        // Verify that top of stack is now an Iterator (the iterator of the JsonObject entrySet)
        Object[] stackAfter = (Object[]) stackField.get(spyReader);
        int stackSizeAfter = stackSizeField.getInt(spyReader);
        assertTrue(stackSizeAfter > 1, "Stack size should have increased by 1 after push");

        Object top = stackAfter[stackSizeAfter - 1];
        assertTrue(top instanceof Iterator, "Top of stack should be an Iterator");

        // Verify the iterator iterates over the entrySet of the JsonObject
        @SuppressWarnings("unchecked")
        Iterator<Map.Entry<String, JsonElement>> iterator = (Iterator<Map.Entry<String, JsonElement>>) top;
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        assertEquals(entries.size(), countIteratorElements(iterator));

        // Verify stack bottom remains the same JsonObject
        assertSame(jsonObject, stackAfter[0]);
    }

    @Test
    @Timeout(8000)
    void beginObject_whenPeekStackNotJsonObject_shouldThrowMalformedJsonException() throws Exception {
        // Set stack[0] to a non-JsonObject to cause MalformedJsonException
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        stack[0] = "not a JsonObject";
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        // The actual thrown exception is MalformedJsonException, so test for that
        assertThrows(com.google.gson.stream.MalformedJsonException.class, () -> jsonTreeReader.beginObject());
    }

    // Helper method to count elements in an iterator without modifying it permanently
    private int countIteratorElements(Iterator<?> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
}