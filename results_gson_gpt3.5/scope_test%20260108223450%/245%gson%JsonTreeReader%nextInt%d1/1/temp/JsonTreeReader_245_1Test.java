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

public class JsonTreeReader_245_1Test {

    private JsonTreeReader reader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a JsonPrimitive with integer value 42
        JsonPrimitive primitive = new JsonPrimitive(42);

        // Create instance of JsonTreeReader with null JsonElement (we will set stack manually)
        reader = new JsonTreeReader(null);

        // Use reflection to set private fields: stack, stackSize, pathIndices
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
        pathIndicesField.set(reader, pathIndices);

        // Mock peek() method to return JsonToken.NUMBER
        // We cannot mock instance methods on real objects directly without a framework like PowerMockito,
        // so we use reflection to override peek() by creating a subclass for testing.
        // Instead, we will use reflection to set a field so peek() uses the stack top.
        // peek() uses peekStack() and returns token based on the top element.
        // To simulate peek() returning NUMBER, we will override peekStack to return JsonPrimitive.
        // But peek() is public and final, so we cannot override easily.
        // Instead, we will use reflection to call nextInt and rely on stack content.

        // So we do not mock peek(), we rely on stack content being JsonPrimitive (which peek() uses).
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withNumberToken_returnsInt() throws Exception {
        // Call nextInt and expect 42 returned
        int result = reader.nextInt();
        assertEquals(42, result);

        // Verify stackSize decreased to 0
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(reader);
        assertEquals(0, stackSize);

        // Verify pathIndices[stackSize - 1] increment does not occur because stackSize == 0
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);
        // pathIndices[0] should remain 0 because stackSize was 1 before popStack()
        assertEquals(0, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withStringToken_returnsInt() throws Exception {
        // Prepare JsonPrimitive with string number "123"
        JsonPrimitive primitive = new JsonPrimitive("123");

        // Set stack and stackSize
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = primitive;
        stackField.set(reader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 1);

        // Call nextInt and expect 123 returned
        int result = reader.nextInt();
        assertEquals(123, result);

        // Verify stackSize decreased to 0
        int stackSize = stackSizeField.getInt(reader);
        assertEquals(0, stackSize);

        // Verify pathIndices unchanged
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);
        assertEquals(0, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextInt_withInvalidToken_throwsIllegalStateException() throws Exception {
        // Prepare an object that will cause peek() to return a token other than NUMBER or STRING
        // We can simulate this by setting stack top to JsonNull.INSTANCE, which peek() returns JsonToken.NULL
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = com.google.gson.JsonNull.INSTANCE;
        stackField.set(reader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 1);

        // Call nextInt and expect IllegalStateException
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            reader.nextInt();
        });
        assertTrue(thrown.getMessage().contains("Expected"));
        assertTrue(thrown.getMessage().contains("but was"));
    }

    @Test
    @Timeout(8000)
    public void testNextInt_incrementsPathIndicesWhenStackSizeGreaterThanZero() throws Exception {
        // Prepare JsonPrimitive with integer 7
        JsonPrimitive primitive = new JsonPrimitive(7);

        // Set stack with two elements so stackSize = 2
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = new JsonPrimitive(1); // dummy
        stackArray[1] = primitive;
        stackField.set(reader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, 2);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = 5;
        pathIndicesField.set(reader, pathIndices);

        // Call nextInt, should pop stack from 2 to 1, then increment pathIndices[0]
        int result = reader.nextInt();
        assertEquals(7, result);

        int stackSize = stackSizeField.getInt(reader);
        assertEquals(1, stackSize);

        // pathIndices[stackSize - 1] = pathIndices[0] incremented by 1
        assertEquals(6, pathIndices[0]);
    }
}