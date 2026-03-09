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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_locationString_Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    jsonReader = new JsonReader(new StringReader("{}"));
  }

  @Test
    @Timeout(8000)
  void locationString_returnsCorrectFormat() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Use reflection to access the package-private locationString method
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    // Set lineNumber, pos, lineStart fields via reflection
    setField(jsonReader, "lineNumber", 4); // lineNumber = 4 means line = 5 (4 + 1)
    setField(jsonReader, "pos", 15);
    setField(jsonReader, "lineStart", 10); // column = pos - lineStart + 1 = 15 - 10 + 1 = 6

    // Set stackSize, pathNames and pathIndices to simulate a path, via reflection
    setField(jsonReader, "stackSize", 2);

    // pathNames and pathIndices are arrays of size 32, so we must get existing arrays and modify them
    Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonReader);
    pathNames[0] = null;
    pathNames[1] = "key";

    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    pathIndices[0] = 0;
    pathIndices[1] = 3;

    // Invoke locationString
    String result = (String) locationStringMethod.invoke(jsonReader);

    // Expected path is ["[0]", "key[3]"] but getPath() returns a string like "$[0].key[3]"
    // So we need to verify the exact path string returned by getPath()
    String expectedPath = jsonReader.getPath();

    String expected = " at line 5 column 6 path " + expectedPath;
    assertEquals(expected, result);
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}