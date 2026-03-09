package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonTreeReader_248_4Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    void setUp() {
        jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);
    }

    @Test
    @Timeout(8000)
    void skipValue_shouldCallNextName_whenPeekIsName() throws Exception {
        // Setup a JsonObject with one entry to properly initialize stack and iterators
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("testName", "value");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        // Initialize stack and stackSize properly for JsonObject
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Object[] stack = (Object[]) stackField.get(reader);
        stack[0] = jsonObject.entrySet().iterator(); // iterator for JsonObject
        stackSizeField.setInt(reader, 1);

        // Spy on the reader
        JsonTreeReader spyReader = Mockito.spy(reader);

        // Stub peek() to return NAME
        doReturn(JsonToken.NAME).when(spyReader).peek();

        // Call skipValue which internally calls nextName(true)
        spyReader.skipValue();

        // Verify peek() was called
        verify(spyReader).peek();

        // We cannot directly verify nextName(true) as it's private, but skipValue should complete without exception
    }

    @Test
    @Timeout(8000)
    void skipValue_shouldCallEndArray_whenPeekIsEndArray() throws IOException, NoSuchFieldException, IllegalAccessException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());

        // Initialize stack and stackSize properly to avoid popStack() errors
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Object[] stack = (Object[]) stackField.get(reader);
        stack[0] = new JsonArray();
        stackSizeField.setInt(reader, 1);

        JsonTreeReader spyReader = Mockito.spy(reader);
        doReturn(JsonToken.END_ARRAY).when(spyReader).peek();

        spyReader.skipValue();

        verify(spyReader).endArray();
    }

    @Test
    @Timeout(8000)
    void skipValue_shouldCallEndObject_whenPeekIsEndObject() throws IOException, NoSuchFieldException, IllegalAccessException {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();

        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Object[] stack = (Object[]) stackField.get(reader);
        stack[0] = jsonObject;
        stackSizeField.setInt(reader, 1);

        JsonTreeReader spyReader = Mockito.spy(reader);
        doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

        spyReader.skipValue();

        verify(spyReader).endObject();
    }

    @Test
    @Timeout(8000)
    void skipValue_shouldDoNothing_whenPeekIsEndDocument() throws IOException {
        JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
        doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

        spyReader.skipValue();

        // No interaction expected beyond peek
        verify(spyReader).peek();
        verifyNoMoreInteractions(spyReader);
    }

    @Test
    @Timeout(8000)
    void skipValue_shouldPopStackAndIncrementPathIndices_whenPeekIsOtherAndStackSizeGreaterThanZero() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);

        Object[] stack = (Object[]) stackField.get(reader);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);

        // Push one element to stack
        stack[0] = new JsonPrimitive("value");
        stackSizeField.setInt(reader, 1);
        pathIndices[0] = 5;

        JsonTreeReader spyReader = Mockito.spy(reader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        spyReader.skipValue();

        int stackSizeAfter = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSizeAfter);

        int pathIndexAfter = pathIndices[0];
        assertEquals(6, pathIndexAfter);
    }

    @Test
    @Timeout(8000)
    void skipValue_shouldPopStackAndNotIncrementPathIndices_whenStackSizeIsZero() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);

        // stackSize is zero
        stackSizeField.setInt(reader, 0);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);
        pathIndices[0] = 5;

        JsonTreeReader spyReader = Mockito.spy(reader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        spyReader.skipValue();

        int stackSizeAfter = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSizeAfter);

        int pathIndexAfter = pathIndices[0];
        assertEquals(5, pathIndexAfter);
    }
}