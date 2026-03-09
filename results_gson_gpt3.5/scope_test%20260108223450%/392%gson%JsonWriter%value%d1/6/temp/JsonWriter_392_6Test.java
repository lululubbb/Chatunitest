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

import com.google.gson.stream.JsonWriter;

class JsonWriter_value_Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_writesFloatValue() throws IOException {
    jsonWriter.value(1.5f);
    assertEquals("1.5", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_writesZero() throws IOException {
    jsonWriter.value(0f);
    assertEquals("0.0", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_writesNegativeFloat() throws IOException {
    jsonWriter.value(-123.456f);
    assertEquals("-123.456", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_throwsOnNaN() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.NaN);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  void value_throwsOnPositiveInfinity() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  void value_throwsOnNegativeInfinity() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  void value_allowsNaNIfLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.NaN);
    assertEquals("NaN", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_allowsPositiveInfinityIfLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.POSITIVE_INFINITY);
    assertEquals("Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_allowsNegativeInfinityIfLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.NEGATIVE_INFINITY);
    assertEquals("-Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_callsWriteDeferredNameAndBeforeValue() throws Exception {
    JsonWriter spyWriter = spy(new JsonWriter(new StringWriter()));

    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    writeDeferredName.setAccessible(true);
    beforeValue.setAccessible(true);

    doNothing().when(spyWriter).value(anyFloat()); // to avoid actual writing side effects

    // Call value(float) method on spy
    spyWriter.value(3.14f);

    // Verify private methods invoked via reflection
    verify(spyWriter, times(1)).getClass().getDeclaredMethod("writeDeferredName").invoke(spyWriter);
    verify(spyWriter, times(1)).getClass().getDeclaredMethod("beforeValue").invoke(spyWriter);
  }

  @Test
    @Timeout(8000)
  void value_privateMethods_writeDeferredName_and_beforeValue_invokedViaReflection() throws Exception {
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    writeDeferredName.setAccessible(true);
    beforeValue.setAccessible(true);

    writeDeferredName.invoke(jsonWriter);
    beforeValue.invoke(jsonWriter);

    // No exceptions means success
  }
}