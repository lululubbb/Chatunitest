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

import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_187_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    jsonReader = new JsonReader(new StringReader("{}"));
  }

  @Test
    @Timeout(8000)
  void testSetLenient_true() {
    jsonReader.setLenient(true);
    assertTrue(jsonReader.isLenient());
  }

  @Test
    @Timeout(8000)
  void testSetLenient_false() {
    jsonReader.setLenient(false);
    assertFalse(jsonReader.isLenient());
  }
}