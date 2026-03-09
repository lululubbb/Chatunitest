package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_220_6Test {

  private JsonReader jsonReader;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    // Provide a non-null Reader to avoid NullPointerException
    Reader dummyReader = new StringReader("");
    jsonReader = new JsonReader(dummyReader);
    getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 0, should return "$"
    setField("stackSize", 0);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayWithoutUsePreviousPath() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 1, stack[0] = EMPTY_ARRAY, pathIndices[0] = 3
    setField("stackSize", 1);
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("pathIndices", new int[] {3});
    setField("pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$[3]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayWithUsePreviousPathDecrement() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 1, stack[0] = NONEMPTY_ARRAY, pathIndices[0] = 2, usePreviousPath = true, last element index = 0
    setField("stackSize", 1);
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("pathIndices", new int[] {2});
    setField("pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, true);
    assertEquals("$[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_objectWithName() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 1, stack[0] = NONEMPTY_OBJECT, pathNames[0] = "name"
    setField("stackSize", 1);
    setField("stack", new int[] {JsonScope.NONEMPTY_OBJECT});
    setField("pathIndices", new int[32]);
    setField("pathNames", new String[32]);
    setField("pathNames", new String[] {"name"});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.name", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_objectWithNullName() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 1, stack[0] = EMPTY_OBJECT, pathNames[0] = null
    setField("stackSize", 1);
    setField("stack", new int[] {JsonScope.EMPTY_OBJECT});
    setField("pathIndices", new int[32]);
    setField("pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_mixedStack() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 3
    // stack = [NONEMPTY_OBJECT, NONEMPTY_ARRAY, EMPTY_OBJECT]
    // pathIndices = [0, 5, 0]
    // pathNames = ["obj", null, "inner"]
    setField("stackSize", 3);
    setField("stack", new int[] {JsonScope.NONEMPTY_OBJECT, JsonScope.NONEMPTY_ARRAY, JsonScope.EMPTY_OBJECT});
    setField("pathIndices", new int[] {0, 5, 0});
    setField("pathNames", new String[] {"obj", null, "inner"});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    // Expected: $.obj[5].inner
    assertEquals("$.obj[5].inner", path);
  }

  private void setField(String fieldName, Object value) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(jsonReader, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}