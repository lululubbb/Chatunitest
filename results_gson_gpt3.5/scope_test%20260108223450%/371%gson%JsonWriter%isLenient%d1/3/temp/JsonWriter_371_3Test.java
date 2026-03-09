package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

class JsonWriter_371_3Test {

  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    jsonWriter = new JsonWriter(new StringWriter());
  }

  @Test
    @Timeout(8000)
  void testIsLenientDefaultFalse() {
    // By default lenient should be false
    assertFalse(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  void testIsLenientTrueAfterSetLenientTrue() {
    jsonWriter.setLenient(true);
    assertTrue(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  void testIsLenientFalseAfterSetLenientFalse() {
    jsonWriter.setLenient(true);
    jsonWriter.setLenient(false);
    assertFalse(jsonWriter.isLenient());
  }
}