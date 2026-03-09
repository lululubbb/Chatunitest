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
import java.lang.reflect.Method;

public class JsonTreeReader_244_3Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonPrimitive with a long value
        JsonPrimitive primitive = new JsonPrimitive(123456789L);

        // Create JsonTreeReader with a dummy JsonElement (null is fine since we will override stack)
        jsonTreeReader = new JsonTreeReader(null);

        // Use reflection to set private fields:
        // stack = new Object[] {primitive}
        // stackSize = 1
        // pathIndices = new int[32] initialized to 0
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitive;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        pathIndicesField.set(jsonTreeReader, new int[32]);
    }

    @Test
    @Timeout(8000)
    public void testNextLong_withNumberToken_returnsLong() throws Exception {
        // Mock peek() to return JsonToken.NUMBER
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        long result = spyReader.nextLong();

        assertEquals(123456789L, result);

        // Verify stackSize decreased by 1 after popStack()
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSizeAfter = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSizeAfter);

        // Verify pathIndices[stackSize - 1] incremented does not happen because stackSize is 0
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
        // Since stackSize is 0, no increment, so pathIndices[0] == 0
        assertEquals(0, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextLong_withStringToken_returnsLong() throws Exception {
        // Create JsonPrimitive with string number
        JsonPrimitive primitive = new JsonPrimitive("987654321");
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitive;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        pathIndicesField.set(jsonTreeReader, new int[32]);

        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        long result = spyReader.nextLong();

        assertEquals(987654321L, result);

        // stackSize should be 0 after popStack()
        int stackSizeAfter = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSizeAfter);

        // pathIndices not incremented because stackSize == 0
        int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(0, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextLong_withInvalidToken_throwsIllegalStateException() throws Exception {
        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

        IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextLong);
        assertTrue(exception.getMessage().contains("Expected " + JsonToken.NUMBER + " but was " + JsonToken.BEGIN_ARRAY));
    }

    @Test
    @Timeout(8000)
    public void testNextLong_incrementsPathIndicesWhenStackSizeGreaterThanZero() throws Exception {
        // Setup stackSize = 2 to test increment of pathIndices[1]
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        // Setup stack array with 2 JsonPrimitives
        JsonPrimitive primitive1 = new JsonPrimitive(111L);
        JsonPrimitive primitive2 = new JsonPrimitive(222L);
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitive1;
        stack[1] = primitive2;

        // Setup pathIndices array with zeros
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[1] = 5; // initial value to verify increment

        // Create a JsonTreeReader instance and set fields
        JsonTreeReader testReader = new JsonTreeReader(null);
        stackField.set(testReader, stack);
        stackSizeField.setInt(testReader, 2);
        pathIndicesField.set(testReader, pathIndices);

        // Use reflection to call private peekStack() and popStack() methods
        Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
        peekStackMethod.setAccessible(true);
        Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
        popStackMethod.setAccessible(true);

        // Spy the testReader to mock peek() method to return NUMBER
        JsonTreeReader spyReader = spy(testReader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        // nextLong() calls peek(), peekStack(), popStack(), and increments pathIndices accordingly
        long result = spyReader.nextLong();

        assertEquals(222L, result);

        // After popStack(), stackSize should be 1
        int stackSizeAfter = stackSizeField.getInt(spyReader);
        assertEquals(1, stackSizeAfter);

        // pathIndices[stackSize - 1] is pathIndices[0], should be incremented by 1 from 0 to 1
        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(1, updatedPathIndices[0]);
        assertEquals(5, updatedPathIndices[1]); // unchanged
    }
}