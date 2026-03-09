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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_222_1Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Use a dummy Reader since getPath does not read from it directly
    Reader dummyReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // EOF
      }
      @Override
      public void close() {
      }
    };
    jsonReader = new JsonReader(dummyReader);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_default() throws Exception {
    // By default stackSize = 0, so getPath(false) returns "$"
    String path = jsonReader.getPath();
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withArrayAndObject() throws Exception {
    // Use reflection to set private fields stack, stackSize, pathNames, pathIndices
    setPrivateField(jsonReader, "stackSize", 3);

    int[] stack = new int[] {
      getPrivateStaticIntField(JsonReader.class, "PEEKED_BEGIN_ARRAY"),
      getPrivateStaticIntField(JsonReader.class, "PEEKED_BEGIN_OBJECT"),
      getPrivateStaticIntField(JsonReader.class, "PEEKED_BEGIN_ARRAY")
    };
    setPrivateField(jsonReader, "stack", stack);

    String[] pathNames = new String[32];
    pathNames[0] = null;
    pathNames[1] = "name";
    pathNames[2] = null;
    setPrivateField(jsonReader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 0;
    pathIndices[2] = 5;
    setPrivateField(jsonReader, "pathIndices", pathIndices);

    // Call private getPath(boolean) with false
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
    String path = (String) getPathMethod.invoke(jsonReader, false);

    // Expected path: $[0].name[5]
    assertEquals("$[0].name[5]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_usePreviousPathTrue() throws Exception {
    // Setup stackSize and stack to simulate previous path usage
    setPrivateField(jsonReader, "stackSize", 2);

    int[] stack = new int[] {
      getPrivateStaticIntField(JsonReader.class, "PEEKED_BEGIN_OBJECT"),
      getPrivateStaticIntField(JsonReader.class, "PEEKED_BEGIN_ARRAY")
    };
    setPrivateField(jsonReader, "stack", stack);

    String[] pathNames = new String[32];
    pathNames[0] = "obj";
    pathNames[1] = null;
    setPrivateField(jsonReader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 7;
    setPrivateField(jsonReader, "pathIndices", pathIndices);

    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
    String path = (String) getPathMethod.invoke(jsonReader, true);

    // Expected path: $['obj']
    assertEquals("$['obj']", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack() throws Exception {
    // stackSize = 0 means path is just "$"
    setPrivateField(jsonReader, "stackSize", 0);
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
    String path = (String) getPathMethod.invoke(jsonReader, false);
    assertEquals("$", path);
  }

  private void setPrivateField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      if (field.getType().isArray() && value instanceof int[]) {
        int[] arr = (int[]) value;
        int[] newArr = new int[32];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        field.set(target, newArr);
      } else if (field.getType().isArray() && value instanceof String[]) {
        String[] arr = (String[]) value;
        String[] newArr = new String[32];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        field.set(target, newArr);
      } else {
        field.set(target, value);
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private int getPrivateStaticIntField(Class<?> clazz, String fieldName) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.getInt(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}