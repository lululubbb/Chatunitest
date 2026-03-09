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

public class JsonReader_220_3Test {

  private JsonReader jsonReader;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    // Provide a non-null Reader to avoid NullPointerException in JsonReader constructor
    Reader reader = new StringReader("");
    jsonReader = new JsonReader(reader);
    getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_EmptyStack() throws InvocationTargetException, IllegalAccessException {
    // stackSize = 0 means no path elements
    setField(jsonReader, "stackSize", 0);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_ArrayNoPreviousPath() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "stack", new int[]{JsonScope.EMPTY_ARRAY});
    setField(jsonReader, "pathIndices", new int[]{3});
    setField(jsonReader, "pathNames", new String[]{null});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$[3]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_ArrayWithPreviousPath_DecrementsIndex() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "stack", new int[]{JsonScope.NONEMPTY_ARRAY});
    setField(jsonReader, "pathIndices", new int[]{2});
    setField(jsonReader, "pathNames", new String[]{null});
    String path = (String) getPathMethod.invoke(jsonReader, true);
    assertEquals("$[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_ObjectWithPathName() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "stack", new int[]{JsonScope.NONEMPTY_OBJECT});
    setField(jsonReader, "pathIndices", new int[]{0});
    setField(jsonReader, "pathNames", new String[]{"key"});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.key", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_ObjectWithNullPathName() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "stack", new int[]{JsonScope.DANGLING_NAME});
    setField(jsonReader, "pathIndices", new int[]{0});
    setField(jsonReader, "pathNames", new String[]{null});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_MultipleStackElements() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 3);
    setField(jsonReader, "stack", new int[]{
        JsonScope.NONEMPTY_DOCUMENT, JsonScope.NONEMPTY_ARRAY, JsonScope.NONEMPTY_OBJECT});
    setField(jsonReader, "pathIndices", new int[]{0, 5, 0});
    setField(jsonReader, "pathNames", new String[]{null, null, "field"});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    // NONEMPTY_DOCUMENT does not add anything
    // NONEMPTY_ARRAY adds [5]
    // NONEMPTY_OBJECT adds .field
    assertEquals("$[5].field", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_UsePreviousPathFalse_DoesNotDecrement() throws InvocationTargetException, IllegalAccessException {
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "stack", new int[]{JsonScope.NONEMPTY_ARRAY});
    setField(jsonReader, "pathIndices", new int[]{1});
    setField(jsonReader, "pathNames", new String[]{null});
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$[1]", path);
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}