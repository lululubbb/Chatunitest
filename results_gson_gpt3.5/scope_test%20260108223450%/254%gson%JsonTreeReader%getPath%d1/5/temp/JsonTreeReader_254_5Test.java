package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class JsonTreeReader_254_5Test {

  private JsonTreeReader jsonTreeReader;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
    // Initialize with a dummy JsonElement, will set stack manually later
    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);

    // Access private getPath(boolean) method
    getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Initialize stack, pathNames, and pathIndices arrays to match stackSize properly
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    for (int i = 0; i < stack.length; i++) {
      stack[i] = JsonNull.INSTANCE;
    }

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    for (int i = 0; i < pathNames.length; i++) {
      pathNames[i] = null;
    }

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    for (int i = 0; i < pathIndices.length; i++) {
      pathIndices[i] = 0;
    }

    // Set stackSize to 0 initially
    setStackSize(0);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 0, expect "$"
    setStackSize(0);
    String path = (String) getPathMethod.invoke(jsonTreeReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleElementOnStack() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // stackSize = 1, pathNames and pathIndices empty, expect "$"
    setStackSize(1);

    // Set stack[0] to JsonNull.INSTANCE (already done in setUp but ensure)
    setStackAt(0, JsonNull.INSTANCE);

    // Clear pathNames[0] and pathIndices[0]
    setPathName(0, null);
    setPathIndex(0, 0);

    String path = (String) getPathMethod.invoke(jsonTreeReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_objectWithName() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // stackSize = 2, pathNames[1] = "fieldName", pathIndices irrelevant
    setStackSize(2);

    // Set stack[0] to JsonObject, stack[1] to JsonObject (fix)
    setStackAt(0, new JsonObject());
    setStackAt(1, new JsonObject());

    setPathName(1, "fieldName");
    setPathIndex(1, 0);

    String path = (String) getPathMethod.invoke(jsonTreeReader, false);
    assertEquals("$.fieldName", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayWithIndex() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // stackSize = 2, pathIndices[1] = 3, pathNames[1] = null
    setStackSize(2);

    // Set stack[0] to JsonArray, stack[1] to JsonArray (fix)
    setStackAt(0, new JsonArray());
    setStackAt(1, new JsonArray());

    setPathName(1, null);
    setPathIndex(1, 3);

    String path = (String) getPathMethod.invoke(jsonTreeReader, false);
    assertEquals("$[3]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_mixedObjectAndArray() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // stackSize = 4
    // pathNames[1] = "obj"
    // pathIndices[2] = 5
    // pathNames[3] = "field"
    setStackSize(4);

    // stack[0] = JsonObject, stack[1] = JsonArray, stack[2] = JsonArray, stack[3] = JsonObject
    setStackAt(0, new JsonObject());
    setStackAt(1, new JsonArray());
    setStackAt(2, new JsonArray());
    setStackAt(3, new JsonObject());

    setPathName(1, "obj");
    setPathIndex(2, 5);
    setPathName(3, "field");

    String path = (String) getPathMethod.invoke(jsonTreeReader, false);
    assertEquals("$.obj[5].field", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withUsePreviousPathTrue() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // stackSize = 3
    // pathNames[1] = "obj"
    // pathIndices[2] = 2
    setStackSize(3);

    // stack[0] = JsonObject, stack[1] = JsonObject, stack[2] = JsonArray
    setStackAt(0, new JsonObject());
    setStackAt(1, new JsonObject());
    setStackAt(2, new JsonArray());

    setPathName(1, "obj");
    setPathIndex(2, 2);

    // usePreviousPath true: indices should be pathIndices[i] - 1
    String path = (String) getPathMethod.invoke(jsonTreeReader, true);
    assertEquals("$.obj[1]", path);
  }

  // Helper methods to set private fields via reflection

  private void setStackSize(int size) {
    try {
      Field f = JsonTreeReader.class.getDeclaredField("stackSize");
      f.setAccessible(true);
      f.setInt(jsonTreeReader, size);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setPathName(int index, String name) {
    try {
      Field f = JsonTreeReader.class.getDeclaredField("pathNames");
      f.setAccessible(true);
      String[] pathNames = (String[]) f.get(jsonTreeReader);
      pathNames[index] = name;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setPathIndex(int index, int value) {
    try {
      Field f = JsonTreeReader.class.getDeclaredField("pathIndices");
      f.setAccessible(true);
      int[] pathIndices = (int[]) f.get(jsonTreeReader);
      pathIndices[index] = value;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setStackAt(int index, Object value) {
    try {
      Field f = JsonTreeReader.class.getDeclaredField("stack");
      f.setAccessible(true);
      Object[] stack = (Object[]) f.get(jsonTreeReader);
      stack[index] = value;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}