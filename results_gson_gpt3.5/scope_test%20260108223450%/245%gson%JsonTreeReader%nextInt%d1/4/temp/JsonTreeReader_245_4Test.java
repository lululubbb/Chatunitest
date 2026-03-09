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

public class JsonTreeReader_245_4Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonPrimitive with getAsInt stubbed
        JsonPrimitive jsonPrimitive = mock(JsonPrimitive.class);
        when(jsonPrimitive.getAsInt()).thenReturn(42);

        // Create instance with dummy JsonElement (null is ok because constructor is not shown)
        jsonTreeReader = new JsonTreeReader(null);

        // Use reflection to set private fields stack, stackSize, pathIndices
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = jsonPrimitive;
        stackField.set(jsonTreeReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Spy the instance to mock peek() method to return JsonToken.NUMBER by default
        jsonTreeReader = spy(jsonTreeReader);
        doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

        // We do not mock popStack() directly; instead, we let the real method run.
        // To ensure popStack works properly, we rely on the real method.
    }

    private Object invokePopStack() throws Exception {
        Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
        popStackMethod.setAccessible(true);
        return popStackMethod.invoke(jsonTreeReader);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_NumberToken_Success() throws Exception {
        // Remove spy to ensure real peek() and popStack() are called
        // Re-setup fields since spy replaced the initial setup
        JsonPrimitive jsonPrimitive = mock(JsonPrimitive.class);
        when(jsonPrimitive.getAsInt()).thenReturn(42);

        jsonTreeReader = new JsonTreeReader(null);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = jsonPrimitive;
        stackField.set(jsonTreeReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Spy to mock peek() only, let popStack() real
        jsonTreeReader = spy(jsonTreeReader);
        doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

        // Do not mock popStack() because it is private and cannot be stubbed directly

        int result = jsonTreeReader.nextInt();
        assertEquals(42, result);

        // Verify pathIndices incremented
        pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        assertEquals(1, pathIndices[0]);

        // Verify stackSize decremented
        int stackSize = stackSizeField.getInt(jsonTreeReader);
        assertEquals(0, stackSize);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_StringToken_Success() throws Exception {
        // Setup peek() to return STRING
        doReturn(JsonToken.STRING).when(jsonTreeReader).peek();

        // Setup stack with JsonPrimitive that returns int 123
        JsonPrimitive jsonPrimitive = mock(JsonPrimitive.class);
        when(jsonPrimitive.getAsInt()).thenReturn(123);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = jsonPrimitive;
        stackField.set(jsonTreeReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 5;
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Do not mock popStack() because it is private and cannot be stubbed directly

        int result = jsonTreeReader.nextInt();
        assertEquals(123, result);

        // pathIndices incremented
        pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        assertEquals(6, pathIndices[0]);

        // stackSize decremented
        int stackSize = stackSizeField.getInt(jsonTreeReader);
        assertEquals(0, stackSize);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_IllegalStateException() throws Exception {
        // Setup peek() to return BOOLEAN (invalid)
        doReturn(JsonToken.BOOLEAN).when(jsonTreeReader).peek();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            jsonTreeReader.nextInt();
        });
        assertTrue(thrown.getMessage().startsWith("Expected NUMBER but was BOOLEAN"));
    }

    @Test
    @Timeout(8000)
    public void testNextInt_PathIndicesNotIncrementedWhenStackEmpty() throws Exception {
        // Setup stack with JsonPrimitive that returns int 7
        JsonPrimitive jsonPrimitive = mock(JsonPrimitive.class);
        when(jsonPrimitive.getAsInt()).thenReturn(7);

        jsonTreeReader = new JsonTreeReader(null);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = jsonPrimitive;
        stackField.set(jsonTreeReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 10;
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Do not mock popStack() because it is private and cannot be stubbed directly
        doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

        int result = jsonTreeReader.nextInt();
        assertEquals(7, result);

        // pathIndices incremented only if stackSize > 0 after popStack
        pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        assertEquals(11, pathIndices[0]);

        int stackSize = stackSizeField.getInt(jsonTreeReader);
        assertEquals(0, stackSize);
    }
}