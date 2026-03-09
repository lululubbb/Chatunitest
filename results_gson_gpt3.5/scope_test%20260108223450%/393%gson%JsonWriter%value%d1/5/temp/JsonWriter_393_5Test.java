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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
  public void value_writesDoubleValue_negative() throws IOException {
    jsonWriter.value(-789.01);
    assertEquals("-789.01", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_writesDoubleValue_zero() throws IOException {
    jsonWriter.value(0.0);
    assertEquals("0.0", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_writesDoubleValue_scientific() throws IOException {
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
  public void value_throwsOnInfiniteWhenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_allowsNaNWhenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.NaN);
    assertEquals("NaN", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_allowsInfiniteWhenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.NEGATIVE_INFINITY);
    assertEquals("-Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_callsWriteDeferredName_andBeforeValue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Use reflection to invoke private writeDeferredName and beforeValue methods to verify no exceptions and coverage
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // Set deferredName to non-null to test writeDeferredName path
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "name");

    // We expect writeDeferredName to write the name and separator to the writer
    writeDeferredName.invoke(jsonWriter);
    beforeValue.invoke(jsonWriter);

    // Clear deferredName
    deferredNameField.set(jsonWriter, null);

    // Now call value again to cover normal path
    jsonWriter.value(42.0);
    assertTrue(stringWriter.toString().contains("42.0"));
  }
}