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

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_220_5Test {

  private JsonReader jsonReader;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    jsonReader = new JsonReader(new StringReader(""));  // Provide non-null Reader
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
  public void testGetPath_arrayUsePreviousPathFalse() throws InvocationTargetException, IllegalAccessException {
    // stack with one array scope, pathIndices = 3, usePreviousPath = false
    setField("stackSize", 1);
    setField("stack", new int[]{JsonScope.EMPTY_ARRAY});
    setField("pathIndices", new int[]{3});
    setField("pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$[3]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayUsePreviousPathTrue_pathIndexGreaterThanZero_lastElement() throws InvocationTargetException, IllegalAccessException {
    // stack with one array scope, pathIndices = 3, usePreviousPath = true, i == stackSize - 1
    setField("stackSize", 1);
    setField("stack", new int[]{JsonScope.NONEMPTY_ARRAY});
    setField("pathIndices", new int[]{3});
    setField("pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, true);
    assertEquals("$[2]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_objectWithName() throws InvocationTargetException, IllegalAccessException {
    // stack with one object scope, pathNames set to "name"
    setField("stackSize", 1);
    setField("stack", new int[]{JsonScope.EMPTY_OBJECT});
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
    // stack with one object scope, pathNames null at index
    setField("stackSize", 1);
    setField("stack", new int[]{JsonScope.NONEMPTY_OBJECT});
    setField("pathIndices", new int[32]);
    String[] pathNames = new String[32];
    pathNames[0] = null;
    setField("pathNames", pathNames);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_multipleMixedScopes() throws InvocationTargetException, IllegalAccessException {
    // stack with multiple scopes: [NONEMPTY_DOCUMENT, NONEMPTY_OBJECT, NONEMPTY_ARRAY]
    // pathNames[1]="obj", pathIndices[2]=5
    setField("stackSize", 3);
    setField("stack", new int[]{
        JsonScope.NONEMPTY_DOCUMENT,
        JsonScope.NONEMPTY_OBJECT,
        JsonScope.NONEMPTY_ARRAY});
    String[] pathNames = new String[32];
    pathNames[1] = "obj";
    setField("pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[2] = 5;
    setField("pathIndices", pathIndices);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.obj[5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_scopesWithNoAppend() throws InvocationTargetException, IllegalAccessException {
    // stack with CLOSED, EMPTY_DOCUMENT, NONEMPTY_DOCUMENT scopes which do not append anything
    setField("stackSize", 3);
    setField("stack", new int[]{
        JsonScope.CLOSED,
        JsonScope.EMPTY_DOCUMENT,
        JsonScope.NONEMPTY_DOCUMENT});
    setField("pathIndices", new int[32]);
    setField("pathNames", new String[32]);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$", path);
  }

  private void setField(String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(jsonReader, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}