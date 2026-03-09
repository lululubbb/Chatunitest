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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeReader_254_4Test {
  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() {
    JsonObject root = new JsonObject();
    root.add("primitive", new JsonPrimitive("value"));
    root.add("null", JsonNull.INSTANCE);
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive(1));
    array.add(new JsonPrimitive(2));
    root.add("array", array);
    jsonTreeReader = new JsonTreeReader(root);
  }

  @Test
    @Timeout(8000)
  void testGetPath_emptyStack() throws Exception {
    // Clear stackSize to 0 to simulate empty stack
    setField(jsonTreeReader, "stackSize", 0);
    String path = (String) invokeGetPath(jsonTreeReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_singleObject() throws Exception {
    // Simulate stack with one JsonObject
    setField(jsonTreeReader, "stackSize", 1);
    setField(jsonTreeReader, "stack", new Object[]{new JsonObject()});
    setField(jsonTreeReader, "pathNames", new String[]{null});
    setField(jsonTreeReader, "pathIndices", new int[]{-1});
    String path = (String) invokeGetPath(jsonTreeReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_objectWithName() throws Exception {
    setField(jsonTreeReader, "stackSize", 2);
    Object[] stack = new Object[32];
    stack[0] = new JsonObject();
    stack[1] = new JsonObject(); // must be JsonObject to build path with name
    setField(jsonTreeReader, "stack", stack);
    String[] pathNames = new String[32];
    pathNames[0] = null; // root element has no name
    pathNames[1] = "child";
    setField(jsonTreeReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = -1;
    pathIndices[1] = -1;
    setField(jsonTreeReader, "pathIndices", pathIndices);

    // Also set pathIndices[1] to -1 explicitly to avoid index usage
    // Also set stack[1] to JsonObject as expected

    String path = (String) invokeGetPath(jsonTreeReader, false);
    assertEquals("$['child']", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_arrayWithIndices() throws Exception {
    setField(jsonTreeReader, "stackSize", 3);
    Object[] stack = new Object[32];
    stack[0] = new JsonObject();
    stack[1] = new JsonArray();
    stack[2] = new JsonArray(); // must be JsonArray to build path with indices
    setField(jsonTreeReader, "stack", stack);
    String[] pathNames = new String[32];
    pathNames[0] = null; // root element has no name
    pathNames[1] = null;
    pathNames[2] = null;
    setField(jsonTreeReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = -1;
    pathIndices[1] = 5;
    pathIndices[2] = 2;
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String path = (String) invokeGetPath(jsonTreeReader, false);
    assertEquals("$[5][2]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withUsePreviousPathTrue() throws Exception {
    setField(jsonTreeReader, "stackSize", 2);
    Object[] stack = new Object[32];
    stack[0] = new JsonObject();
    stack[1] = new JsonObject();
    setField(jsonTreeReader, "stack", stack);
    String[] pathNames = new String[32];
    pathNames[0] = null; // root element has no name
    pathNames[1] = "child";
    setField(jsonTreeReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = -1;
    pathIndices[1] = -1;
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String path = (String) invokeGetPath(jsonTreeReader, true);
    assertEquals("$['child']", path);
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonTreeReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Object invokeGetPath(JsonTreeReader instance, boolean usePreviousPath) {
    try {
      Method method = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
      method.setAccessible(true);
      return method.invoke(instance, usePreviousPath);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}