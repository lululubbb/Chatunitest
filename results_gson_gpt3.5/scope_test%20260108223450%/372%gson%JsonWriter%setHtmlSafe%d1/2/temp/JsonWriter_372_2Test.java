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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_372_2Test {

  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    mockWriter = new StringWriter();
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  void testSetHtmlSafeTrue() {
    jsonWriter.setHtmlSafe(true);
    assertTrue(jsonWriter.isHtmlSafe());
  }

  @Test
    @Timeout(8000)
  void testSetHtmlSafeFalse() {
    jsonWriter.setHtmlSafe(false);
    assertFalse(jsonWriter.isHtmlSafe());
  }
}