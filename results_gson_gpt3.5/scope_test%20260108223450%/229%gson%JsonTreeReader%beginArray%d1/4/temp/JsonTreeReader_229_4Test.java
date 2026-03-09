package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

class JsonTreeReaderBeginArrayTest {

  private JsonTreeReader jsonTreeReader;
  private JsonArray jsonArray;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("elem1"));
    jsonArray.add(new JsonPrimitive("elem2"));
    jsonTreeReader = new JsonTreeReader(jsonArray);

    // Use reflection to set stack and stackSize to simulate the state before beginArray is called
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = jsonArray;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = -1;
  }

  @Test
    @Timeout(8000)
  void testBeginArray_success() throws Exception {
    // Use reflection to invoke beginArray
    Method beginArrayMethod = JsonTreeReader.class.getDeclaredMethod("beginArray");
    beginArrayMethod.setAccessible(true);

    beginArrayMethod.invoke(jsonTreeReader);

    // Verify that stackSize increased by 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(2, stackSize);

    // Verify top of stack is an iterator over jsonArray
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    Object top = stack[stackSize - 1];
    assertTrue(top instanceof Iterator);

    // Verify pathIndices[stackSize - 1] == 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(0, pathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void testBeginArray_expectThrows() throws Exception {
    // Create a new JsonTreeReader instance and set its internal state as needed
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("not an array"));

    // Use reflection to set stack and stackSize to simulate wrong state
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = new JsonPrimitive("not an array");

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0] = -1;

    Method beginArrayMethod = JsonTreeReader.class.getDeclaredMethod("beginArray");
    beginArrayMethod.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        beginArrayMethod.invoke(reader);
      } catch (java.lang.reflect.InvocationTargetException e) {
        // unwrap IOException
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          throw (IOException) cause;
        }
        throw e;
      }
    });

    assertTrue(thrown.getMessage().contains("Expected BEGIN_ARRAY but was STRING"));
  }
}