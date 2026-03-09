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

public class JsonReader_221_5Test {

  private JsonReader jsonReader;

  private int PEEKED_BEGIN_ARRAY;
  private int PEEKED_BEGIN_OBJECT;

  @BeforeEach
  public void setUp() throws Exception {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Access private static final fields via reflection
    Field peekedBeginArrayField = JsonReader.class.getDeclaredField("PEEKED_BEGIN_ARRAY");
    peekedBeginArrayField.setAccessible(true);
    PEEKED_BEGIN_ARRAY = peekedBeginArrayField.getInt(null);

    Field peekedBeginObjectField = JsonReader.class.getDeclaredField("PEEKED_BEGIN_OBJECT");
    peekedBeginObjectField.setAccessible(true);
    PEEKED_BEGIN_OBJECT = peekedBeginObjectField.getInt(null);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_EmptyStack() throws Exception {
    // stackSize = 0, so path should be "$"
    setField("stackSize", 0);
    String path = jsonReader.getPreviousPath();
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_ArrayIndex() throws Exception {
    // Simulate stack with one array at root and pathIndices at 5
    setField("stackSize", 1);
    int[] stack = new int[32];
    stack[0] = PEEKED_BEGIN_ARRAY;
    setField("stack", stack);
    int[] pathIndices = new int[32];
    pathIndices[0] = 5;
    setField("pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField("pathNames", pathNames);
    String path = jsonReader.getPreviousPath();
    assertEquals("$[5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_ObjectName() throws Exception {
    // Simulate stack with one object and pathNames "name"
    setField("stackSize", 1);
    int[] stack = new int[32];
    stack[0] = PEEKED_BEGIN_OBJECT;
    setField("stack", stack);
    String[] pathNames = new String[32];
    pathNames[0] = "name";
    setField("pathNames", pathNames);
    int[] pathIndices = new int[32];
    setField("pathIndices", pathIndices);
    String path = jsonReader.getPreviousPath();
    assertEquals("$.name", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_MultipleLevels() throws Exception {
    // stackSize=3: root object, array, object with name "foo" and index 2
    setField("stackSize", 3);
    int[] stack = new int[32];
    stack[0] = PEEKED_BEGIN_OBJECT;
    stack[1] = PEEKED_BEGIN_ARRAY;
    stack[2] = PEEKED_BEGIN_OBJECT;
    setField("stack", stack);
    String[] pathNames = new String[32];
    pathNames[0] = "root";
    pathNames[2] = "foo";
    setField("pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[1] = 2;
    setField("pathIndices", pathIndices);
    String path = jsonReader.getPreviousPath();
    assertEquals("$.root[2].foo", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_UsePreviousPathFalse() throws Exception {
    // Use reflection to invoke private getPath(false) to verify difference with true
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Setup stackSize=1 and stack with array, pathIndices=3
    setField("stackSize", 1);
    int[] stack = new int[32];
    stack[0] = PEEKED_BEGIN_ARRAY;
    setField("stack", stack);
    int[] pathIndices = new int[32];
    pathIndices[0] = 3;
    setField("pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField("pathNames", pathNames);

    String pathTrue = (String) getPathMethod.invoke(jsonReader, true);
    String pathFalse = (String) getPathMethod.invoke(jsonReader, false);

    // When usePreviousPath is false, index is incremented by 1, so pathFalse should be $[4]
    assertEquals("$[3]", pathTrue);
    assertEquals("$[4]", pathFalse);
  }

  private void setField(String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }
}