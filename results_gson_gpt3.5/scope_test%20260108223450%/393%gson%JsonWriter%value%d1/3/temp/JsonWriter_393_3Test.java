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
import java.io.Writer;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

class JsonWriter_value_Test {
  private Writer out;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    out = mock(Writer.class);
    jsonWriter = new JsonWriter(out);
  }

  @Test
    @Timeout(8000)
  public void value_writesDoubleValue() throws IOException {
    jsonWriter.value(123.456);

    verify(out).append("123.456");
  }

  @Test
    @Timeout(8000)
  public void value_writesNegativeDoubleValue() throws IOException {
    jsonWriter.value(-987.654);

    verify(out).append("-987.654");
  }

  @Test
    @Timeout(8000)
  public void value_writesZero() throws IOException {
    jsonWriter.value(0.0);

    verify(out).append("0.0");
  }

  @Test
    @Timeout(8000)
  public void value_writesDoubleWithExponent() throws IOException {
    jsonWriter.value(1.23e10);

    verify(out).append("1.23E10");
  }

  @Test
    @Timeout(8000)
  public void value_throwsForNaN_whenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NaN);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsForPositiveInfinity_whenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsForNegativeInfinity_whenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_allowsNaN_whenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.NaN);

    verify(out).append("NaN");
  }

  @Test
    @Timeout(8000)
  public void value_allowsPositiveInfinity_whenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.POSITIVE_INFINITY);

    verify(out).append("Infinity");
  }

  @Test
    @Timeout(8000)
  public void value_allowsNegativeInfinity_whenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.NEGATIVE_INFINITY);

    verify(out).append("-Infinity");
  }

  @Test
    @Timeout(8000)
  public void value_invokesWriteDeferredName() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    spyWriter.value(1.23);

    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    writeDeferredNameMethod.invoke(spyWriter);
    verify(spyWriter, times(1)).value(1.23);
  }

  @Test
    @Timeout(8000)
  public void value_invokesBeforeValue() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    spyWriter.value(4.56);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
    beforeValueMethod.invoke(spyWriter);
    verify(spyWriter, times(1)).value(4.56);
  }
}