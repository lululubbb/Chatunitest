package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_252_4Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonElement to pass to constructor, can be empty JsonObject
    JsonObject root = new JsonObject();
    jsonTreeReader = new JsonTreeReader(root);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack() throws Exception {
    // stackSize = 0, so path should be just "$"
    setField("stackSize", 0);
    String path = invokeGetPath(true);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonArray_withIterator_usePreviousPathTrue_indexDecremented() throws Exception {
    // Prepare stack with JsonArray followed by Iterator, pathIndices > 0, usePreviousPath true, i == stackSize - 1
    JsonArray jsonArray = new JsonArray();
    Iterator<JsonElement> iterator = Arrays.<JsonElement>asList(new JsonPrimitive("a")).iterator();

    setField("stack", new Object[] {jsonArray, iterator});
    setField("stackSize", 2);
    setField("pathIndices", new int[] {0, 1});
    setField("pathNames", new String[32]);

    String path = invokeGetPath(true);
    // The index is decremented from 1 to 0, so path should be $[0]
    assertEquals("$[0]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonArray_withIterator_usePreviousPathFalse_noDecrement() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<JsonElement> iterator = Arrays.<JsonElement>asList(new JsonPrimitive("a")).iterator();

    setField("stack", new Object[] {jsonArray, iterator});
    setField("stackSize", 2);
    setField("pathIndices", new int[] {0, 1});
    setField("pathNames", new String[32]);

    String path = invokeGetPath(false);
    // No decrement, so index remains 1
    assertEquals("$[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_jsonArray_iterator_iEqualsStackSizeMinus2_usePreviousPathTrue() throws Exception {
    // stackSize = 4, i=2, i == stackSize - 2
    JsonArray jsonArray = new JsonArray();
    Iterator<JsonElement> iterator = Arrays.<JsonElement>asList(new JsonPrimitive("a")).iterator();
    // Fourth element can be anything (simulate next element on stack)
    JsonPrimitive nextElement = new JsonPrimitive("next");

    Object[] stack = new Object[32];
    stack[0] = new JsonPrimitive("dummy");
    stack[1] = jsonArray;
    stack[2] = iterator;
    stack[3] = nextElement;

    setField("stack", stack);
    setField("stackSize", 4);
    int[] pathIndices = new int[32];
    pathIndices[2] = 2;
    setField("pathIndices", pathIndices);
    setField("pathNames", new String[32]);

    String path = invokeGetPath(true);
    // index decremented from 2 to 1
    assertEquals("$[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_jsonObject_withIterator_andPathName() throws Exception {
    JsonObject jsonObject = new JsonObject();
    Iterator<Map.Entry<String, JsonElement>> iterator = Arrays.<Map.Entry<String, JsonElement>>asList(
        new AbstractMap.SimpleEntry<>("key", new JsonPrimitive("value"))).iterator();

    Object[] stack = new Object[32];
    stack[0] = jsonObject;
    stack[1] = iterator;

    setField("stack", stack);
    setField("stackSize", 2);
    setField("pathIndices", new int[32]);
    String[] pathNames = new String[32];
    pathNames[1] = "keyName";
    setField("pathNames", pathNames);

    String path = invokeGetPath(false);
    // Should append .keyName
    assertEquals("$.keyName", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_jsonObject_withIterator_nullPathName() throws Exception {
    JsonObject jsonObject = new JsonObject();
    Iterator<Map.Entry<String, JsonElement>> iterator = Arrays.<Map.Entry<String, JsonElement>>asList(
        new AbstractMap.SimpleEntry<>("key", new JsonPrimitive("value"))).iterator();

    Object[] stack = new Object[32];
    stack[0] = jsonObject;
    stack[1] = iterator;

    setField("stack", stack);
    setField("stackSize", 2);
    setField("pathIndices", new int[32]);
    String[] pathNames = new String[32];
    pathNames[1] = null;
    setField("pathNames", pathNames);

    String path = invokeGetPath(false);
    // Should append '.' but no name after it
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_mixedStack() throws Exception {
    JsonArray array1 = new JsonArray();
    Iterator<JsonElement> iter1 = Arrays.<JsonElement>asList(new JsonPrimitive("a")).iterator();
    JsonObject object1 = new JsonObject();
    Iterator<Map.Entry<String, JsonElement>> iter2 = Arrays.<Map.Entry<String, JsonElement>>asList(
        new AbstractMap.SimpleEntry<>("foo", new JsonPrimitive("bar"))).iterator();

    Object[] stack = new Object[32];
    stack[0] = array1;
    stack[1] = iter1;
    stack[2] = object1;
    stack[3] = iter2;

    setField("stack", stack);
    setField("stackSize", 4);

    int[] pathIndices = new int[32];
    pathIndices[1] = 3; // for array index
    setField("pathIndices", pathIndices);

    String[] pathNames = new String[32];
    pathNames[3] = "foo";
    setField("pathNames", pathNames);

    String path = invokeGetPath(false);
    // For array at index 0, pathIndex=3, no decrement because usePreviousPath is false
    // Then for object at index 2, append .foo
    assertEquals("$[3].foo", path);
  }

  private void setField(String fieldName, Object value) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonTreeReader, value);
  }

  private String invokeGetPath(boolean usePreviousPath) throws Exception {
    Method method = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    method.setAccessible(true);
    return (String) method.invoke(jsonTreeReader, usePreviousPath);
  }
}