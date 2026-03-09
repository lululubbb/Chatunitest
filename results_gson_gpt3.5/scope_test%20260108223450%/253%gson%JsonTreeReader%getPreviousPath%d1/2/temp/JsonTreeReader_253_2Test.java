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

import java.lang.reflect.Method;

public class JsonTreeReader_253_2Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    jsonTreeReader = new JsonTreeReader(new JsonNull());
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_emptyStack() throws Exception {
    // stackSize = 0, expect path "$"
    setField(jsonTreeReader, "stackSize", 0);
    String result = invokeGetPath(true);
    assertEquals("$", result);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_singleObject() throws Exception {
    JsonObject obj = new JsonObject();
    // Ensure stack has length at least 1
    Object[] stack = new Object[1];
    stack[0] = obj;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    // pathNames length at least 1
    String[] pathNames = new String[1];
    pathNames[0] = "name1";
    setField(jsonTreeReader, "pathNames", pathNames);
    // pathIndices length at least 1
    int[] pathIndices = new int[1];
    pathIndices[0] = 0;
    setField(jsonTreeReader, "pathIndices", pathIndices);
    String result = invokeGetPath(true);
    assertEquals("$.name1", result);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_arrayWithIndices() throws Exception {
    JsonArray array = new JsonArray();
    JsonPrimitive prim = new JsonPrimitive("x");
    Object[] stack = new Object[2];
    stack[0] = array;
    stack[1] = prim;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 2);
    String[] pathNames = new String[2];
    pathNames[0] = "arr";
    pathNames[1] = null;
    setField(jsonTreeReader, "pathNames", pathNames);
    int[] pathIndices = new int[2];
    pathIndices[0] = 3;
    pathIndices[1] = 5;
    setField(jsonTreeReader, "pathIndices", pathIndices);
    String result = invokeGetPath(true);
    // pathIndices[0] = 3 - 1 = 2, pathIndices[1] = 5 - 1 = 4
    // pathNames[0] = "arr", pathNames[1] = null
    // Expected: $.arr[2][4]
    assertEquals("$.arr[2][4]", result);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_pathNamesNullAndNonArray() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("value");
    Object[] stack = new Object[1];
    stack[0] = primitive;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    String[] pathNames = new String[1];
    pathNames[0] = null;
    setField(jsonTreeReader, "pathNames", pathNames);
    int[] pathIndices = new int[1];
    pathIndices[0] = 0;
    setField(jsonTreeReader, "pathIndices", pathIndices);
    String result = invokeGetPath(true);
    assertEquals("$", result);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_usePreviousPathFalse() throws Exception {
    JsonArray array = new JsonArray();
    Object[] stack = new Object[1];
    stack[0] = array;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 1);
    String[] pathNames = new String[1];
    pathNames[0] = null;
    setField(jsonTreeReader, "pathNames", pathNames);
    int[] pathIndices = new int[1];
    pathIndices[0] = 3;
    setField(jsonTreeReader, "pathIndices", pathIndices);
    String result = invokeGetPath(false);
    // pathIndices[0] = 3, no -1 because usePreviousPath is false
    assertEquals("$[3]", result);
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

  // Helper to invoke private getPath(boolean) method via reflection
  private String invokeGetPath(boolean usePreviousPath) {
    try {
      Method method = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
      method.setAccessible(true);
      return (String) method.invoke(jsonTreeReader, usePreviousPath);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_complexStack() throws Exception {
    JsonObject obj = new JsonObject();
    JsonArray array = new JsonArray();
    JsonPrimitive primitive = new JsonPrimitive("prim");

    Object[] stack = new Object[3];
    stack[0] = obj;
    stack[1] = array;
    stack[2] = primitive;

    String[] pathNames = new String[3];
    pathNames[0] = "objName";
    pathNames[1] = null;
    pathNames[2] = "primName";

    int[] pathIndices = new int[3];
    pathIndices[0] = 1;
    pathIndices[1] = 2;
    pathIndices[2] = 0;

    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 3);
    setField(jsonTreeReader, "pathNames", pathNames);
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String result = invokeGetPath(true);
    // For i=0: objName, pathIndices[0] = 1 - 1 = 0 -> $.objName[0]
    // For i=1: array, pathNames null, pathIndices[1] = 2 - 1 = 1 -> [1]
    // For i=2: primitive, pathNames "primName", pathIndices[2] = 0 - 1 = -1 < 0 so no index -> .primName
    assertEquals("$.objName[0][1].primName", result);
  }
}