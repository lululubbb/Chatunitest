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

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonTreeReader_240_6Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    void setUp() throws Exception {
        // Create a dummy JsonPrimitive for testing
        JsonPrimitive jsonPrimitive = new JsonPrimitive("testString");

        // Create instance of JsonTreeReader with a dummy JsonElement via reflection
        // Since constructor is public JsonTreeReader(JsonElement), but JsonElement is abstract,
        // we can pass null and manually set stack and stackSize.
        jsonTreeReader = new JsonTreeReader(null);

        // Use reflection to set private fields stack and stackSize
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = new Object[32];
        stackArray[0] = jsonPrimitive; // put JsonPrimitive on stack at position 0
        stackField.set(jsonTreeReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        // Set pathIndices array with -1 (default initial value)
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = new int[32];
        pathIndices[0] = -1; // initialize to -1 as in actual JsonTreeReader
        pathIndicesField.set(jsonTreeReader, pathIndices);

        // Initialize pathNames array with nulls
        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = new String[32];
        pathNamesField.set(jsonTreeReader, pathNames);
    }

    private void setStackAndStackSize(JsonTreeReader reader, Object topElement, int size) throws Exception {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArr = new Object[32];
        stackArr[0] = topElement;
        stackField.set(reader, stackArr);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(reader, size);
    }

    private void setPathIndices(JsonTreeReader reader, int[] pathIndices) throws Exception {
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        pathIndicesField.set(reader, pathIndices);
    }

    private int[] getPathIndices(JsonTreeReader reader) throws Exception {
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        return (int[]) pathIndicesField.get(reader);
    }

    @Test
    @Timeout(8000)
    void nextString_shouldReturnString_whenTokenIsString() throws Exception {
        // jsonTreeReader already has JsonPrimitive("testString") on stack at position 0 and stackSize=1

        String result = jsonTreeReader.nextString();
        assertEquals("testString", result);

        // Verify pathIndices[stackSize - 1] incremented from -1 to 0
        int[] pathIndices = getPathIndices(jsonTreeReader);
        assertEquals(0, pathIndices[0]); // incremented from -1 to 0
    }

    @Test
    @Timeout(8000)
    void nextString_shouldReturnString_whenTokenIsNumber() throws Exception {
        JsonPrimitive numberPrimitive = new JsonPrimitive(123);

        JsonTreeReader reader = new JsonTreeReader(null);
        setStackAndStackSize(reader, numberPrimitive, 1);

        int[] pathIndices = new int[32];
        pathIndices[0] = 3; // set to 3 to simulate prior index
        setPathIndices(reader, pathIndices);

        // Initialize pathNames to avoid NPE or unexpected state
        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = new String[32];
        pathNamesField.set(reader, pathNames);

        String result = reader.nextString();
        assertEquals("123", result);

        int[] updatedPathIndices = getPathIndices(reader);
        assertEquals(4, updatedPathIndices[0]); // incremented from 3 to 4
    }

    @Test
    @Timeout(8000)
    void nextString_shouldThrowIllegalStateException_whenTokenIsNotStringOrNumber() throws Exception {
        JsonPrimitive booleanPrimitive = new JsonPrimitive(true);

        JsonTreeReader reader = new JsonTreeReader(null);
        setStackAndStackSize(reader, booleanPrimitive, 1);

        // Set pathNames to simulate locationString() returning something
        Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = new String[32];
        pathNames[0] = "fieldName";
        pathNamesField.set(reader, pathNames);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, reader::nextString);
        assertTrue(thrown.getMessage().contains("Expected STRING but was BOOLEAN"));
    }
}