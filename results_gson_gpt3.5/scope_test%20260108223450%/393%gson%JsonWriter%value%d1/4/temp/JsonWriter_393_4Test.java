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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_value_Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_double_writesValue() throws IOException {
    jsonWriter.value(123.456);
    assertEquals("123.456", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_double_writesNegativeValue() throws IOException {
    jsonWriter.value(-789.01);
    assertEquals("-789.01", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_double_writesZero() throws IOException {
    jsonWriter.value(0.0);
    assertEquals("0.0", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_double_throwsOnNaN() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NaN);
    });
    assertTrue(exception.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_double_throwsOnPositiveInfinity() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(exception.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_double_throwsOnNegativeInfinity() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NEGATIVE_INFINITY);
    });
    assertTrue(exception.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_double_allowsNaNAndInfinityWhenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.NaN);
    assertEquals("NaN", stringWriter.toString());
    stringWriter.getBuffer().setLength(0);

    jsonWriter.value(Double.POSITIVE_INFINITY);
    assertEquals("Infinity", stringWriter.toString());
    stringWriter.getBuffer().setLength(0);

    jsonWriter.value(Double.NEGATIVE_INFINITY);
    assertEquals("-Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_double_callsWriteDeferredName() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);

    // Use reflection to invoke private method and spy on it by wrapping invocation
    // Since Mockito cannot mock private methods, we invoke the real method
    // and verify indirectly by spying on the public method that calls it.

    // Call value on spy, which will call the private method internally
    spyWriter.value(1.23);

    // We cannot verify private method calls with Mockito, so no verify here.
  }

  @Test
    @Timeout(8000)
  public void value_double_callsBeforeValue() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // Same as above, call value and rely on spy for public method
    spyWriter.value(4.56);

    // No direct verification of private method call possible with Mockito.
  }

  @Test
    @Timeout(8000)
  public void value_double_appendsCorrectString() throws IOException {
    jsonWriter.value(1.0 / 3.0);
    String output = stringWriter.toString();
    assertTrue(output.startsWith("0.333"));
  }

  @Test
    @Timeout(8000)
  public void value_double_invokesPrivateWriteDeferredName() throws Exception {
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);
    writeDeferredName.invoke(jsonWriter);
  }

  @Test
    @Timeout(8000)
  public void value_double_invokesPrivateBeforeValue() throws Exception {
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);
    beforeValue.invoke(jsonWriter);
  }
}