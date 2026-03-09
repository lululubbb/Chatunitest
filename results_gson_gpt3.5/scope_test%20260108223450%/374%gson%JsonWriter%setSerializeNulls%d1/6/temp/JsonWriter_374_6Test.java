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
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

class JsonWriter_374_6Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void testSetSerializeNulls_true() throws Exception {
    // Initially serializeNulls is true by default
    assertTrue(jsonWriter.getSerializeNulls());

    // Change serializeNulls to false
    jsonWriter.setSerializeNulls(false);
    assertFalse(jsonWriter.getSerializeNulls());

    // Change back to true
    jsonWriter.setSerializeNulls(true);
    assertTrue(jsonWriter.getSerializeNulls());

    // Use reflection to verify private field serializeNulls
    var field = JsonWriter.class.getDeclaredField("serializeNulls");
    field.setAccessible(true);
    assertEquals(true, field.getBoolean(jsonWriter));
  }

  @Test
    @Timeout(8000)
  void testSetSerializeNulls_reflectionInvoke() throws Exception {
    Method setSerializeNullsMethod = JsonWriter.class.getDeclaredMethod("setSerializeNulls", boolean.class);
    setSerializeNullsMethod.setAccessible(true);

    // Initially true
    assertTrue(jsonWriter.getSerializeNulls());

    // Invoke with false
    setSerializeNullsMethod.invoke(jsonWriter, false);
    assertFalse(jsonWriter.getSerializeNulls());

    // Invoke with true
    setSerializeNullsMethod.invoke(jsonWriter, true);
    assertTrue(jsonWriter.getSerializeNulls());
  }
}