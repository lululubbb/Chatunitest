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

class JsonReader_221_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void testGetPreviousPath_invokesGetPathWithTrue() throws Exception {
    // Since getPath(boolean) is private, invoke it via reflection from getPreviousPath()
    // getPreviousPath() calls getPath(true) internally, so just verify the returned value.

    // Use reflection to set the internal state so getPath(true) returns "mockedPath"
    // We'll set stackSize to 0 so path is "$"
    setField(jsonReader, "stackSize", 0);

    // Spy on jsonReader to override getPath(boolean) via reflection is not feasible,
    // so instead, we invoke getPreviousPath() and verify it returns "$" (stackSize=0)

    String result = jsonReader.getPreviousPath();
    assertEquals("$", result);
  }

  @Test
    @Timeout(8000)
  void testGetPathTrue_emptyStack() throws Exception {
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // stackSize = 0 means no path elements
    setField(jsonReader, "stackSize", 0);

    String path = (String) getPathMethod.invoke(jsonReader, true);
    // The path for empty stack is "$"
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  void testGetPathTrue_withArrayAndObject() throws Exception {
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Setup stack and path arrays for a realistic scenario:
    // stackSize = 3
    // stack = [JsonReader.PEEKED_BEGIN_OBJECT, JsonReader.PEEKED_BEGIN_ARRAY, JsonReader.PEEKED_BEGIN_OBJECT]
    // pathIndices = [0, 2, 0]
    // pathNames = ["name1", null, "name2"]
    setField(jsonReader, "stackSize", 3);
    setField(jsonReader, "stack", new int[] {
        1, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathIndices", new int[] {
        0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    setField(jsonReader, "pathNames", new String[] {
        "name1", null, "name2", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null});

    String path = (String) getPathMethod.invoke(jsonReader, true);

    // Expected: "$.name1[2].name2"
    assertEquals("$.name1[2].name2", path);
  }

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