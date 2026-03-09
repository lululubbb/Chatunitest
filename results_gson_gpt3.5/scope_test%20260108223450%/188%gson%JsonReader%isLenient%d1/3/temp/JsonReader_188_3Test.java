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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.StringReader;

class JsonReader_188_3Test {

  @Test
    @Timeout(8000)
  void testIsLenientDefaultFalse() {
    JsonReader reader = new JsonReader(new StringReader(""));
    // By default, lenient should be false
    assertFalse(reader.isLenient());
  }

  @Test
    @Timeout(8000)
  void testIsLenientAfterSetTrue() {
    JsonReader reader = new JsonReader(new StringReader(""));
    reader.setLenient(true);
    assertTrue(reader.isLenient());
  }

  @Test
    @Timeout(8000)
  void testIsLenientAfterSetFalse() {
    JsonReader reader = new JsonReader(new StringReader(""));
    reader.setLenient(true);
    reader.setLenient(false);
    assertFalse(reader.isLenient());
  }
}