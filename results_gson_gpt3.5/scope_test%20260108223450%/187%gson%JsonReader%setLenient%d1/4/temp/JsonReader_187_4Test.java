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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.lang.reflect.Field;

class JsonReader_187_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader dummyReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // EOF immediately
      }
      @Override
      public void close() {}
    };
    jsonReader = new JsonReader(dummyReader);
  }

  @Test
    @Timeout(8000)
  void testSetLenientTrue() throws Exception {
    jsonReader.setLenient(true);

    Field lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenientValue = (boolean) lenientField.get(jsonReader);

    assertTrue(lenientValue, "lenient should be true after setLenient(true)");
  }

  @Test
    @Timeout(8000)
  void testSetLenientFalse() throws Exception {
    // First set true to verify toggle
    jsonReader.setLenient(true);
    jsonReader.setLenient(false);

    Field lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenientValue = (boolean) lenientField.get(jsonReader);

    assertFalse(lenientValue, "lenient should be false after setLenient(false)");
  }
}