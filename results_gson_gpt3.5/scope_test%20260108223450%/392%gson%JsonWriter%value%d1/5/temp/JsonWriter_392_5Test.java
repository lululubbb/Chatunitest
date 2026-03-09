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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_392_5Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_writesFiniteFloat_appendsValue() throws IOException {
    jsonWriter.value(1.5f);
    assertEquals("1.5", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_writesNegativeFloat_appendsValue() throws IOException {
    jsonWriter.value(-2.75f);
    assertEquals("-2.75", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_writesZeroFloat_appendsValue() throws IOException {
    jsonWriter.value(0.0f);
    assertEquals("0.0", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_throwsForNaN_whenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.NaN);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  void value_throwsForPositiveInfinity_whenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  void value_throwsForNegativeInfinity_whenNotLenient() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Float.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  void value_allowsNaN_whenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.NaN);
    assertEquals("NaN", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_allowsPositiveInfinity_whenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.POSITIVE_INFINITY);
    assertEquals("Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_allowsNegativeInfinity_whenLenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Float.NEGATIVE_INFINITY);
    assertEquals("-Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_callsWriteDeferredName_andBeforeValue() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    // Set stack and stackSize to simulate that we are inside an object expecting a name
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(spyWriter);
    stack[0] = 5; // 5 == DANGLING_NAME according to JsonScope
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyWriter, 1);

    // Use reflection to reset deferredName to non-null to test writeDeferredName invocation
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(spyWriter, "name");

    // Call value
    spyWriter.value(3.14f);

    // Verify deferredName was cleared
    assertNull(deferredNameField.get(spyWriter));

    // Verify output contains the name and value as expected
    assertEquals(",\"name\":3.14", stringWriter.toString());
  }
}