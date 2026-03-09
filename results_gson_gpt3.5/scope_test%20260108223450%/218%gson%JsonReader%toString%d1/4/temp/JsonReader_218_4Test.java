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

public class JsonReader_218_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    jsonReader = new JsonReader(new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // EOF
      }
      @Override
      public void close() {
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testToString_includesClassNameAndLocationString() throws Exception {
    // Use reflection to invoke private locationString method
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String location = (String) locationStringMethod.invoke(jsonReader);

    String expected = JsonReader.class.getSimpleName() + location;
    String actual = jsonReader.toString();

    assertEquals(expected, actual);
  }
}