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
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;

class JsonReader_188_4Test {

  @Test
    @Timeout(8000)
  void testIsLenientDefaultFalse() {
    Reader reader = new StringReader("");
    JsonReader jsonReader = new JsonReader(reader);
    assertFalse(jsonReader.isLenient());
  }

  @Test
    @Timeout(8000)
  void testIsLenientTrueAfterSetLenient() {
    Reader reader = new StringReader("");
    JsonReader jsonReader = new JsonReader(reader);
    jsonReader.setLenient(true);
    assertTrue(jsonReader.isLenient());
  }

  @Test
    @Timeout(8000)
  void testIsLenientFalseAfterSetLenientFalse() {
    Reader reader = new StringReader("");
    JsonReader jsonReader = new JsonReader(reader);
    jsonReader.setLenient(true);
    jsonReader.setLenient(false);
    assertFalse(jsonReader.isLenient());
  }
}