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

public class JsonReader_222_5Test {

  private JsonReader jsonReader;

  private int PEEKED_BEGIN_ARRAY;
  private int PEEKED_BEGIN_OBJECT;

  @BeforeEach
  public void setUp() throws Exception {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Access private static final int fields via reflection
    Field peekedBeginArrayField = JsonReader.class.getDeclaredField("PEEKED_BEGIN_ARRAY");
    peekedBeginArrayField.setAccessible(true);
    PEEKED_BEGIN_ARRAY = peekedBeginArrayField.getInt(null);

    Field peekedBeginObjectField = JsonReader.class.getDeclaredField("PEEKED_BEGIN_OBJECT");
    peekedBeginObjectField.setAccessible(true);
    PEEKED_BEGIN_OBJECT = peekedBeginObjectField.getInt(null);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_default() throws Exception {
    // getPath() calls getPath(false)
    String path = jsonReader.getPath();
    assertNotNull(path);
    // Since stackSize is 0 by default, path should be "$"
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_usePreviousPath_false_stackSize0() throws Exception {
    String path = invokeGetPath(false);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_usePreviousPath_true_stackSize0() throws Exception {
    String path = invokeGetPath(true);
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_stackSize1_array() throws Exception {
    setStackSize(1);
    setStackAt(0, PEEKED_BEGIN_ARRAY);
    setPathIndicesAt(0, 5);
    setPathNamesAt(0, null);

    String path = invokeGetPath(false);
    assertEquals("$[5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_stackSize1_object_withName() throws Exception {
    setStackSize(1);
    setStackAt(0, PEEKED_BEGIN_OBJECT);
    setPathIndicesAt(0, 0);
    setPathNamesAt(0, "foo");

    String path = invokeGetPath(false);
    assertEquals("$.foo", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_stackSize2_mixed() throws Exception {
    setStackSize(2);
    // First element is array
    setStackAt(0, PEEKED_BEGIN_ARRAY);
    setPathIndicesAt(0, 3);
    setPathNamesAt(0, null);
    // Second element is object
    setStackAt(1, PEEKED_BEGIN_OBJECT);
    setPathIndicesAt(1, 0);
    setPathNamesAt(1, "bar");

    String path = invokeGetPath(false);
    // Should be $[3].bar
    assertEquals("$[3].bar", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withUsePreviousPath_true() throws Exception {
    setStackSize(2);
    setStackAt(0, PEEKED_BEGIN_ARRAY);
    setPathIndicesAt(0, 1);
    setPathNamesAt(0, null);
    setStackAt(1, PEEKED_BEGIN_OBJECT);
    setPathIndicesAt(1, 2);
    setPathNamesAt(1, "baz");

    String path = invokeGetPath(true);
    // usePreviousPath true, so index is decremented by 1 for arrays
    // For stack[0] index 1 -> 0
    // For stack[1] index 2 -> 1, but since pathNames is "baz", index is ignored
    assertEquals("$[0].baz", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withUsePreviousPath_true_andIndexZero() throws Exception {
    setStackSize(1);
    setStackAt(0, PEEKED_BEGIN_ARRAY);
    setPathIndicesAt(0, 0);
    setPathNamesAt(0, null);

    String path = invokeGetPath(true);
    // index 0 decremented by 1 -> -1, so path should be $[-1]
    assertEquals("$[-1]", path);
  }

  // Helpers to access private fields and methods

  private String invokeGetPath(boolean usePreviousPath) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    method.setAccessible(true);
    return (String) method.invoke(jsonReader, usePreviousPath);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, size);
  }

  private void setStackAt(int index, int value) throws Exception {
    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    stack[index] = value;
  }

  private void setPathIndicesAt(int index, int value) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    pathIndices[index] = value;
  }

  private void setPathNamesAt(int index, String value) throws Exception {
    Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonReader);
    pathNames[index] = value;
  }
}