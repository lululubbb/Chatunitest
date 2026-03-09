package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonTreeReader_255_2Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonElement to pass to the constructor (can be JsonNull.INSTANCE for simplicity)
    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);

    // Initialize stack, stackSize, pathNames, and pathIndices to consistent default values
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = JsonNull.INSTANCE; // root element
    stackField.set(jsonTreeReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1); // Must set to 1 for root element

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    pathNamesField.set(jsonTreeReader, new String[32]);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(jsonTreeReader, new int[32]);
  }

  @Test
    @Timeout(8000)
  void testLocationString_withEmptyPath() throws Exception {
    // By default, pathNames and pathIndices are empty, stackSize is 1 (root)
    // So getPath() should return "$"
    String expectedPath = "$";

    // Use reflection to invoke private method locationString()
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String result = (String) locationStringMethod.invoke(jsonTreeReader);

    assertEquals(" at path " + expectedPath, result);
  }

  @Test
    @Timeout(8000)
  void testLocationString_withPopulatedPath() throws Exception {
    // Setup internal state to simulate a path like $.foo[3].bar
    // pathNames and pathIndices arrays and stackSize

    // Use reflection to set private fields
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    // Set stackSize to 4 (root + foo + array + bar)
    stackSizeField.setInt(jsonTreeReader, 4);

    // Set pathNames with [null, "foo", null, "bar"]
    String[] pathNames = new String[32];
    pathNames[0] = null;     // root has null name
    pathNames[1] = "foo";
    pathNames[2] = null;     // array element has null name
    pathNames[3] = "bar";
    pathNamesField.set(jsonTreeReader, pathNames);

    // Set pathIndices with [0,0,3,0]
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 0;
    pathIndices[2] = 3;    // index 3 in array
    pathIndices[3] = 0;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    // Setup stack to match the path types (Object, JsonObject, JsonArray, JsonObject)
    Object[] stack = new Object[32];
    // stack[0] is root element, can be JsonNull.INSTANCE
    stack[0] = JsonNull.INSTANCE;
    // stack[1] is JsonObject (for "foo")
    stack[1] = mock(com.google.gson.JsonObject.class);
    // stack[2] is JsonArray (for [3])
    stack[2] = mock(com.google.gson.JsonArray.class);
    // stack[3] is JsonObject (for "bar")
    stack[3] = mock(com.google.gson.JsonObject.class);
    stackField.set(jsonTreeReader, stack);

    // The expected path string is "$.foo[3].bar"
    String expectedPath = "$.foo[3].bar";

    // Use reflection to invoke private method locationString()
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String result = (String) locationStringMethod.invoke(jsonTreeReader);

    assertEquals(" at path " + expectedPath, result);
  }

  @Test
    @Timeout(8000)
  void testLocationString_withArrayIndexOnly() throws Exception {
    // Setup internal state for path: $[2]
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    stackSizeField.setInt(jsonTreeReader, 2);

    String[] pathNames = new String[32];
    pathNames[0] = null;
    pathNames[1] = null;
    pathNamesField.set(jsonTreeReader, pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 2;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    // Setup stack to match the path types (Object, JsonArray)
    Object[] stack = new Object[32];
    stack[0] = JsonNull.INSTANCE;
    stack[1] = mock(com.google.gson.JsonArray.class);
    stackField.set(jsonTreeReader, stack);

    String expectedPath = "$[2]";

    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String result = (String) locationStringMethod.invoke(jsonTreeReader);

    assertEquals(" at path " + expectedPath, result);
  }
}