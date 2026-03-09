package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_252_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement for constructor (can be null as we won't use it directly)
    jsonTreeReader = new JsonTreeReader(null);

    // Access and reset private fields stack, stackSize, pathNames, pathIndices
    setField(jsonTreeReader, "stack", new Object[32]);
    setField(jsonTreeReader, "pathNames", new String[32]);
    setField(jsonTreeReader, "pathIndices", new int[32]);
    setField(jsonTreeReader, "stackSize", 0);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack() throws Exception {
    // stackSize = 0, expect "$"
    setField(jsonTreeReader, "stackSize", 0);
    String path = invokeGetPath(jsonTreeReader, true);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonArray_withIterator_usePreviousPathFalse() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> iterator = ((List<String>)(List<?>)Arrays.asList("a", "b")).iterator();
    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stack[1] = iterator;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 2);
    setField(jsonTreeReader, "pathIndices", new int[32]);
    int[] pathIndices = new int[32];
    pathIndices[1] = 1;
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String path = invokeGetPath(jsonTreeReader, false);
    assertEquals("$[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonArray_withIterator_usePreviousPathTrue_pathIndexGreaterThanZero() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> iterator = ((List<String>)(List<?>)Arrays.asList("a", "b")).iterator();
    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stack[1] = iterator;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 2);

    int[] pathIndices = new int[32];
    pathIndices[1] = 2; // > 0
    setField(jsonTreeReader, "pathIndices", pathIndices);

    // i == stackSize - 1 == 1 here, so pathIndex should decrement by 1
    String path = invokeGetPath(jsonTreeReader, true);
    assertEquals("$[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonObject_withIterator_andPathName() throws Exception {
    JsonObject jsonObject = new JsonObject();
    Iterator<Map.Entry<String, JsonElement>> iterator = ((List<Map.Entry<String, JsonElement>>)(List<?>)Arrays.asList()).iterator();
    Object[] stack = new Object[32];
    stack[0] = jsonObject;
    stack[1] = iterator;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 2);

    String[] pathNames = new String[32];
    pathNames[1] = "foo";
    setField(jsonTreeReader, "pathNames", pathNames);

    String path = invokeGetPath(jsonTreeReader, false);
    assertEquals("$.foo", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_multipleMixedStackElements() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> arrayIterator = ((List<String>)(List<?>)Arrays.asList("a", "b")).iterator();
    JsonObject jsonObject = new JsonObject();
    Iterator<Map.Entry<String, JsonElement>> objectIterator = ((List<Map.Entry<String, JsonElement>>)(List<?>)Arrays.asList()).iterator();

    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stack[1] = arrayIterator;
    stack[2] = jsonObject;
    stack[3] = objectIterator;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 4);

    int[] pathIndices = new int[32];
    pathIndices[1] = 3;
    setField(jsonTreeReader, "pathIndices", pathIndices);

    String[] pathNames = new String[32];
    pathNames[3] = "bar";
    setField(jsonTreeReader, "pathNames", pathNames);

    String path = invokeGetPath(jsonTreeReader, false);
    // Expected: "$[3].bar"
    assertEquals("$[3].bar", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_decrementPathIndex_usePreviousPathTrue_lastButOneElement() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> arrayIterator = ((List<String>)(List<?>)Arrays.asList("a", "b")).iterator();
    Iterator<?> nextIterator = ((List<String>)(List<?>)Arrays.asList("x")).iterator();

    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stack[1] = arrayIterator;
    stack[2] = nextIterator;
    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 3);

    int[] pathIndices = new int[32];
    pathIndices[1] = 2; // > 0
    setField(jsonTreeReader, "pathIndices", pathIndices);

    // i == stackSize - 2 == 1 here, so pathIndex should decrement by 1
    String path = invokeGetPath(jsonTreeReader, true);
    assertEquals("$[1]", path);
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static String invokeGetPath(JsonTreeReader instance, boolean usePreviousPath) throws Exception {
    Method method = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    method.setAccessible(true);
    return (String) method.invoke(instance, usePreviousPath);
  }
}