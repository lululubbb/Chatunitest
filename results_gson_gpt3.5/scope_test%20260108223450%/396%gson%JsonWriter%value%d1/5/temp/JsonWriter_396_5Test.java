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
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_396_5Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testValue_NullValue_CallsNullValue() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Number) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void testValue_ValidInteger_WritesNumber() throws IOException {
    jsonWriter.value(123);
    assertEquals("123", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_ValidDouble_WritesNumber() throws IOException {
    jsonWriter.value(123.456);
    assertEquals("123.456", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_ValidBigDecimal_WritesNumber() throws IOException {
    jsonWriter.value(new BigDecimal("12345.6789"));
    assertEquals("12345.6789", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_ValidBigInteger_WritesNumber() throws IOException {
    jsonWriter.value(new BigInteger("9876543210"));
    assertEquals("9876543210", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_InfinityWithoutLenient_Throws() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void testValue_NaNWithoutLenient_Throws() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NaN);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void testValue_InfinityWithLenient_DoesNotThrow() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.POSITIVE_INFINITY);
    assertEquals("Infinity", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_NaNWithLenient_DoesNotThrow() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.NaN);
    assertEquals("NaN", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_UntrustedNumberTypeWithValidString_WritesNumber() throws IOException {
    Number number = new Number() {
      @Override public int intValue() { return 0; }
      @Override public long longValue() { return 0L; }
      @Override public float floatValue() { return 0f; }
      @Override public double doubleValue() { return 1.23; }
      @Override public String toString() { return "1.23"; }
    };
    // forcibly set lenient false to test validation
    jsonWriter.setLenient(false);
    jsonWriter.value(number);
    assertEquals("1.23", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_UntrustedNumberTypeWithInvalidString_Throws() {
    Number number = new Number() {
      @Override public int intValue() { return 0; }
      @Override public long longValue() { return 0L; }
      @Override public float floatValue() { return 0f; }
      @Override public double doubleValue() { return 0; }
      @Override public String toString() { return "invalid123"; }
    };
    jsonWriter.setLenient(false);
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(number);
    });
    assertTrue(thrown.getMessage().contains("is not a valid JSON number"));
  }

  @Test
    @Timeout(8000)
  public void testValue_Branches_beforeValueAndWriteDeferredName() throws Exception {
    // Use spy to verify private methods called via reflection
    JsonWriter spyWriter = spy(new JsonWriter(stringWriter));

    // Begin an object to allow name() call without nesting error
    spyWriter.beginObject();

    // Setup deferredName so writeDeferredName does nothing
    spyWriter.name("testName");

    // Use reflection to spy on private writeDeferredName
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);

    // Use reflection to spy on private beforeValue
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    // call value with a valid number
    spyWriter.value(42);

    // End object to clean up
    spyWriter.endObject();

    // We cannot directly verify private method calls with spy, but if no exceptions thrown and output correct, coverage is good
    assertEquals("{\"testName\":42}", stringWriter.toString());
  }
}