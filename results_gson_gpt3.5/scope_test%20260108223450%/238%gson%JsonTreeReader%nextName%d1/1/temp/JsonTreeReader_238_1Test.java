package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

class JsonTreeReader_238_1Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonObject with one entry for testing
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    jsonTreeReader = new JsonTreeReader(jsonObject);

    // Use reflection to push the iterator of the entry set onto the stack
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();

    // Clear stack and push the JsonObject first, then the iterator
    // because JsonTreeReader expects the stack top to be the iterator over entries,
    // but the previous stack item should be the JsonObject itself.
    // This matches the internal stack structure used by JsonTreeReader.

    // Push the JsonObject first
    stack[0] = jsonObject;
    // Push the iterator second (top of stack)
    stack[1] = iterator;
    stackSizeField.setInt(jsonTreeReader, 2);

    // Also set pathNames and pathIndices arrays for completeness
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);

    // Initialize pathNames and pathIndices at top of stack
    pathNames[0] = null;
    pathIndices[0] = 0;
    pathNames[1] = null;
    pathIndices[1] = 0;
  }

  @Test
    @Timeout(8000)
  void nextName_shouldReturnKeyAndPushValue_whenSkipNameFalse() throws Exception {
    // Reset stack to initial state for this test
    setUp();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    String result = (String) nextNameMethod.invoke(jsonTreeReader, false);

    assertEquals("key", result);

    // Verify that pathNames[stackSize - 1] is set to "key"
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);

    assertEquals("key", pathNames[stackSize - 1]);

    // Verify that the stack top is now the value of the entry ("value" JsonPrimitive)
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    Object top = stack[stackSize - 1];
    assertNotNull(top);
    assertTrue(top instanceof com.google.gson.JsonPrimitive);
    assertEquals("value", ((com.google.gson.JsonPrimitive) top).getAsString());
  }

  @Test
    @Timeout(8000)
  void nextName_shouldReturnSkippedAndPushValue_whenSkipNameTrue() throws Exception {
    // Reset the stack for this test to initial state
    setUp();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    String result = (String) nextNameMethod.invoke(jsonTreeReader, true);

    assertEquals("key", result);

    // Verify that pathNames[stackSize - 1] is set to "<skipped>"
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);

    // The pathNames is set at stackSize - 2, not stackSize - 1
    // because push(entry.getValue()) increases stackSize by 1,
    // so the name is stored at previous stack frame
    assertEquals("<skipped>", pathNames[stackSize - 2]);

    // Verify that the stack top is now the value of the entry ("value" JsonPrimitive)
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    Object top = stack[stackSize - 1];
    assertNotNull(top);
    assertTrue(top instanceof com.google.gson.JsonPrimitive);
    assertEquals("value", ((com.google.gson.JsonPrimitive) top).getAsString());
  }

  @Test
    @Timeout(8000)
  void nextName_shouldThrowIOException_whenExpectFails() throws Exception {
    // Create a JsonArray and push its iterator on stack
    com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
    jsonArray.add("element");
    Iterator<?> iterator = jsonArray.iterator();

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Push the JsonArray first, then the iterator on top
    stack[0] = jsonArray;
    stack[1] = iterator;
    stackSizeField.setInt(jsonTreeReader, 2);

    // Also set pathNames and pathIndices arrays for completeness
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);

    pathNames[0] = null;
    pathIndices[0] = 0;
    pathNames[1] = null;
    pathIndices[1] = 0;

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    Throwable thrown = assertThrows(Throwable.class, () -> nextNameMethod.invoke(jsonTreeReader, false));
    // Because reflection wraps exceptions in InvocationTargetException, unwrap:
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IOException);
  }
}