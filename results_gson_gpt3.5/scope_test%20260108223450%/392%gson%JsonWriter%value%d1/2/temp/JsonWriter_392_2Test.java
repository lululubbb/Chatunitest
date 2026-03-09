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

public class JsonWriter_392_2Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_writesFloatValueSuccessfully() throws Exception {
    // Arrange
    jsonWriter.setLenient(false);
    // call private writeDeferredName() and beforeValue() indirectly by calling value()
    float testValue = 123.45f;

    // Act
    JsonWriter returned = jsonWriter.value(testValue);

    // Assert
    assertSame(jsonWriter, returned);
    assertEquals(Float.toString(testValue), stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_throwsIllegalArgumentException_whenFloatIsNaN() {
    float nanValue = Float.NaN;
    jsonWriter.setLenient(false);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(nanValue);
    });

    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_throwsIllegalArgumentException_whenFloatIsInfinite() {
    float infValue = Float.POSITIVE_INFINITY;
    jsonWriter.setLenient(false);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(infValue);
    });

    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_allowsNaNAndInfinite_whenLenientTrue() throws IOException {
    jsonWriter.setLenient(true);

    // NaN
    jsonWriter.value(Float.NaN);
    assertEquals("NaN", stringWriter.toString());
    stringWriter.getBuffer().setLength(0);

    // Positive Infinity
    jsonWriter.value(Float.POSITIVE_INFINITY);
    assertEquals("Infinity", stringWriter.toString());
    stringWriter.getBuffer().setLength(0);

    // Negative Infinity
    jsonWriter.value(Float.NEGATIVE_INFINITY);
    assertEquals("-Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_invokesWriteDeferredNameAndBeforeValue() throws Exception {
    // To avoid the "Nesting problem" exception, we must begin an object before setting deferredName
    jsonWriter.beginObject();

    // Use reflection to access private fields and methods
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);

    // Set deferredName via reflection
    deferredNameField.set(jsonWriter, "myName");

    // Call value(float)
    jsonWriter.value(1.23f);

    // After calling value, deferredName should be null
    assertNull(deferredNameField.get(jsonWriter));

    // The output should contain the name string (quoted) plus separator plus the float value
    String output = stringWriter.toString();
    assertTrue(output.contains("\"myName\""));
    assertTrue(output.contains(":"));
    assertTrue(output.contains("1.23"));

    // Close the object to maintain JSON structure correctness
    jsonWriter.endObject();
  }
}