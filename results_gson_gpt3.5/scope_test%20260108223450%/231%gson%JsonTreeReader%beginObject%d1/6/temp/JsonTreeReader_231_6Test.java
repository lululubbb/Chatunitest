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
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_231_6Test {

    private JsonTreeReader jsonTreeReader;
    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare a JsonObject with one entry
        jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");

        // Create JsonTreeReader instance with the JsonObject
        jsonTreeReader = new JsonTreeReader(jsonObject);

        // Set up internal stack manually to simulate state before beginObject call
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        stack[0] = jsonObject;

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_success() throws Exception {
        // Invoke beginObject
        jsonTreeReader.beginObject();

        // Verify stack has increased by 1 and top is iterator of entrySet
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(jsonTreeReader);

        assertEquals(2, stackSize);

        Object top = stack[stackSize - 1];
        assertTrue(top instanceof Iterator);

        @SuppressWarnings("unchecked")
        Iterator<Map.Entry<String, JsonElement>> iterator = (Iterator<Map.Entry<String, JsonElement>>) top;
        assertTrue(iterator.hasNext());
        Map.Entry<String, JsonElement> entry = iterator.next();
        assertEquals("key", entry.getKey());
        assertEquals("value", entry.getValue().getAsString());
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_expectThrows() throws Exception {
        // Use reflection to set stack top to a JsonElement that is NOT a JsonObject instance
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);

        // Create a mock JsonElement that is NOT a JsonObject instance and returns false for isJsonObject()
        JsonElement mockElement = mock(JsonElement.class);
        when(mockElement.isJsonObject()).thenReturn(false);
        // Also make sure it is not other types to avoid false positives
        when(mockElement.isJsonArray()).thenReturn(false);
        when(mockElement.isJsonPrimitive()).thenReturn(false);
        when(mockElement.isJsonNull()).thenReturn(false);

        stack[0] = mockElement;

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        IOException thrown = assertThrows(IOException.class, () -> jsonTreeReader.beginObject());
        assertTrue(thrown.getMessage().contains("Expected BEGIN_OBJECT"));
    }
}