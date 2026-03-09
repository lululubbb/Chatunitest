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

class JsonReader_218_6Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader dummyReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // simulate EOF
      }
      @Override
      public void close() { }
    };
    jsonReader = new JsonReader(dummyReader);
  }

  @Test
    @Timeout(8000)
  void toString_includesClassNameAndLocationString() throws Exception {
    // Use reflection to invoke private locationString() method
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String locationString = (String) locationStringMethod.invoke(jsonReader);

    String expected = JsonReader.class.getSimpleName() + locationString;
    String actual = jsonReader.toString();

    assertEquals(expected, actual);
  }
}