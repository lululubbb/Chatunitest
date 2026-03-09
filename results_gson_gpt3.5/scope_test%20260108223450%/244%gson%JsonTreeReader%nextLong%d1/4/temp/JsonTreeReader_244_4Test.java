package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeReader_244_4Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonPrimitive for the stack
        JsonPrimitive jsonPrimitive = mock(JsonPrimitive.class);
        when(jsonPrimitive.getAsLong()).thenReturn(123456789L);

        // Create a JsonTreeReader instance using reflection since constructor is package-private
        // Use a dummy JsonPrimitive as root element
        jsonTreeReader = new JsonTreeReader(jsonPrimitive);

        // Set stack and stackSize via reflection
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = jsonPrimitive;
        stackField.set(jsonTreeReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        // Set pathIndices array to all zeros
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndicesField.set(jsonTreeReader, pathIndices);
    }

    @Test
    @Timeout(8000)
    public void nextLong_withNumberToken_returnsLong() throws Exception {
        // Mock peek() to return NUMBER
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        long result = spyReader.nextLong();

        assertEquals(123456789L, result);

        // Verify stackSize decreased by 1
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSize);

        // Verify pathIndices[stackSize - 1] incremented if stackSize > 0 (here stackSize=0 so no increment)
        // So no exception or error expected
    }

    @Test
    @Timeout(8000)
    public void nextLong_withStringToken_returnsLong() throws Exception {
        JsonPrimitive jsonPrimitive = mock(JsonPrimitive.class);
        when(jsonPrimitive.getAsLong()).thenReturn(987654321L);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        // Setup stack and stackSize
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = jsonPrimitive;
        stackField.set(reader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndicesField.set(reader, pathIndices);

        JsonTreeReader spyReader = spy(reader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        long result = spyReader.nextLong();

        assertEquals(987654321L, result);

        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSize);
    }

    @Test
    @Timeout(8000)
    public void nextLong_withInvalidToken_throwsIllegalStateException() throws Exception {
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

        IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextLong);
        assertTrue(exception.getMessage().contains("Expected"));
        assertTrue(exception.getMessage().contains(JsonToken.NUMBER.toString()));
        assertTrue(exception.getMessage().contains(JsonToken.BEGIN_ARRAY.toString()));
    }

    @Test
    @Timeout(8000)
    public void nextLong_incrementsPathIndicesWhenStackSizeGreaterThanZero() throws Exception {
        JsonPrimitive jsonPrimitive = mock(JsonPrimitive.class);
        when(jsonPrimitive.getAsLong()).thenReturn(111L);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = jsonPrimitive;
        stackArray[1] = jsonPrimitive;
        stackField.set(reader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 2);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 5;
        pathIndicesField.set(reader, pathIndices);

        JsonTreeReader spyReader = spy(reader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        long result = spyReader.nextLong();

        assertEquals(111L, result);

        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(1, stackSize);

        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(6, updatedPathIndices[0]);
    }
}