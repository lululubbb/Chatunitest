package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class JsonTreeReader_253_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Initialize with a JsonElement, here a JsonObject for simplicity
    jsonTreeReader = new JsonTreeReader(new JsonObject());
  }

  @Test
    @Timeout(8000)
  void testGetPreviousPath_emptyStack() throws Exception {
    // Clear stackSize to zero to simulate empty stack
    setField(jsonTreeReader, "stackSize", 0);
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  void testGetPreviousPath_withArrayIndex() throws Exception {
    // Setup stack with one JsonArray and pathIndices with index 3
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = array;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 3; // set to 3 so getPreviousPath returns index 3 ([3])
    setField(jsonTreeReader, "pathIndices", pathIndices);
    setField(jsonTreeReader, "pathNames", new String[32]);
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$[3]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPreviousPath_withObjectName() throws Exception {
    // Setup stack with one JsonObject and pathNames with "foo"
    JsonObject object = new JsonObject();
    Object[] stack = new Object[32];
    stack[0] = object;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonTreeReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    pathNames[0] = "foo";
    setField(jsonTreeReader, "pathNames", pathNames);
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$.foo", path);
  }

  @Test
    @Timeout(8000)
  void testGetPreviousPath_mixedStack() throws Exception {
    // Setup stack with JsonObject then JsonArray, pathNames and pathIndices set
    JsonObject object = new JsonObject();
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = object;
    stack[1] = array;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[0] = "obj";
    setField(jsonTreeReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[1] = 5; // so getPreviousPath returns index 5 ([5])
    setField(jsonTreeReader, "pathIndices", pathIndices);
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$.obj[5]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPreviousPath_withNullJsonElementInStack() throws Exception {
    // Setup stack with JsonNull element
    Object[] stack = new Object[32];
    stack[0] = JsonNull.INSTANCE;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    setField(jsonTreeReader, "pathNames", new String[32]);
    setField(jsonTreeReader, "pathIndices", new int[32]);
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$", path);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}