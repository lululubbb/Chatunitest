package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeReader_244_6Test {

    private JsonTreeReader reader;

    @BeforeEach
    void setUp() throws Exception {
        // Create a JsonPrimitive with a long value
        JsonPrimitive primitive = new JsonPrimitive(1234567890123L);
        // Create instance of JsonTreeReader with a dummy JsonPrimitive element
        reader = new JsonTreeReader(primitive);

        // Using reflection to set private fields stack, stackSize, pathIndices to simulate state
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitive;
        stackField.set(reader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        pathIndicesField.set(reader, pathIndices);
    }

    @Test
    @Timeout(8000)
    void nextLong_withNumberToken_returnsLong() throws Exception {
        // Mock peek() to return JsonToken.NUMBER
        JsonTreeReader spyReader = Mockito.spy(reader);
        Mockito.doReturn(JsonToken.NUMBER).when(spyReader).peek();

        long result = spyReader.nextLong();

        assertEquals(1234567890123L, result);

        // Verify stackSize decremented and pathIndices incremented
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSize);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(1, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    void nextLong_withStringToken_returnsLong() throws Exception {
        JsonPrimitive primitive = new JsonPrimitive("1234567890123");
        JsonTreeReader readerWithString = new JsonTreeReader(primitive);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitive;
        stackField.set(readerWithString, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(readerWithString, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        pathIndicesField.set(readerWithString, pathIndices);

        JsonTreeReader spyReader = Mockito.spy(readerWithString);
        Mockito.doReturn(JsonToken.STRING).when(spyReader).peek();

        long result = spyReader.nextLong();

        assertEquals(1234567890123L, result);

        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSize);

        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(1, updatedPathIndices[0]);
    }

    @Test
    @Timeout(8000)
    void nextLong_withInvalidToken_throwsIllegalStateException() throws Exception {
        JsonTreeReader spyReader = Mockito.spy(reader);
        Mockito.doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

        IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextLong);
        assertTrue(exception.getMessage().contains("Expected " + JsonToken.NUMBER + " but was " + JsonToken.BEGIN_OBJECT));
    }

    @Test
    @Timeout(8000)
    void nextLong_decrementsStackSizeAndIncrementsPathIndices() throws Exception {
        JsonPrimitive primitive = new JsonPrimitive(42L);
        JsonTreeReader readerInstance = new JsonTreeReader(primitive);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitive;
        stack[1] = primitive;
        stackField.set(readerInstance, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(readerInstance, 2);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 5;
        pathIndicesField.set(readerInstance, pathIndices);

        JsonTreeReader spyReader = Mockito.spy(readerInstance);
        Mockito.doReturn(JsonToken.NUMBER).when(spyReader).peek();

        long result = spyReader.nextLong();

        assertEquals(42L, result);

        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(1, stackSize);

        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(6, updatedPathIndices[0]);
    }
}