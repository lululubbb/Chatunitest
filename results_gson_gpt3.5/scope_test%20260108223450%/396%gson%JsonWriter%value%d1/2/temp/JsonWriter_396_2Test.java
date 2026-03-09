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

public class JsonWriter_396_2Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testValue_withNull_callsNullValue() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Number) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void testValue_withFiniteNumber_writesNumber() throws IOException {
    jsonWriter.value(123);
    assertEquals("123", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_withNegativeInfinity_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("-Infinity"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withPositiveInfinity_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("Infinity"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNaN_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(Double.NaN);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withLenientInfinityAndNaN_doesNotThrow() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(Double.POSITIVE_INFINITY);
    jsonWriter.value(Double.NEGATIVE_INFINITY);
    jsonWriter.value(Double.NaN);
    String output = stringWriter.toString();
    assertTrue(output.contains("Infinity"));
    assertTrue(output.contains("-Infinity"));
    assertTrue(output.contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withUntrustedNumberType_invalidJsonNumberString_throwsException() {
    Number invalidNumber = new Number() {
      @Override
      public int intValue() {
        return 0;
      }

      @Override
      public long longValue() {
        return 0L;
      }

      @Override
      public float floatValue() {
        return 0f;
      }

      @Override
      public double doubleValue() {
        return 0d;
      }

      @Override
      public String toString() {
        return "invalid_number";
      }
    };

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonWriter.value(invalidNumber);
    });
    assertTrue(thrown.getMessage().contains("is not a valid JSON number"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withTrustedNumberType_andValidNumberString_writesNumber() throws IOException {
    // BigDecimal is trusted type
    BigDecimal bigDecimal = new BigDecimal("123.456e-7");
    jsonWriter.value(bigDecimal);
    // The output should be the normalized string representation of BigDecimal
    assertEquals(bigDecimal.toString(), stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_callsWriteDeferredName_andBeforeValue() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    // Use reflection to access private methods
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    // Begin array to avoid multiple top-level values error
    spyWriter.beginArray();

    // Invoke writeDeferredName and beforeValue before writing the value
    writeDeferredNameMethod.invoke(spyWriter);
    beforeValueMethod.invoke(spyWriter);

    Number number = 42;
    spyWriter.value(number);

    spyWriter.endArray();

    assertEquals("[42]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testIsTrustedNumberType_staticMethod() throws Exception {
    Method isTrustedNumberType = JsonWriter.class.getDeclaredMethod("isTrustedNumberType", Class.class);
    isTrustedNumberType.setAccessible(true);

    assertTrue((Boolean) isTrustedNumberType.invoke(null, Integer.class));
    assertTrue((Boolean) isTrustedNumberType.invoke(null, Long.class));
    assertTrue((Boolean) isTrustedNumberType.invoke(null, Double.class));
    assertTrue((Boolean) isTrustedNumberType.invoke(null, Float.class));
    assertTrue((Boolean) isTrustedNumberType.invoke(null, Short.class));
    assertTrue((Boolean) isTrustedNumberType.invoke(null, Byte.class));
    assertTrue((Boolean) isTrustedNumberType.invoke(null, BigDecimal.class));
    assertTrue((Boolean) isTrustedNumberType.invoke(null, BigInteger.class));
    assertFalse((Boolean) isTrustedNumberType.invoke(null, Number.class));
    assertFalse((Boolean) isTrustedNumberType.invoke(null, Object.class));
  }
}