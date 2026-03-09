package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_220_4Test {
  private JsonReader jsonReader;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    jsonReader = new JsonReader(null);
    getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStackSize() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 0);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayPathWithoutUsePreviousPath() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 2);
    setField(jsonReader, "stack", new int[] {
        JsonScope.EMPTY_ARRAY,
        JsonScope.NONEMPTY_ARRAY,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathIndices", new int[] {0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$[0][3]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayPathWithUsePreviousPath_decrementsLastIndex() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 2);
    setField(jsonReader, "stack", new int[] {
        JsonScope.NONEMPTY_ARRAY,
        JsonScope.NONEMPTY_ARRAY,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathIndices", new int[] {1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, true);
    assertEquals("$[1][1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_objectPathWithNames() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 3);
    setField(jsonReader, "stack", new int[] {
        JsonScope.NONEMPTY_OBJECT,
        JsonScope.DANGLING_NAME,
        JsonScope.EMPTY_OBJECT,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[] {"first", "second", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.first.second.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_mixedPath() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 4);
    setField(jsonReader, "stack", new int[] {
        JsonScope.NONEMPTY_OBJECT,
        JsonScope.NONEMPTY_ARRAY,
        JsonScope.DANGLING_NAME,
        JsonScope.NONEMPTY_ARRAY,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathIndices", new int[] {0, 2, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathNames", new String[] {"obj", null, "key", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.obj[2].key[5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_ignoredScopes() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 3);
    setField(jsonReader, "stack", new int[] {
        JsonScope.NONEMPTY_DOCUMENT,
        JsonScope.EMPTY_DOCUMENT,
        JsonScope.CLOSED,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$", path);
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}