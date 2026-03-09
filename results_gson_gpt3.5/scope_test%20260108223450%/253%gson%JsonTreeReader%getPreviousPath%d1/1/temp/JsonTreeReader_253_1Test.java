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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonTreeReader_253_1Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_emptyStack() throws Exception {
    // stackSize = 0, should return "$"
    setField(jsonTreeReader, "stackSize", 0);
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_withObjectAndName() throws Exception {
    // Setup stack with an object and a name at the top
    setField(jsonTreeReader, "stackSize", 2);

    Object[] stack = new Object[32];
    stack[0] = new JsonObject();
    stack[1] = new JsonPrimitive("value");
    setField(jsonTreeReader, "stack", stack);

    String[] pathNames = new String[32];
    pathNames[0] = null;
    pathNames[1] = "keyName";
    setField(jsonTreeReader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 0;
    setField(jsonTreeReader, "pathIndices", pathIndices);

    // The last element in stack is a JsonPrimitive, so pathIndices and pathNames at index 1 are used
    // The stackSize is 2, so getPreviousPath looks at stackSize - 1 = 1

    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$.keyName", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_withArrayAndIndex() throws Exception {
    // Setup stack with an array and index at the top
    setField(jsonTreeReader, "stackSize", 2);

    Object[] stack = new Object[32];
    stack[0] = new JsonArray();
    stack[1] = new JsonPrimitive("value");
    setField(jsonTreeReader, "stack", stack);

    String[] pathNames = new String[32];
    pathNames[0] = null;
    pathNames[1] = null;
    setField(jsonTreeReader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 2; // index in first array element
    pathIndices[1] = 5; // index in second element (primitive)
    setField(jsonTreeReader, "pathIndices", pathIndices);

    // The getPreviousPath method uses stackSize - 1 = 1 for last element, which is a primitive
    // The pathIndices and pathNames at index 1 are used to build path

    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$[2][5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_mixedObjectArray() throws Exception {
    // Setup stack with mixed JsonObject and JsonArray with names and indices
    setField(jsonTreeReader, "stackSize", 4);

    Object[] stack = new Object[32];
    stack[0] = new JsonObject();
    stack[1] = new JsonArray();
    stack[2] = new JsonObject();
    stack[3] = new JsonPrimitive("value");
    setField(jsonTreeReader, "stack", stack);

    String[] pathNames = new String[32];
    pathNames[0] = "root";
    pathNames[1] = null;
    pathNames[2] = "child";
    pathNames[3] = null;
    setField(jsonTreeReader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 3;
    pathIndices[2] = 0;
    pathIndices[3] = 0;
    setField(jsonTreeReader, "pathIndices", pathIndices);

    // The getPreviousPath uses stackSize - 1 = 3 as last element (primitive), so pathNames[3] and pathIndices[3] are used
    // The path is built from indices and names up to stackSize - 1

    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$.root[3].child", path);
  }

  // Helper method to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonTreeReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}