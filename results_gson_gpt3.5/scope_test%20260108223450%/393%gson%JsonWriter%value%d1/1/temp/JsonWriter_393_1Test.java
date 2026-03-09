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

public class JsonWriter_393_1Test {

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
  public void value_throwsOnNaN() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NaN);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsOnPositiveInfinity() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsOnNegativeInfinity() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_allowsNaNWhenLenient() throws Exception {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.NaN);
    assertEquals("NaN", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_allowsPositiveInfinityWhenLenient() throws Exception {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.POSITIVE_INFINITY);
    assertEquals("Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_allowsNegativeInfinityWhenLenient() throws Exception {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.NEGATIVE_INFINITY);
    assertEquals("-Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_callsWriteDeferredNameAndBeforeValue() throws Exception {
    // Use spy to verify private method calls via reflection
    JsonWriter spyWriter = spy(new JsonWriter(stringWriter));

    // Use reflection to set private field deferredName
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(spyWriter, "name");

    // Use reflection to invoke private method push(int)
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(spyWriter, JsonScope.EMPTY_OBJECT);

    spyWriter.value(42.0);

    // deferredName should be null after value call
    assertNull(deferredNameField.get(spyWriter));

    // Output should contain the double value
    assertTrue(stringWriter.toString().contains("42.0"));
  }

  @Test
    @Timeout(8000)
  public void value_appendsDoubleToOut() throws IOException {
    // Setup to check output appending
    jsonWriter.value(3.14);
    assertEquals("3.14", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_handlesMultipleValues() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(1.0);
    jsonWriter.value(2.0);
    jsonWriter.endArray();
    assertEquals("[1.0,2.0]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_withDeferredName() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.name("key");
    jsonWriter.value(10.5);
    jsonWriter.endObject();
    String output = stringWriter.toString();
    assertTrue(output.contains("10.5"));
  }

}