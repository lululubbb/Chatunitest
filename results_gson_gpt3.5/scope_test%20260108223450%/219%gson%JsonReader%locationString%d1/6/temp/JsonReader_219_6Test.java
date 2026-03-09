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

public class JsonReader_219_6Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Provide a dummy Reader since JsonReader requires it but locationString() does not use it
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
  public void testLocationString() throws Exception {
    // Use reflection to set private fields lineNumber, pos, lineStart
    setField(jsonReader, "lineNumber", 4); // lineNumber = 4 means line = 5
    setField(jsonReader, "pos", 10);
    setField(jsonReader, "lineStart", 5);

    // Instead of subclassing, use reflection to override getPath() by creating a proxy instance
    // Since getPath() is public, we can replace the method call by using reflection on the original instance
    // But since we cannot override easily, we invoke locationString() via reflection on the original instance,
    // then we temporarily replace getPath() using a spy or by reflection on a backing field if any.
    // Since getPath() is public, we can use reflection to override it by creating a proxy is complicated,
    // so instead we invoke locationString() and replace getPath() by reflection on the original instance's class.

    // To do this, we create a subclass that overrides getPath() but we cannot call the constructor with jsonReader.in because 'in' is private.
    // So we get the 'in' field value by reflection and pass it.

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    Reader in = (Reader) inField.get(jsonReader);

    JsonReader jsonReaderWithPath = new JsonReader(in) {
      @Override
      public String getPath() {
        return "$.testPath";
      }
    };
    setField(jsonReaderWithPath, "lineNumber", 4);
    setField(jsonReaderWithPath, "pos", 10);
    setField(jsonReaderWithPath, "lineStart", 5);

    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String result = (String) locationStringMethod.invoke(jsonReaderWithPath);

    // line = lineNumber + 1 = 5
    // column = pos - lineStart + 1 = 10 - 5 + 1 = 6
    // path = $.testPath
    assertEquals(" at line 5 column 6 path $.testPath", result);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}