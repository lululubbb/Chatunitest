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

public class JsonReader_220_1Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Provide a dummy Reader since constructor requires it, not used for getPath()
    jsonReader = new JsonReader(new java.io.StringReader(""));
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack_usePreviousFalse() throws Exception {
    setStackSize(0);
    String path = invokeGetPath(false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack_usePreviousTrue() throws Exception {
    setStackSize(0);
    String path = invokeGetPath(true);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayUsePreviousFalse() throws Exception {
    setStackSize(1);
    setStackAt(0, JsonScope.EMPTY_ARRAY);
    setPathIndicesAt(0, 5);
    String path = invokeGetPath(false);
    assertEquals("$[5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayUsePreviousTrue_indexGreaterThanZero_lastElement() throws Exception {
    setStackSize(1);
    setStackAt(0, JsonScope.NONEMPTY_ARRAY);
    setPathIndicesAt(0, 3);
    String path = invokeGetPath(true);
    // pathIndex decremented by 1 because usePreviousPath is true and last element
    assertEquals("$[2]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_arrayUsePreviousTrue_indexZero_lastElement() throws Exception {
    setStackSize(1);
    setStackAt(0, JsonScope.NONEMPTY_ARRAY);
    setPathIndicesAt(0, 0);
    String path = invokeGetPath(true);
    // index 0, no decrement
    assertEquals("$[0]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_objectWithName() throws Exception {
    setStackSize(1);
    setStackAt(0, JsonScope.EMPTY_OBJECT);
    setPathNamesAt(0, "foo");
    String path = invokeGetPath(false);
    assertEquals("$.foo", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_objectWithNullName() throws Exception {
    setStackSize(1);
    setStackAt(0, JsonScope.DANGLING_NAME);
    setPathNamesAt(0, null);
    String path = invokeGetPath(false);
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_multipleMixedElements() throws Exception {
    setStackSize(4);
    setStackAt(0, JsonScope.NONEMPTY_DOCUMENT);
    setStackAt(1, JsonScope.NONEMPTY_OBJECT);
    setStackAt(2, JsonScope.NONEMPTY_ARRAY);
    setStackAt(3, JsonScope.EMPTY_OBJECT);
    setPathNamesAt(1, "obj");
    setPathIndicesAt(2, 7);
    setPathNamesAt(3, "bar");
    String path = invokeGetPath(false);
    // NONEMPTY_DOCUMENT and EMPTY_DOCUMENT and CLOSED do nothing
    // NONEMPTY_OBJECT appends ".obj"
    // NONEMPTY_ARRAY appends "[7]"
    // EMPTY_OBJECT appends "." + "bar"
    assertEquals("$.obj[7].bar", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_multipleMixedElements_usePreviousTrue() throws Exception {
    setStackSize(3);
    setStackAt(0, JsonScope.NONEMPTY_OBJECT);
    setStackAt(1, JsonScope.NONEMPTY_ARRAY);
    setStackAt(2, JsonScope.NONEMPTY_ARRAY);
    setPathNamesAt(0, "obj");
    setPathIndicesAt(1, 3);
    setPathIndicesAt(2, 1);
    String path = invokeGetPath(true);
    // For last element (i=2), pathIndex decremented by 1 (1->0)
    assertEquals("$.obj[3][0]", path);
  }

  private void setStackSize(int size) throws Exception {
    setField("stackSize", size);
  }

  private void setStackAt(int index, int value) throws Exception {
    int[] stack = (int[]) getField("stack");
    stack[index] = value;
  }

  private void setPathIndicesAt(int index, int value) throws Exception {
    int[] pathIndices = (int[]) getField("pathIndices");
    pathIndices[index] = value;
  }

  private void setPathNamesAt(int index, String value) throws Exception {
    String[] pathNames = (String[]) getField("pathNames");
    pathNames[index] = value;
  }

  private Object getField(String fieldName) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(jsonReader);
  }

  private void setField(String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  private String invokeGetPath(boolean usePreviousPath) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    method.setAccessible(true);
    return (String) method.invoke(jsonReader, usePreviousPath);
  }
}