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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonTreeReader_244_2Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // We create a minimal JsonPrimitive to pass to constructor
        JsonPrimitive primitive = new JsonPrimitive(123L);
        jsonTreeReader = new JsonTreeReader(primitive);

        // Set stack to contain the JsonPrimitive and stackSize to 1
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        if (stack.length < 1) {
            stack = new Object[32];
        }
        stack[0] = primitive;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        // Set pathIndices to zeros
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        if (pathIndices.length < 1) {
            pathIndices = new int[32];
        }
        pathIndices[0] = 0;
        pathIndicesField.set(jsonTreeReader, pathIndices);
    }

    @Test
    @Timeout(8000)
    public void testNextLong_withNumberToken_returnsLongValue() throws Exception {
        // Mock peek() to return NUMBER token
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        // Reset pathIndices[0] to 0 before test
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
        pathIndices[0] = 0;
        pathIndicesField.set(spyReader, pathIndices);

        long result = spyReader.nextLong();

        assertEquals(123L, result);

        // Verify stackSize decreased by 1
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSize);

        // Verify pathIndices incremented (should not throw)
        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(1, updatedPathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextLong_withStringToken_returnsLongValue() throws Exception {
        // Setup stack with JsonPrimitive with numeric string
        JsonPrimitive primitive = new JsonPrimitive("456");
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        if (stack.length < 1) {
            stack = new Object[32];
        }
        stack[0] = primitive;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        // Reset pathIndices to 0 before test
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        if (pathIndices.length < 1) {
            pathIndices = new int[32];
        }
        pathIndices[0] = 0;
        pathIndicesField.set(jsonTreeReader, pathIndices);

        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        // Reset pathIndices[0] to 0 before test on spy
        int[] spyPathIndices = (int[]) pathIndicesField.get(spyReader);
        spyPathIndices[0] = 0;
        pathIndicesField.set(spyReader, spyPathIndices);

        long result = spyReader.nextLong();

        assertEquals(456L, result);

        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSize);

        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(1, updatedPathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextLong_withInvalidToken_throwsIllegalStateException() throws Exception {
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

        IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::nextLong);
        assertTrue(ex.getMessage().contains("Expected NUMBER but was BEGIN_ARRAY"));
    }

    @Test
    @Timeout(8000)
    public void testNextLong_decrementsStackSizeAndIncrementsPathIndices() throws Exception {
        // Setup stackSize > 1 to test incrementing pathIndices at stackSize-1 index
        JsonPrimitive primitive0 = new JsonPrimitive(789L);
        JsonPrimitive primitive1 = new JsonPrimitive("123");
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(jsonTreeReader);
        if (stack.length < 2) {
            stack = new Object[32];
        }
        stack[0] = primitive0;
        stack[1] = primitive1;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 2);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        if (pathIndices.length < 2) {
            pathIndices = new int[32];
        }
        pathIndices[1] = 5;
        pathIndicesField.set(jsonTreeReader, pathIndices);

        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        // Reset pathIndices[1] to 5 on spy before test
        int[] spyPathIndices = (int[]) pathIndicesField.get(spyReader);
        spyPathIndices[1] = 5;
        pathIndicesField.set(spyReader, spyPathIndices);

        // Reset stackSize to 2 on spy before test
        Field spyStackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        spyStackSizeField.setAccessible(true);
        spyStackSizeField.setInt(spyReader, 2);

        // Reset stack on spy before test
        Field spyStackField = JsonTreeReader.class.getDeclaredField("stack");
        spyStackField.setAccessible(true);
        Object[] spyStack = (Object[]) spyStackField.get(spyReader);
        if (spyStack.length < 2) {
            spyStack = new Object[32];
        }
        spyStack[0] = primitive0;
        spyStack[1] = primitive1;
        spyStackField.set(spyReader, spyStack);

        long result = spyReader.nextLong();

        assertEquals(789L, result);

        int stackSize = spyStackSizeField.getInt(spyReader);
        assertEquals(1, stackSize);

        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(6, updatedPathIndices[1]);
    }
}