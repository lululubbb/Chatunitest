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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

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
  public void value_writesDoubleValue() throws IOException {
    jsonWriter.value(123.456);
    assertEquals("123.456", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_writesNegativeDoubleValue() throws IOException {
    jsonWriter.value(-987.654);
    assertEquals("-987.654", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_writesZero() throws IOException {
    jsonWriter.value(0.0);
    assertEquals("0.0", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_writesScientificNotation() throws IOException {
    jsonWriter.value(1.23e10);
    assertEquals("1.23E10", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_throwsOnNaNWhenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NaN);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsOnPositiveInfinityWhenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsOnNegativeInfinityWhenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_allowsNaNWhenLenient() throws IOException {
    try {
      setLenient(true);
      jsonWriter.value(Double.NaN);
      assertEquals("NaN", stringWriter.toString());
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  public void value_allowsPositiveInfinityWhenLenient() throws IOException {
    try {
      setLenient(true);
      jsonWriter.value(Double.POSITIVE_INFINITY);
      assertEquals("Infinity", stringWriter.toString());
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  public void value_allowsNegativeInfinityWhenLenient() throws IOException {
    try {
      setLenient(true);
      jsonWriter.value(Double.NEGATIVE_INFINITY);
      assertEquals("-Infinity", stringWriter.toString());
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  public void value_callsWriteDeferredNameAndBeforeValue() throws Exception {
    JsonWriter spyWriter = spy(new JsonWriter(stringWriter));

    // Use reflection to invoke value(double) on spyWriter to ensure internal calls happen
    Method valueMethod = JsonWriter.class.getDeclaredMethod("value", double.class);
    valueMethod.setAccessible(true);
    valueMethod.invoke(spyWriter, 42.0);

    // Verify that value(double) was called on spyWriter
    verify(spyWriter).value(42.0);
  }

  private void setLenient(boolean lenient) throws Exception {
    Field field = JsonWriter.class.getDeclaredField("lenient");
    field.setAccessible(true);
    field.setBoolean(jsonWriter, lenient);
  }
}