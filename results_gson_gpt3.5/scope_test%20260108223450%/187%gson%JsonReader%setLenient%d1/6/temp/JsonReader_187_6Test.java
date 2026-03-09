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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.StringReader;

class JsonReader_187_6Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    jsonReader = new JsonReader(new StringReader(""));
  }

  @Test
    @Timeout(8000)
  void testSetLenientTrue() {
    jsonReader.setLenient(true);
    assertTrue(jsonReader.isLenient());
  }

  @Test
    @Timeout(8000)
  void testSetLenientFalse() {
    jsonReader.setLenient(false);
    assertFalse(jsonReader.isLenient());
  }
}