package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class JsonTreeReader_251_1Test {

    private JsonTreeReader jsonTreeReader;
    private Method pushMethod;
    private Field stackField;
    private Field stackSizeField;
    private Field pathIndicesField;
    private Field pathNamesField;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a minimal JsonElement for constructor (using JsonNull.INSTANCE)
        jsonTreeReader = new JsonTreeReader(com.google.gson.JsonNull.INSTANCE);

        // Access private push method
        pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);

        // Access private fields
        stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);

        pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);

        // Reset stackSize to 0 before each test
        stackSizeField.setInt(jsonTreeReader, 0);
    }

    @Test
    @Timeout(8000)
    public void testPush_NormalWithinCapacity() throws Exception {
        Object element = new Object();

        // Initial stack size is 0
        int initialStackSize = stackSizeField.getInt(jsonTreeReader);
        Object[] stackBefore = (Object[]) stackField.get(jsonTreeReader);
        int lengthBefore = stackBefore.length;

        // Invoke push
        pushMethod.invoke(jsonTreeReader, element);

        // Verify stackSize incremented by 1
        int newStackSize = stackSizeField.getInt(jsonTreeReader);
        assertEquals(initialStackSize + 1, newStackSize);

        // Verify element pushed at correct position
        Object[] stackAfter = (Object[]) stackField.get(jsonTreeReader);
        assertSame(element, stackAfter[newStackSize - 1]);

        // Verify stack array length unchanged (no resize)
        assertEquals(lengthBefore, stackAfter.length);

        // Verify pathIndices and pathNames length unchanged
        int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
        assertEquals(lengthBefore, pathIndices.length);
        assertEquals(lengthBefore, pathNames.length);
    }

    @Test
    @Timeout(8000)
    public void testPush_ResizeWhenFull() throws Exception {
        // Fill the stack to capacity
        int capacity = ((Object[]) stackField.get(jsonTreeReader)).length;
        stackSizeField.setInt(jsonTreeReader, capacity);

        // Fill stack with dummy objects
        Object[] fullStack = new Object[capacity];
        for (int i = 0; i < capacity; i++) {
            fullStack[i] = new Object();
        }
        stackField.set(jsonTreeReader, fullStack);

        // Fill pathIndices and pathNames arrays to capacity
        int[] pathIndices = new int[capacity];
        Arrays.fill(pathIndices, 1);
        pathIndicesField.set(jsonTreeReader, pathIndices);

        String[] pathNames = new String[capacity];
        Arrays.fill(pathNames, "name");
        pathNamesField.set(jsonTreeReader, pathNames);

        Object newElement = new Object();

        // Invoke push, which should trigger resize
        pushMethod.invoke(jsonTreeReader, newElement);

        // After push, stackSize should be capacity + 1
        int newStackSize = stackSizeField.getInt(jsonTreeReader);
        assertEquals(capacity + 1, newStackSize);

        // Stack array length should have doubled
        Object[] newStack = (Object[]) stackField.get(jsonTreeReader);
        assertEquals(capacity * 2, newStack.length);

        // The newly pushed element should be last
        assertSame(newElement, newStack[newStackSize - 1]);

        // pathIndices and pathNames arrays should also have doubled length
        int[] newPathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
        String[] newPathNames = (String[]) pathNamesField.get(jsonTreeReader);
        assertEquals(capacity * 2, newPathIndices.length);
        assertEquals(capacity * 2, newPathNames.length);

        // The first capacity elements should be copied correctly
        for (int i = 0; i < capacity; i++) {
            assertEquals(1, newPathIndices[i]);
            assertEquals("name", newPathNames[i]);
            assertSame(fullStack[i], newStack[i]);
        }
    }
}