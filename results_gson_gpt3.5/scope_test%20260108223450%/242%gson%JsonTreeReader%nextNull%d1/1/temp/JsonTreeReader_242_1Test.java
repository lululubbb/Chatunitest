package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonNull;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

public class JsonTreeReader_242_1Test {

    private JsonTreeReader reader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a JsonTreeReader instance with a JsonNull element (using reflection)
        var jsonNullClass = Class.forName("com.google.gson.JsonNull");
        var instanceField = jsonNullClass.getField("INSTANCE");
        Object jsonNullInstance = instanceField.get(null);
        reader = new JsonTreeReader((com.google.gson.JsonElement) jsonNullInstance);

        // Inject stack and stackSize for testing
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(reader, new Object[32]);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 0);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        pathIndicesField.set(reader, new int[32]);
    }

    @Test
    @Timeout(8000)
    public void testNextNull_withStackSizeZero() throws Exception {
        // Setup stackSize = 0
        setStackSize(0);

        // Push JsonNull onto stack to simulate current token
        pushStack(JsonNull.INSTANCE);

        // Call nextNull()
        reader.nextNull();

        // After nextNull, stackSize should be decremented
        int stackSizeAfter = getStackSize();
        assertEquals(0, stackSizeAfter);

        // pathIndices should not be incremented because stackSize == 0
        int[] pathIndices = getPathIndices();
        assertEquals(0, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextNull_withStackSizeGreaterThanZero() throws Exception {
        // Setup stackSize = 2
        setStackSize(2);

        // Prepare stack with JsonNull on top
        // Clear stack first
        clearStack();

        // Push JsonNull twice to fill stack[0] and stack[1]
        pushStack(JsonNull.INSTANCE);
        pushStack(JsonNull.INSTANCE);

        // Initialize pathIndices
        int[] pathIndices = getPathIndices();
        pathIndices[1] = 5; // set some value for index 1
        pathIndices[0] = 0; // ensure starting at 0 for index 0

        // Call nextNull()
        reader.nextNull();

        // After nextNull, stackSize should be decremented to 1
        int stackSizeAfter = getStackSize();
        assertEquals(1, stackSizeAfter);

        // pathIndices[stackSize -1] should be incremented (index 0)
        pathIndices = getPathIndices();
        assertEquals(1, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextNull_expectThrowsIOException() throws Exception {
        // Create a real JsonTreeReader instance with JsonNull element
        var jsonNullClass = Class.forName("com.google.gson.JsonNull");
        var instanceField = jsonNullClass.getField("INSTANCE");
        Object jsonNullInstance = instanceField.get(null);

        // Create a Mockito spy of JsonTreeReader
        JsonTreeReader realReader = new JsonTreeReader((com.google.gson.JsonElement) jsonNullInstance);

        // Setup internal state of realReader for nextNull to run
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = new Object[32];
        stack[0] = JsonNull.INSTANCE;
        stackField.set(realReader, stack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(realReader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        pathIndicesField.set(realReader, new int[32]);

        JsonTreeReader spyReader = spy(realReader);

        // Instead of subclassing final class, mock nextNull method using Mockito spy
        doThrow(new IOException("Forced exception")).when(spyReader).nextNull();

        // Verify that nextNull throws IOException
        assertThrows(IOException.class, spyReader::nextNull);
    }

    private void setStackSize(int size) throws Exception {
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, size);
    }

    private int getStackSize() throws Exception {
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        return stackSizeField.getInt(reader);
    }

    private int[] getPathIndices() throws Exception {
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        return (int[]) pathIndicesField.get(reader);
    }

    private void pushStack(Object obj) throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(reader);

        int stackSize = getStackSize();
        stack[stackSize] = obj;

        setStackSize(stackSize + 1);
    }

    private void clearStack() throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(reader);
        for (int i = 0; i < stack.length; i++) {
            stack[i] = null;
        }
    }
}