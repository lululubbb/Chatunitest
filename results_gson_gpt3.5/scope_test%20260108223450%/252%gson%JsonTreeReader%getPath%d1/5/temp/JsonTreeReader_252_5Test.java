package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonTreeReader_252_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create dummy JsonElement to pass to constructor (can be null because we won't use it)
    jsonTreeReader = new JsonTreeReader(Mockito.mock(JsonElement.class));
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack() throws Exception {
    setStackSize(0);
    String path = invokeGetPath(false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withJsonArrayAndIterator_usePreviousPathFalse() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> iterator = Arrays.asList("a", "b").iterator();

    // stack = [JsonArray, Iterator]
    setStack(new Object[] {jsonArray, iterator});
    setStackSize(2);
    setPathIndices(new int[] {0, 1});
    setPathNames(new String[32]);

    String path = invokeGetPath(false);
    // usePreviousPath false, so pathIndex not decremented, should append [1]
    assertEquals("$[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withJsonArrayAndIterator_usePreviousPathTrue_pathIndexGreaterThanZero_lastElement() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> iterator = Arrays.asList("a", "b").iterator();

    // stack = [JsonArray, Iterator]
    setStack(new Object[] {jsonArray, iterator});
    setStackSize(2);
    int[] pathIndices = new int[32];
    pathIndices[1] = 2; // > 0
    setPathIndices(pathIndices);
    setPathNames(new String[32]);

    String path = invokeGetPath(true);
    // usePreviousPath true and last element, pathIndex decremented by 1
    assertEquals("$[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withJsonArrayAndIterator_usePreviousPathTrue_pathIndexZero() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> iterator = Arrays.asList("a", "b").iterator();

    // stack = [JsonArray, Iterator]
    setStack(new Object[] {jsonArray, iterator});
    setStackSize(2);
    int[] pathIndices = new int[32];
    pathIndices[1] = 0; // zero, no decrement
    setPathIndices(pathIndices);
    setPathNames(new String[32]);

    String path = invokeGetPath(true);
    // pathIndex is 0, no decrement
    assertEquals("$[0]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withJsonObjectAndIterator_pathNameNull() throws Exception {
    JsonObject jsonObject = new JsonObject();
    Iterator<?> iterator = Arrays.asList("a", "b").iterator();

    // stack = [JsonObject, Iterator]
    setStack(new Object[] {jsonObject, iterator});
    setStackSize(2);
    setPathIndices(new int[32]);
    String[] pathNames = new String[32];
    pathNames[1] = null;
    setPathNames(pathNames);

    String path = invokeGetPath(false);
    // pathName null, so just append '.'
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withJsonObjectAndIterator_pathNameNonNull() throws Exception {
    JsonObject jsonObject = new JsonObject();
    Iterator<?> iterator = Arrays.asList("a", "b").iterator();

    // stack = [JsonObject, Iterator]
    setStack(new Object[] {jsonObject, iterator});
    setStackSize(2);
    setPathIndices(new int[32]);
    String[] pathNames = new String[32];
    pathNames[1] = "fieldName";
    setPathNames(pathNames);

    String path = invokeGetPath(false);
    // pathName non-null, so append '.' + fieldName
    assertEquals("$.fieldName", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_complexStack() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> iterator1 = Arrays.asList("a", "b").iterator();
    JsonObject jsonObject = new JsonObject();
    Iterator<?> iterator2 = Arrays.asList("x", "y").iterator();

    // stack = [JsonArray, Iterator, JsonObject, Iterator]
    setStack(new Object[] {jsonArray, iterator1, jsonObject, iterator2});
    setStackSize(4);

    int[] pathIndices = new int[32];
    pathIndices[1] = 3;
    pathIndices[3] = 5;
    setPathIndices(pathIndices);

    String[] pathNames = new String[32];
    pathNames[3] = "key";
    setPathNames(pathNames);

    String path = invokeGetPath(true);

    // For i=0 JsonArray + i=1 Iterator: usePreviousPath true, pathIndex=3 > 0 and i=1 == stackSize-1 => decrement to 2
    // Append [2]
    // For i=2 JsonObject + i=3 Iterator: append '.' + "key"
    // Result: "$[2].key"
    assertEquals("$[2].key", path);
  }

  // Helper methods to set private fields via reflection

  private void setStack(Object[] newStack) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(jsonTreeReader, newStack);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, size);
  }

  private void setPathIndices(int[] newPathIndices) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(jsonTreeReader, newPathIndices);
  }

  private void setPathNames(String[] newPathNames) throws Exception {
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    pathNamesField.set(jsonTreeReader, newPathNames);
  }

  private String invokeGetPath(boolean usePreviousPath) throws Exception {
    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
    return (String) getPathMethod.invoke(jsonTreeReader, usePreviousPath);
  }
}