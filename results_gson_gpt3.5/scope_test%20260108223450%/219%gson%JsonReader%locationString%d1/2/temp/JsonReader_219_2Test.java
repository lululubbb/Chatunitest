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

public class JsonReader_219_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    // Provide a dummy Reader since JsonReader requires one
    Reader dummyReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // EOF
      }

      @Override
      public void close() {
        // no-op
      }
    };
    jsonReader = new JsonReader(dummyReader);
  }

  @Test
    @Timeout(8000)
  void testLocationString() throws Exception {
    // Set lineNumber, pos, lineStart fields via reflection
    setField(jsonReader, "lineNumber", 4); // lineNumber is zero-based, so line=5
    setField(jsonReader, "pos", 15);
    setField(jsonReader, "lineStart", 10);

    // Create a subclass instance that overrides getPath() by creating a dynamic proxy subclass
    JsonReader jsonReaderWithPath = new JsonReader(jsonReader_in(jsonReader)) {
      @Override
      public String getPath() {
        return "$.store.book[0].title";
      }
    };
    setField(jsonReaderWithPath, "lineNumber", 4);
    setField(jsonReaderWithPath, "pos", 15);
    setField(jsonReaderWithPath, "lineStart", 10);

    // Use reflection to invoke the package-private locationString() method
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    String result = (String) locationStringMethod.invoke(jsonReaderWithPath);
    // line = 5, column = pos - lineStart + 1 = 15 - 10 + 1 = 6
    assertEquals(" at line 5 column 6 path $.store.book[0].title", result);

    // Also test with lineNumber=0, pos=lineStart to test column=1 and line=1
    setField(jsonReaderWithPath, "lineNumber", 0);
    setField(jsonReaderWithPath, "pos", 3);
    setField(jsonReaderWithPath, "lineStart", 3);

    result = (String) locationStringMethod.invoke(jsonReaderWithPath);
    assertEquals(" at line 1 column 1 path $.store.book[0].title", result);
  }

  private static Reader jsonReader_in(JsonReader jsonReader) throws Exception {
    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    return (Reader) inField.get(jsonReader);
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Class<?> clazz = target.getClass();
    while (clazz != null) {
      try {
        var field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
        return;
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}