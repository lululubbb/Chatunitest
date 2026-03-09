package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeReader_252_1Test {

  private JsonTreeReader jsonTreeReader;
  private Field stackField;
  private Field stackSizeField;
  private Field pathIndicesField;
  private Field pathNamesField;
  private Method getPathMethod;

  @BeforeEach
  void setUp() throws Exception {
    jsonTreeReader = new JsonTreeReader(new JsonObject());

    stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);

    getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
  }

  private String invokeGetPath(boolean usePreviousPath) throws Exception {
    return (String) getPathMethod.invoke(jsonTreeReader, usePreviousPath);
  }

  @Test
    @Timeout(8000)
  void testGetPath_emptyStack() throws Exception {
    stackSizeField.setInt(jsonTreeReader, 0);
    String path = invokeGetPath(false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withJsonArrayAndIterator_usePreviousPathFalse() throws Exception {
    JsonArray array = new JsonArray();
    List<JsonElement> list = new ArrayList<>();
    list.add(new JsonObject());
    Iterator<JsonElement> iterator = list.iterator();

    Object[] stack = new Object[32];
    stack[0] = array;
    stack[1] = iterator;

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 2);

    int[] pathIndices = new int[32];
    pathIndices[1] = 0;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    String path = invokeGetPath(false);
    assertEquals("$[0]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withJsonArrayAndIterator_usePreviousPathTrue_pathIndexGreaterThanZero_lastElement() throws Exception {
    JsonArray array = new JsonArray();
    List<JsonElement> list = new ArrayList<>();
    list.add(new JsonObject());
    Iterator<JsonElement> iterator = list.iterator();

    Object[] stack = new Object[32];
    stack[0] = array;
    stack[1] = iterator;

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 2);

    int[] pathIndices = new int[32];
    pathIndices[1] = 1;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    String path = invokeGetPath(true);
    assertEquals("$[0]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withJsonArrayAndIterator_usePreviousPathTrue_pathIndexGreaterThanZero_secondLastElement() throws Exception {
    JsonArray array = new JsonArray();
    List<JsonElement> list = new ArrayList<>();
    list.add(new JsonObject());
    Iterator<JsonElement> iterator = list.iterator();

    Object[] stack = new Object[32];
    stack[0] = array;
    stack[1] = iterator;
    stack[2] = new JsonObject(); // extra element to make stackSize 3

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 3);

    int[] pathIndices = new int[32];
    pathIndices[1] = 1;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    String path = invokeGetPath(true);
    assertEquals("$[0]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withJsonObjectAndIterator_pathNameNull() throws Exception {
    JsonObject object = new JsonObject();
    List<String> list = new ArrayList<>();
    list.add("key");
    Iterator<String> iterator = list.iterator();

    Object[] stack = new Object[32];
    stack[0] = object;
    stack[1] = iterator;

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 2);

    String[] pathNames = new String[32];
    pathNames[1] = null;
    pathNamesField.set(jsonTreeReader, pathNames);

    String path = invokeGetPath(false);
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withJsonObjectAndIterator_pathNameNonNull() throws Exception {
    JsonObject object = new JsonObject();
    List<String> list = new ArrayList<>();
    list.add("key");
    Iterator<String> iterator = list.iterator();

    Object[] stack = new Object[32];
    stack[0] = object;
    stack[1] = iterator;

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 2);

    String[] pathNames = new String[32];
    pathNames[1] = "keyName";
    pathNamesField.set(jsonTreeReader, pathNames);

    String path = invokeGetPath(false);
    assertEquals("$.keyName", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_mixedStack() throws Exception {
    JsonArray array = new JsonArray();
    List<JsonElement> arrayIteratorList = new ArrayList<>();
    arrayIteratorList.add(new JsonObject());
    Iterator<JsonElement> arrayIterator = arrayIteratorList.iterator();

    JsonObject object = new JsonObject();
    List<String> objectIteratorList = new ArrayList<>();
    objectIteratorList.add("foo");
    Iterator<String> objectIterator = objectIteratorList.iterator();

    Object[] stack = new Object[32];
    stack[0] = array;
    stack[1] = arrayIterator;
    stack[2] = object;
    stack[3] = objectIterator;

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 4);

    int[] pathIndices = new int[32];
    pathIndices[1] = 2;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    String[] pathNames = new String[32];
    pathNames[3] = "foo";
    pathNamesField.set(jsonTreeReader, pathNames);

    String path = invokeGetPath(false);
    assertEquals("$[2].foo", path);
  }
}