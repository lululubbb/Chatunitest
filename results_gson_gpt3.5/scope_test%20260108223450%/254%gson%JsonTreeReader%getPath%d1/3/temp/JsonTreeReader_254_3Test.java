package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_254_3Test {

  private JsonTreeReader jsonTreeReader;
  private Method getPathBooleanMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    // Initialize with a JsonElement (empty JsonObject)
    jsonTreeReader = new JsonTreeReader(new JsonObject());

    // Access private getPath(boolean) method via reflection
    getPathBooleanMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathBooleanMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_defaultUsesFalse() {
    String path = jsonTreeReader.getPath();
    // Since stackSize is 1 after construction, path should be "$"
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withEmptyStack() throws Exception {
    // Clear stackSize to 0 to test empty stack path
    setField(jsonTreeReader, "stackSize", 0);
    String path = (String) getPathBooleanMethod.invoke(jsonTreeReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withRootArray() throws Exception {
    // Push a JsonArray on stack to simulate root array
    JsonArray array = new JsonArray();
    setField(jsonTreeReader, "stackSize", 1);
    setField(jsonTreeReader, "stack", new Object[]{array});
    setField(jsonTreeReader, "pathIndices", new int[]{0}); // Set to 0, not -1
    setField(jsonTreeReader, "pathNames", new String[32]);

    String path = (String) getPathBooleanMethod.invoke(jsonTreeReader, false);
    // For root array, path should be "$[0]"
    assertEquals("$[0]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withNestedObjectAndArray() throws Exception {
    // Setup stack with root object and nested array
    JsonObject rootObject = new JsonObject();
    JsonArray nestedArray = new JsonArray();

    Object[] stack = new Object[32];
    stack[0] = rootObject;
    stack[1] = nestedArray;

    String[] pathNames = new String[32];
    pathNames[0] = "root";
    pathNames[1] = null;

    int[] pathIndices = new int[32];
    pathIndices[0] = -1; // root object -1
    pathIndices[1] = 2;  // nested array index 2

    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 2);
    setField(jsonTreeReader, "pathNames", pathNames);
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String path = (String) getPathBooleanMethod.invoke(jsonTreeReader, false);
    // Expected path: $.root[2]
    assertEquals("$.root[2]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withUsePreviousPathTrue() throws Exception {
    // Setup stack with one object and pathNames
    JsonObject rootObject = new JsonObject();

    Object[] stack = new Object[32];
    stack[0] = rootObject;

    String[] pathNames = new String[32];
    pathNames[0] = "previousName";

    int[] pathIndices = new int[32];
    pathIndices[0] = -1; // -1 for object

    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    setField(jsonTreeReader, "pathNames", pathNames);
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String path = (String) getPathBooleanMethod.invoke(jsonTreeReader, true);
    // Expected path: $.previousName
    assertEquals("$.previousName", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withNullPathNamesAndArrayIndex() throws Exception {
    // Setup stack with JsonArray and null pathNames element
    JsonArray array = new JsonArray();

    Object[] stack = new Object[32];
    stack[0] = array;

    String[] pathNames = new String[32];
    pathNames[0] = null;

    int[] pathIndices = new int[32];
    pathIndices[0] = 3; // set directly to 3

    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    setField(jsonTreeReader, "pathNames", pathNames);
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String path = (String) getPathBooleanMethod.invoke(jsonTreeReader, false);
    // Expected path: $[3]
    assertEquals("$[3]", path);
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = JsonTreeReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private static Object getField(Object target, String fieldName) {
    try {
      java.lang.reflect.Field field = JsonTreeReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}