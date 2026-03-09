package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_254_6Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() {
    JsonObject root = new JsonObject();
    jsonTreeReader = new JsonTreeReader(root);

    // Initialize stack with root element and stackSize = 1 to avoid empty stack issues
    Object[] stack = new Object[32];
    stack[0] = root;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);

    // Initialize pathNames and pathIndices arrays
    setField(jsonTreeReader, "pathNames", new String[32]);
    setField(jsonTreeReader, "pathIndices", new int[32]);
  }

  @Test
    @Timeout(8000)
  void testGetPath_emptyStack() throws Exception {
    // Clear stackSize to 0 to simulate empty stack
    setField(jsonTreeReader, "stackSize", 0);

    String path = invokeGetPath(jsonTreeReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withArrayIndex() throws Exception {
    // Setup stack with one JsonArray and pathIndices[0] = 3
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = array;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 3;
    setField(jsonTreeReader, "pathIndices", pathIndices);
    setField(jsonTreeReader, "pathNames", new String[32]);

    String path = invokeGetPath(jsonTreeReader, false);
    assertEquals("$[3]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withObjectName() throws Exception {
    // Setup stack with one JsonObject and pathNames[0] = "key"
    JsonObject object = new JsonObject();
    Object[] stack = new Object[32];
    stack[0] = object;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonTreeReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    pathNames[0] = "key";
    setField(jsonTreeReader, "pathNames", pathNames);

    String path = invokeGetPath(jsonTreeReader, false);
    assertEquals("$.key", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_mixedStack() throws Exception {
    // Setup stack with JsonObject and JsonArray, pathNames and pathIndices set accordingly
    JsonObject object = new JsonObject();
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = object;
    stack[1] = array;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 2);

    String[] pathNames = new String[32];
    pathNames[0] = "parent";
    pathNames[1] = null;
    setField(jsonTreeReader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 2;
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String path = invokeGetPath(jsonTreeReader, false);
    assertEquals("$.parent[2]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_usePreviousPathTrue() throws Exception {
    // When usePreviousPath true, pathIndices[i] - 1 is used
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = array;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 4; // so 4 - 1 = 3
    setField(jsonTreeReader, "pathIndices", pathIndices);
    setField(jsonTreeReader, "pathNames", new String[32]);

    String path = invokeGetPath(jsonTreeReader, true);
    assertEquals("$[3]", path);
  }

  // Helper to invoke private getPath(boolean)
  private String invokeGetPath(JsonTreeReader reader, boolean usePreviousPath) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    method.setAccessible(true);
    return (String) method.invoke(reader, usePreviousPath);
  }

  // Helper to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonTreeReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}