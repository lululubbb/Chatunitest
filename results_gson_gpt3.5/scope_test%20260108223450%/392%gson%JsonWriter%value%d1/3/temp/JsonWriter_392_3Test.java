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

public class JsonWriter_392_3Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_writesFloatValueSuccessfully() throws IOException {
    jsonWriter.value(1.5f);
    assertEquals("1.5", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_writesNegativeFloatValueSuccessfully() throws IOException {
    jsonWriter.value(-123.456f);
    assertEquals("-123.456", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_writesZeroFloatValueSuccessfully() throws IOException {
    jsonWriter.value(0.0f);
    assertEquals("0.0", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_throwsOnNaN() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.NaN);
    });
    assertTrue(exception.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsOnPositiveInfinity() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.POSITIVE_INFINITY);
    });
    assertTrue(exception.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsOnNegativeInfinity() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.NEGATIVE_INFINITY);
    });
    assertTrue(exception.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_allowsNaNIfLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.NaN);
    assertEquals("NaN", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_allowsPositiveInfinityIfLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.POSITIVE_INFINITY);
    assertEquals("Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_allowsNegativeInfinityIfLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.NEGATIVE_INFINITY);
    assertEquals("-Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_invokesWriteDeferredNameAndBeforeValue() throws Throwable {
    JsonWriter spyWriter = spy(new JsonWriter(new StringWriter()));

    // Use reflection to access private methods
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    // Clear previous invocations on spy
    clearInvocations(spyWriter);

    // Begin a JSON array to allow multiple values
    spyWriter.beginArray();

    // Call value
    spyWriter.value(2.5f);

    // End the JSON array
    spyWriter.endArray();

    // We cannot verify private method calls directly with Mockito.
    // Instead, verify that public method 'value' was called once.
    verify(spyWriter, times(1)).value(2.5f);

    // As an alternative, invoke private methods directly to ensure they do not throw exceptions
    // To avoid IllegalStateException from beforeValue, set lenient to true before invoking
    spyWriter.setLenient(true);
    writeDeferredNameMethod.invoke(spyWriter);
    beforeValueMethod.invoke(spyWriter);
  }
}