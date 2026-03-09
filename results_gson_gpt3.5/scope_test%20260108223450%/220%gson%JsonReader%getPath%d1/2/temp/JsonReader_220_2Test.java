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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_220_2Test {
  private JsonReader jsonReader;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    // Provide a non-null Reader to avoid NullPointerException in constructor
    Reader dummyReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // EOF immediately
      }
      @Override
      public void close() {}
    };
    jsonReader = new JsonReader(dummyReader);
    getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStackSizeFalse() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 0, expect "$"
    setField("stackSize", 0);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStackSizeTrue() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 0, expect "$"
    setField("stackSize", 0);
    String path = (String) getPathMethod.invoke(jsonReader, true);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayUsePreviousPathFalse() throws InvocationTargetException, IllegalAccessException {
    // stack with one array scope, pathIndices = 2, usePreviousPath = false
    setField("stackSize", 1);
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("pathIndices", new int[] {2});
    setField("pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$[2]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayUsePreviousPathTruePathIndexGreaterThanZeroLastElement()
      throws InvocationTargetException, IllegalAccessException {
    // stack with one array scope, pathIndices = 2, usePreviousPath = true, last element => index decremented
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
    // stack with one object scope, pathNames[0] = "name"
    setField("stackSize", 1);
    setField("stack", new int[] {JsonScope.NONEMPTY_OBJECT});
    setField("pathIndices", new int[32]);
    String[] pathNames = new String[32];
    pathNames[0] = "name";
    setField("pathNames", pathNames);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.name", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_objectWithNullName() throws InvocationTargetException, IllegalAccessException {
    // stack with one object scope, pathNames[0] = null
    setField("stackSize", 1);
    setField("stack", new int[] {JsonScope.EMPTY_OBJECT});
    setField("pathIndices", new int[32]);
    setField("pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_multipleMixedScopes() throws InvocationTargetException, IllegalAccessException {
    // stack with 4 elements: array, object with name, array, object with null name
    int[] stack = new int[] {
        JsonScope.NONEMPTY_ARRAY,
        JsonScope.DANGLING_NAME,
        JsonScope.EMPTY_ARRAY,
        JsonScope.NONEMPTY_OBJECT
    };
    int[] pathIndices = new int[] {3, 0, 5, 0};
    String[] pathNames = new String[32];
    pathNames[1] = "key";
    pathNames[3] = null;
    setField("stackSize", 4);
    setField("stack", stack);
    setField("pathIndices", pathIndices);
    setField("pathNames", pathNames);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    // Expected: $[3].key[5].
    assertEquals("$[3].key[5].", path);
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