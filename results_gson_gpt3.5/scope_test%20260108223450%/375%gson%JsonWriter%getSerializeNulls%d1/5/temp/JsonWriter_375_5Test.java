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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Writer;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_375_5Test {

  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  void testGetSerializeNulls_defaultTrue() throws Exception {
    // By default, serializeNulls should be true
    assertTrue(jsonWriter.getSerializeNulls());
  }

  @Test
    @Timeout(8000)
  void testGetSerializeNulls_afterSetSerializeNullsFalse() throws Exception {
    jsonWriter.setSerializeNulls(false);
    assertFalse(jsonWriter.getSerializeNulls());
  }

  @Test
    @Timeout(8000)
  void testGetSerializeNulls_afterSetSerializeNullsTrue() throws Exception {
    jsonWriter.setSerializeNulls(false);
    jsonWriter.setSerializeNulls(true);
    assertTrue(jsonWriter.getSerializeNulls());
  }

  @Test
    @Timeout(8000)
  void testGetSerializeNulls_reflectionChange() throws Exception {
    // Use reflection to set private boolean serializeNulls to false
    Field serializeNullsField = JsonWriter.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    serializeNullsField.setBoolean(jsonWriter, false);

    assertFalse(jsonWriter.getSerializeNulls());

    // Change it back to true
    serializeNullsField.setBoolean(jsonWriter, true);
    assertTrue(jsonWriter.getSerializeNulls());
  }
}