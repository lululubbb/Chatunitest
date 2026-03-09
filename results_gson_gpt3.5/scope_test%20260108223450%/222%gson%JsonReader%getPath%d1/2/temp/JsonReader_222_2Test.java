package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_222_2Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  private int PEEKED_BEGIN_OBJECT;
  private int PEEKED_BEGIN_ARRAY;

  @BeforeEach
  void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Access private static final fields via reflection
    Field beginObjectField = JsonReader.class.getDeclaredField("PEEKED_BEGIN_OBJECT");
    beginObjectField.setAccessible(true);
    PEEKED_BEGIN_OBJECT = beginObjectField.getInt(null);

    Field beginArrayField = JsonReader.class.getDeclaredField("PEEKED_BEGIN_ARRAY");
    beginArrayField.setAccessible(true);
    PEEKED_BEGIN_ARRAY = beginArrayField.getInt(null);
  }

  @Test
    @Timeout(8000)
  void testGetPath_default() {
    // The default stackSize is 0, so path should be "$"
    String path = jsonReader.getPath();
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withArrayIndex() throws Exception {
    // Use reflection to invoke private getPath(boolean)
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Setup internal state to simulate stack with an array context
    // stack[0] = PEEKED_BEGIN_ARRAY to simulate inside an array
    // stackSize = 1
    // pathIndices[0] = 5
    // pathNames[0] = null
    setField(jsonReader, "stack", new int[]{PEEKED_BEGIN_ARRAY});
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[]{5});
    setField(jsonReader, "pathNames", new String[]{null});

    // Set pathIndices[0] to 5 for usePreviousPath = true case
    // But for usePreviousPath = false, pathIndices[0] should be 0
    // So for false call, temporarily set pathIndices[0] = 0
    int[] pathIndices = new int[]{0};
    setField(jsonReader, "pathIndices", pathIndices);

    // Call private getPath with false
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$[0]", path);

    // For true call, set pathIndices[0] = 5
    pathIndices[0] = 5;
    setField(jsonReader, "pathIndices", pathIndices);

    // Call private getPath with true (usePreviousPath)
    path = (String) getPathMethod.invoke(jsonReader, true);
    assertEquals("$[5]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_withObjectName() throws Exception {
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Setup internal state to simulate stack with an object context
    // stack[0] = PEEKED_BEGIN_OBJECT
    // stackSize = 1
    // pathNames[0] = "key"
    // pathIndices[0] = 0
    setField(jsonReader, "stack", new int[]{PEEKED_BEGIN_OBJECT});
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathNames", new String[]{"key"});
    setField(jsonReader, "pathIndices", new int[]{0});

    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.key", path);

    path = (String) getPathMethod.invoke(jsonReader, true);
    assertEquals("$.key", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_complexPath() throws Exception {
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Setup complex stack:
    // stackSize = 4
    // stack = {PEEKED_BEGIN_OBJECT, PEEKED_BEGIN_ARRAY, PEEKED_BEGIN_OBJECT, PEEKED_BEGIN_ARRAY}
    // pathNames = { "obj1", null, "obj2", null }
    // pathIndices = {0, 2, 0, 5}
    int[] stack = {
      PEEKED_BEGIN_OBJECT,
      PEEKED_BEGIN_ARRAY,
      PEEKED_BEGIN_OBJECT,
      PEEKED_BEGIN_ARRAY
    };
    String[] pathNames = {"obj1", null, "obj2", null};
    int[] pathIndices = {0, 2, 0, 5};
    setField(jsonReader, "stack", stack);
    setField(jsonReader, "stackSize", 4);
    setField(jsonReader, "pathNames", pathNames);
    setField(jsonReader, "pathIndices", pathIndices);

    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$.obj1[2].obj2[5]", path);
  }

  // Helper method to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}