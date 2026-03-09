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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_219_1Test {
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader reader = new StringReader("");
    jsonReader = new JsonReader(reader);
  }

  @Test
    @Timeout(8000)
  public void testLocationString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Use reflection to set private fields lineNumber, pos, lineStart
    setField(jsonReader, "lineNumber", 4);
    setField(jsonReader, "pos", 15);
    setField(jsonReader, "lineStart", 10);

    // Set stackSize to 1 and stack[0] to 0 to represent root '$' path correctly
    setField(jsonReader, "stackSize", 1);
    int[] stack = new int[32];
    stack[0] = 0; // root array/object, corresponds to '$'
    setField(jsonReader, "stack", stack);

    // pathIndices and pathNames must be set for getPath() to work properly
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    String[] pathNames = new String[32];
    pathNames[0] = null;
    setField(jsonReader, "pathNames", pathNames);

    // Invoke private method locationString
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String result = (String) locationStringMethod.invoke(jsonReader);

    // line = lineNumber + 1 = 5
    // column = pos - lineStart + 1 = 15 - 10 + 1 = 6
    // path = getPath() returns "$" (default root path)
    assertEquals(" at line 5 column 6 path $", result);
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}