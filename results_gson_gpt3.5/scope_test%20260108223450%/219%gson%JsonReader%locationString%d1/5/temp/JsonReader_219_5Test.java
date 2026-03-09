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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_locationString_Test {

  private JsonReader jsonReader;
  private Method locationStringMethod;

  @BeforeEach
  void setUp() throws Exception {
    // Create a dummy Reader since it's required by the constructor but not used here
    Reader dummyReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1;
      }
      @Override
      public void close() {}
    };
    jsonReader = new JsonReader(dummyReader);

    // Access the package-private locationString() method via reflection
    locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void locationString_returnsCorrectString() throws Exception {
    // Use reflection to set lineNumber, pos, lineStart fields
    setField(jsonReader, "lineNumber", 4); // lineNumber=4 means line = 5 (4+1)
    setField(jsonReader, "pos", 10);
    setField(jsonReader, "lineStart", 5);

    // Set up pathNames and pathIndices, and stackSize so getPath() returns a non-empty path
    setField(jsonReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[0] = null; // must be null to get "$"
    pathNames[1] = "child";
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 1;
    setField(jsonReader, "pathIndices", pathIndices);

    // Invoke locationString
    String result = (String) locationStringMethod.invoke(jsonReader);

    // Expected column = pos - lineStart + 1 = 10 - 5 + 1 = 6
    // Expected line = lineNumber + 1 = 5
    // Expected path = getPath() returns "$.child[1]"
    // So the expected string is: " at line 5 column 6 path $.child[1]"
    assertEquals(" at line 5 column 6 path $.child[1]", result);
  }

  @Test
    @Timeout(8000)
  void locationString_withDefaultPath_returnsCorrectString() throws Exception {
    // lineNumber=0 means line=1
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "lineStart", 0);

    // stackSize=1 means path has one element "$"
    setField(jsonReader, "stackSize", 1);

    // pathNames and pathIndices should be empty arrays to avoid NPE in getPath()
    setField(jsonReader, "pathNames", new String[32]);
    setField(jsonReader, "pathIndices", new int[32]);

    // Invoke locationString
    String result = (String) locationStringMethod.invoke(jsonReader);

    // Expected column = 0 - 0 + 1 = 1
    // Expected line = 0 + 1 = 1
    // Expected path = "$" since stackSize=1 but pathNames[0] == null and pathIndices[0] == 0
    assertEquals(" at line 1 column 1 path $", result);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}