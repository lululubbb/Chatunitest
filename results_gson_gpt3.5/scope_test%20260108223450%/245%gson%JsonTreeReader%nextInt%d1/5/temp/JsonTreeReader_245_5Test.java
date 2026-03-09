package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonTreeReader_245_5Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a JsonPrimitive with a known int value
        JsonPrimitive primitive = new JsonPrimitive(42);

        // Instantiate JsonTreeReader with the primitive element
        jsonTreeReader = new JsonTreeReader(null);

        // Set stack and stackSize
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitive;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        // Set pathIndices
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        pathIndicesField.set(jsonTreeReader, pathIndices);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withNumberToken_returnsInt() throws Exception {
        // Call nextInt()
        int result = jsonTreeReader.nextInt();

        assertEquals(42, result);

        // Verify stackSize decreased by 1
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSizeAfter = stackSizeField.getInt(jsonTreeReader);
        assertEquals(0, stackSizeAfter);

        // Verify pathIndices incremented
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        assertEquals(0, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withStringToken_returnsInt() throws Exception {
        // Set stack top to JsonPrimitive with string number "123"
        JsonPrimitive primitiveString = new JsonPrimitive("123");

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = primitiveString;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 5;
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Call nextInt()
        int result = jsonTreeReader.nextInt();

        assertEquals(123, result);

        // Verify stackSize decreased by 1
        int stackSizeAfter = stackSizeField.getInt(jsonTreeReader);
        assertEquals(0, stackSizeAfter);

        // Verify pathIndices incremented
        pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        assertEquals(5, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withInvalidToken_throwsIllegalStateException() throws Exception {
        // Set stack top to JsonNull to cause peek() to return JsonToken.NULL

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = JsonNull.INSTANCE;
        stackField.set(jsonTreeReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonTreeReader.nextInt());
        assertTrue(thrown.getMessage().contains("Expected NUMBER but was NULL"));
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withEmptyStack_throwsIllegalStateException() throws Exception {
        // stackSize = 0 means peek() will fail or return SENTINEL_CLOSED, but nextInt expects NUMBER or STRING

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 0);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonTreeReader.nextInt());
        assertTrue(thrown.getMessage().contains("Expected NUMBER but was"));
    }

    @Test
    @Timeout(8000)
    public void testNextInt_incrementsPathIndicesOnlyIfStackSizeGreaterThanZero() throws Exception {
        // Setup with stackSize = 1
        JsonPrimitive primitive = new JsonPrimitive(7);

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
        int[] pathIndices = new int[32];
        pathIndices[0] = 10;
        pathIndicesField.set(jsonTreeReader, pathIndices);

        int result = jsonTreeReader.nextInt();
        assertEquals(7, result);

        int stackSizeAfter = stackSizeField.getInt(jsonTreeReader);
        assertEquals(0, stackSizeAfter);

        int[] pathIndicesAfter = (int[]) pathIndicesField.get(jsonTreeReader);
        // Since stackSize after pop is 0, pathIndices should not increment further
        // But increment happens if stackSize > 0 after popStack(), so no increment here
        assertEquals(10, pathIndicesAfter[0]);
    }
}