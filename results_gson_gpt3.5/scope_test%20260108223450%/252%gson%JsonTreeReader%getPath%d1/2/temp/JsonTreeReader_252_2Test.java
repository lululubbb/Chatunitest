package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_252_2Test {

  private JsonTreeReader reader;
  private Field stackField;
  private Field stackSizeField;
  private Field pathNamesField;
  private Field pathIndicesField;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement to instantiate JsonTreeReader (can be JsonNull)
    JsonElement element = JsonNull.INSTANCE;
    reader = new JsonTreeReader(element);

    // Access private fields by reflection
    stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);

    pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    // Access private getPath(boolean) method by reflection
    getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
  }

  private void setStack(Object[] stack, int stackSize) throws IllegalAccessException {
    stackField.set(reader, stack);
    stackSizeField.set(reader, stackSize);
  }

  private void setPathNames(String[] pathNames) throws IllegalAccessException {
    pathNamesField.set(reader, pathNames);
  }

  private void setPathIndices(int[] pathIndices) throws IllegalAccessException {
    pathIndicesField.set(reader, pathIndices);
  }

  private String invokeGetPath(boolean usePreviousPath) throws Exception {
    return (String) getPathMethod.invoke(reader, usePreviousPath);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack() throws Exception {
    setStack(new Object[32], 0);
    setPathNames(new String[32]);
    setPathIndices(new int[32]);
    String path = invokeGetPath(false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonObject_withPathName() throws Exception {
    JsonObject obj = new JsonObject();
    Object[] stack = new Object[32];
    stack[0] = obj;
    stack[1] = mock(Iterator.class);
    int stackSize = 2;

    String[] pathNames = new String[32];
    pathNames[1] = "name";

    int[] pathIndices = new int[32];

    setStack(stack, stackSize);
    setPathNames(pathNames);
    setPathIndices(pathIndices);

    String path = invokeGetPath(false);
    assertEquals("$.name", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonObject_nullPathName() throws Exception {
    JsonObject obj = new JsonObject();
    Object[] stack = new Object[32];
    stack[0] = obj;
    stack[1] = mock(Iterator.class);
    int stackSize = 2;

    String[] pathNames = new String[32];
    pathNames[1] = null;

    int[] pathIndices = new int[32];

    setStack(stack, stackSize);
    setPathNames(pathNames);
    setPathIndices(pathIndices);

    String path = invokeGetPath(false);
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonArray_usePreviousPathFalse() throws Exception {
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = array;
    stack[1] = mock(Iterator.class);
    int stackSize = 2;

    String[] pathNames = new String[32];
    int[] pathIndices = new int[32];
    pathIndices[1] = 5;

    setStack(stack, stackSize);
    setPathNames(pathNames);
    setPathIndices(pathIndices);

    String path = invokeGetPath(false);
    assertEquals("$[5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonArray_usePreviousPathTrue_pathIndexZero() throws Exception {
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = array;
    stack[1] = mock(Iterator.class);
    int stackSize = 2;

    String[] pathNames = new String[32];
    int[] pathIndices = new int[32];
    pathIndices[1] = 0;

    setStack(stack, stackSize);
    setPathNames(pathNames);
    setPathIndices(pathIndices);

    String path = invokeGetPath(true);
    assertEquals("$[0]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonArray_usePreviousPathTrue_pathIndexPositive_lastElement() throws Exception {
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = array;
    stack[1] = mock(Iterator.class);
    int stackSize = 2;

    String[] pathNames = new String[32];
    int[] pathIndices = new int[32];
    pathIndices[1] = 3;

    setStack(stack, stackSize);
    setPathNames(pathNames);
    setPathIndices(pathIndices);

    // i == stackSize - 1, pathIndex > 0, usePreviousPath true -> pathIndex decremented by 1
    String path = invokeGetPath(true);
    assertEquals("$[2]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_singleJsonArray_usePreviousPathTrue_pathIndexPositive_secondLastElement() throws Exception {
    JsonArray array = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = array;
    stack[1] = mock(Iterator.class);
    stack[2] = mock(Iterator.class);
    int stackSize = 3;

    String[] pathNames = new String[32];
    int[] pathIndices = new int[32];
    pathIndices[1] = 3;

    setStack(stack, stackSize);
    setPathNames(pathNames);
    setPathIndices(pathIndices);

    // i == stackSize - 2, pathIndex > 0, usePreviousPath true -> pathIndex decremented by 1
    String path = invokeGetPath(true);
    assertEquals("$[2]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_multipleMixedStack() throws Exception {
    JsonArray array1 = new JsonArray();
    JsonObject object = new JsonObject();
    JsonArray array2 = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = array1;
    stack[1] = mock(Iterator.class);
    stack[2] = object;
    stack[3] = mock(Iterator.class);
    stack[4] = array2;
    stack[5] = mock(Iterator.class);
    int stackSize = 6;

    String[] pathNames = new String[32];
    pathNames[3] = "objKey";

    int[] pathIndices = new int[32];
    pathIndices[1] = 1;
    pathIndices[5] = 4;

    setStack(stack, stackSize);
    setPathNames(pathNames);
    setPathIndices(pathIndices);

    String path = invokeGetPath(false);
    // Expected: $[1].objKey[4]
    assertEquals("$[1].objKey[4]", path);
  }
}