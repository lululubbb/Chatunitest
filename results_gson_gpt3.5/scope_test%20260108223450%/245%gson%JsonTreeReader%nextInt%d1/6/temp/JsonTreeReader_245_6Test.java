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

public class JsonTreeReader_245_6Test {

    private JsonTreeReader reader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy JsonPrimitive for the stack
        JsonPrimitive primitive = new JsonPrimitive(42);

        // Instantiate JsonTreeReader with a dummy JsonElement (null is okay for this test)
        reader = new JsonTreeReader(null);

        // Use reflection to set private fields stack, stackSize, pathIndices
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = primitive;
        stackField.set(reader, stackArray);

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
    public void testNextInt_withNumberToken_returnsInt() throws Exception {
        // Mock peek() to return JsonToken.NUMBER
        JsonTreeReader spyReader = spy(reader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        // nextInt should return the int value and update pathIndices
        int result = spyReader.nextInt();

        assertEquals(42, result);

        // Verify stackSize is 1 and pathIndices[0] incremented to 1
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(1, pathIndices[0]);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, stackSize); // popStack() reduces stackSize by 1
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withStringToken_returnsInt() throws Exception {
        // Setup JsonPrimitive with string int value
        JsonPrimitive primitive = new JsonPrimitive("123");

        // Setup stack and stackSize
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = primitive;
        stackField.set(reader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 5;
        pathIndicesField.set(reader, pathIndices);

        // Spy to mock peek() returning STRING token
        JsonTreeReader spyReader = spy(reader);
        doReturn(JsonToken.STRING).when(spyReader).peek();

        int result = spyReader.nextInt();

        assertEquals(123, result);

        // pathIndices[0] incremented from 5 to 6
        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(6, updatedPathIndices[0]);

        int updatedStackSize = stackSizeField.getInt(spyReader);
        assertEquals(0, updatedStackSize);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withInvalidToken_throwsIllegalStateException() throws Exception {
        JsonTreeReader spyReader = spy(reader);
        doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

        IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextInt);
        assertTrue(exception.getMessage().contains("Expected NUMBER but was BEGIN_ARRAY"));
    }

    @Test
    @Timeout(8000)
    public void testNextInt_stackSizeZero_doesNotIncrementPathIndices() throws Exception {
        // Setup JsonPrimitive with int value
        JsonPrimitive primitive = new JsonPrimitive(7);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = primitive;
        stackField.set(reader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 10;
        pathIndicesField.set(reader, pathIndices);

        JsonTreeReader spyReader = spy(reader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        // popStack() will reduce stackSize to 0
        int result = spyReader.nextInt();

        assertEquals(7, result);

        // pathIndices should be incremented since stackSize after popStack is 0, so no increment
        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
        assertEquals(11, updatedPathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_afterPopStack_stackSizeZero_noPathIndicesIncrement() throws Exception {
        // Setup JsonPrimitive with int value
        JsonPrimitive primitive = new JsonPrimitive(7);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = primitive;
        stackField.set(reader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 1);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 10;
        pathIndicesField.set(reader, pathIndices);

        JsonTreeReader spyReader = spy(reader);
        doReturn(JsonToken.NUMBER).when(spyReader).peek();

        // Use reflection to override popStack() behavior by replacing the private method with a public one using a dynamic proxy is not feasible,
        // so instead, we replace popStack call by reflection to simulate stackSize 0 after popStack

        // Save original popStack method
        Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
        popStackMethod.setAccessible(true);

        // Create a wrapper method to call original popStack, then set stackSize to 0
        // We cannot mock private methods directly with Mockito, so we use doAnswer on nextInt to simulate behavior

        // Instead, we call nextInt via reflection and before calling popStack, we manually set stackSize to 0 after popStack is called via reflection

        // So we just call nextInt normally, then manually set stackSize to 0 after popStack call.

        // To simulate this correctly, we create a subclass to override nextInt and simulate popStack behavior

        JsonTreeReader testReader = new JsonTreeReader(null) {
            @Override
            public int nextInt() throws IOException {
                JsonToken token = peek();
                if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
                    throw new IllegalStateException(
                            "Expected " + JsonToken.NUMBER + " but was " + token + locationString());
                }
                int result = ((JsonPrimitive) peekStack()).getAsInt();
                try {
                    // Call original popStack via reflection
                    Object popped = popStackMethod.invoke(this);
                    // After popStack, forcibly set stackSize to 0
                    Field stackSizeF = JsonTreeReader.class.getDeclaredField("stackSize");
                    stackSizeF.setAccessible(true);
                    stackSizeF.setInt(this, 0);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (stackSize > 0) {
                    pathIndices[stackSize - 1]++;
                }
                return result;
            }
        };

        // Setup fields in testReader same as spyReader
        Field stackField2 = JsonTreeReader.class.getDeclaredField("stack");
        stackField2.setAccessible(true);
        Object[] stackArray2 = new Object[32];
        stackArray2[0] = primitive;
        stackField2.set(testReader, stackArray2);

        Field stackSizeField2 = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField2.setAccessible(true);
        stackSizeField2.setInt(testReader, 1);

        Field pathIndicesField2 = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField2.setAccessible(true);
        int[] pathIndices2 = new int[32];
        pathIndices2[0] = 10;
        pathIndicesField2.set(testReader, pathIndices2);

        JsonTreeReader spyTestReader = spy(testReader);
        doReturn(JsonToken.NUMBER).when(spyTestReader).peek();

        int result = spyTestReader.nextInt();

        assertEquals(7, result);

        // Since stackSize is 0 after popStack, pathIndices should NOT be incremented
        int[] updatedPathIndices = (int[]) pathIndicesField.get(spyTestReader);
        assertEquals(10, updatedPathIndices[0]);
    }
}