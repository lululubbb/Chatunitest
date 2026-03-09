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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_222_3Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_default() {
    String path = jsonReader.getPath();
    assertNotNull(path);
    // Default state, stackSize = 0, so path should be "$"
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_usePreviousPath_false() throws Exception {
    Method getPathBoolean = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathBoolean.setAccessible(true);

    // Default stackSize = 0, pathNames and pathIndices default values
    String path = (String) getPathBoolean.invoke(jsonReader, false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_usePreviousPath_true() throws Exception {
    Method getPathBoolean = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathBoolean.setAccessible(true);

    // Setup internal state to simulate stack with one object and one array
    // stack[0] = 1 (PEEKED_BEGIN_OBJECT)
    // stack[1] = 3 (PEEKED_BEGIN_ARRAY)
    // stackSize = 2
    // pathNames[0] = "name0"
    // pathIndices[1] = 5
    setField(jsonReader, "stackSize", 2);
    int[] stack = new int[32];
    stack[0] = 1; // PEEKED_BEGIN_OBJECT
    stack[1] = 3; // PEEKED_BEGIN_ARRAY
    setField(jsonReader, "stack", stack);
    String[] pathNames = new String[32];
    pathNames[0] = "name0";
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[1] = 5;
    setField(jsonReader, "pathIndices", pathIndices);

    String path = (String) getPathBoolean.invoke(jsonReader, true);
    // Expected: $.name0[5]
    assertEquals("$.name0[5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_variousStackStates() throws Exception {
    Method getPathBoolean = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathBoolean.setAccessible(true);

    // stackSize = 3, stack states: PEEKED_BEGIN_OBJECT, PEEKED_END_OBJECT, PEEKED_BEGIN_ARRAY
    int[] stack = new int[32];
    stack[0] = 1;  // PEEKED_BEGIN_OBJECT
    stack[1] = 2;  // PEEKED_END_OBJECT
    stack[2] = 3;  // PEEKED_BEGIN_ARRAY
    setField(jsonReader, "stack", stack);

    String[] pathNames = new String[32];
    pathNames[0] = "obj";
    pathNames[1] = null;
    pathNames[2] = null;
    setField(jsonReader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 7;
    pathIndices[1] = 8;
    pathIndices[2] = 9;
    setField(jsonReader, "pathIndices", pathIndices);

    setField(jsonReader, "stackSize", 3);

    String path = (String) getPathBoolean.invoke(jsonReader, false);
    // Expected: $.obj[9]
    // stack[0] = PEEKED_BEGIN_OBJECT -> .obj
    // stack[1] = PEEKED_END_OBJECT -> no path segment
    // stack[2] = PEEKED_BEGIN_ARRAY -> [pathIndices[2]] = [9]
    assertEquals("$.obj[9]", path);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}