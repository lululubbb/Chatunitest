package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_239_6Test {

    private JsonTreeReader reader;
    private Method nextNameMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException, IOException {
        // Create a JsonObject with some properties for testing
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("foo", new JsonPrimitive("bar"));
        jsonObject.add("baz", new JsonPrimitive(123));

        reader = new JsonTreeReader(jsonObject);

        // Manually begin the object to move the reader into the object context
        reader.beginObject();

        // Access private nextName(boolean) method via reflection
        nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testNextNameReturnsFirstName() throws Throwable {
        // Call nextName(false) to get the first property name
        String name = (String) nextNameMethod.invoke(reader, false);
        assertEquals("foo", name);

        // The pathNames[stackSize - 1] should be set to "foo"
        // Before calling nextName again, call reader.nextString() to consume the value
        reader.nextString();

        // Next call should return "baz"
        String nextName = (String) nextNameMethod.invoke(reader, false);
        assertEquals("baz", nextName);
    }

    @Test
    @Timeout(8000)
    public void testNextNameSkipNameTrue() throws Throwable {
        // Calling with skipName = true should skip setting pathNames
        String name = (String) nextNameMethod.invoke(reader, true);
        assertEquals("foo", name);

        // Consume the value to move to next property
        reader.nextString();

        // The second call with skipName = true returns next property name
        String nextName = (String) nextNameMethod.invoke(reader, true);
        assertEquals("baz", nextName);
    }

    @Test
    @Timeout(8000)
    public void testNextNameThrowsIfNotObject() throws Throwable {
        // Create a JsonArray (not an object)
        JsonElement array = new com.google.gson.JsonArray();
        JsonTreeReader arrayReader = new JsonTreeReader(array);

        // Using reflection to invoke nextName on non-object should throw IllegalStateException
        Method nextNameMethodLocal = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethodLocal.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            nextNameMethodLocal.invoke(arrayReader, false);
        });
        // InvocationTargetException wraps the actual exception
        assertTrue(thrown.getCause() instanceof IllegalStateException);
    }

    @Test
    @Timeout(8000)
    public void testNextNameWithEmptyObjectThrows() throws Throwable {
        JsonObject emptyObject = new JsonObject();
        JsonTreeReader emptyReader = new JsonTreeReader(emptyObject);
        emptyReader.beginObject();

        Method nextNameMethodLocal = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethodLocal.setAccessible(true);

        // The first call to nextName will throw IllegalStateException because there are no names (no properties)
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            nextNameMethodLocal.invoke(emptyReader, false);
        });
        assertTrue(thrown.getCause() instanceof IllegalStateException);
    }

    @Test
    @Timeout(8000)
    public void testNextNameUpdatesPathNamesAndPathIndices() throws Throwable {
        // Initially pathNames and pathIndices arrays should be empty or default
        String firstName = (String) nextNameMethod.invoke(reader, false);
        assertEquals("foo", firstName);

        // Use reflection to get pathNames, pathIndices and stackSize fields and verify update
        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = (String[]) pathNamesField.get(reader);

        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(reader);

        assertEquals("foo", pathNames[stackSize - 1]);
        assertEquals(0, pathIndices[stackSize - 1]);
    }
}