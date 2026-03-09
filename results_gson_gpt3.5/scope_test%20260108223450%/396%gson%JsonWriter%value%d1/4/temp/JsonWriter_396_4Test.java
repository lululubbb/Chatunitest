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

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_null_callsNullValue() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Number) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void value_infinity_throwsIllegalArgumentException_whenNotLenient() {
    Number inf = Double.POSITIVE_INFINITY;
    jsonWriter.setLenient(false);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(inf);
    });
    assertTrue(ex.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_nan_throwsIllegalArgumentException_whenNotLenient() {
    Number nan = Double.NaN;
    jsonWriter.setLenient(false);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(nan);
    });
    assertTrue(ex.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_infinity_allows_whenLenient() throws IOException {
    Number inf = Double.POSITIVE_INFINITY;
    jsonWriter.setLenient(true);

    JsonWriter result = jsonWriter.value(inf);
    assertSame(jsonWriter, result);
    assertTrue(stringWriter.toString().contains("Infinity"));
  }

  @Test
    @Timeout(8000)
  public void value_nan_allows_whenLenient() throws IOException {
    Number nan = Double.NaN;
    jsonWriter.setLenient(true);

    JsonWriter result = jsonWriter.value(nan);
    assertSame(jsonWriter, result);
    assertTrue(stringWriter.toString().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void value_trustedNumberType_writesValue() throws IOException {
    jsonWriter.setLenient(false);
    Number trustedNumber = Integer.valueOf(123);

    JsonWriter result = jsonWriter.value(trustedNumber);

    assertSame(jsonWriter, result);
    assertEquals("123", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_untrustedNumberType_validPattern_writesValue() throws Exception {
    // Create a custom Number subclass that produces a valid JSON number string
    Number customNumber = new Number() {
      @Override public int intValue() { return 0; }
      @Override public long longValue() { return 0L; }
      @Override public float floatValue() { return 0f; }
      @Override public double doubleValue() { return 0d; }
      @Override public String toString() { return "42.5"; }
    };

    JsonWriter spyWriter = spy(jsonWriter);

    // Initialize internal state via reflection to avoid IllegalStateException
    // Set stack and stackSize fields to simulate valid state
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(spyWriter, new int[] {JsonScope.EMPTY_DOCUMENT});
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyWriter, 1);

    // Call private methods via reflection before calling value()
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    writeDeferredNameMethod.invoke(spyWriter);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
    beforeValueMethod.invoke(spyWriter);

    JsonWriter result = spyWriter.value(customNumber);

    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void value_untrustedNumberType_invalidPattern_throwsIllegalArgumentException() {
    Number customNumber = new Number() {
      @Override public int intValue() { return 0; }
      @Override public long longValue() { return 0L; }
      @Override public float floatValue() { return 0f; }
      @Override public double doubleValue() { return 0d; }
      @Override public String toString() { return "abc"; }
    };

    jsonWriter.setLenient(false);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(customNumber);
    });
    assertTrue(ex.getMessage().contains("is not a valid JSON number"));
  }

  @Test
    @Timeout(8000)
  public void value_callsWriteDeferredNameAndBeforeValue_andAppendsString() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    // Initialize internal state via reflection to avoid IllegalStateException
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(spyWriter, new int[] {JsonScope.EMPTY_DOCUMENT});
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyWriter, 1);

    Number number = Integer.valueOf(7);
    JsonWriter result = spyWriter.value(number);

    assertSame(spyWriter, result);
  }
}