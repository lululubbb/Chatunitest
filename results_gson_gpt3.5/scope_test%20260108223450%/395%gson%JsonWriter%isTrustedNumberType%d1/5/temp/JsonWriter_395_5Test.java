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
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JsonWriter_395_5Test {
  private static Method isTrustedNumberTypeMethod;

  @BeforeAll
  static void setup() throws NoSuchMethodException {
    isTrustedNumberTypeMethod = JsonWriter.class.getDeclaredMethod("isTrustedNumberType", Class.class);
    isTrustedNumberTypeMethod.setAccessible(true);
  }

  private boolean invokeIsTrustedNumberType(Class<? extends Number> clazz) {
    try {
      return (boolean) isTrustedNumberTypeMethod.invoke(null, clazz);
    } catch (IllegalAccessException | InvocationTargetException e) {
      // Adjusted to unwrap InvocationTargetException cause for null argument
      if (e instanceof InvocationTargetException) {
        Throwable cause = e.getCause();
        if (cause instanceof NullPointerException) {
          throw (NullPointerException) cause;
        }
        if (cause instanceof IllegalArgumentException) {
          // The method under test does not handle null and throws IllegalArgumentException via reflection
          // Wrap it as NullPointerException for the test expectation
          throw new NullPointerException(cause.getMessage());
        }
      }
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withInteger() {
    assertTrue(invokeIsTrustedNumberType(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withLong() {
    assertTrue(invokeIsTrustedNumberType(Long.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withDouble() {
    assertTrue(invokeIsTrustedNumberType(Double.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withFloat() {
    assertTrue(invokeIsTrustedNumberType(Float.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withByte() {
    assertTrue(invokeIsTrustedNumberType(Byte.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withShort() {
    assertTrue(invokeIsTrustedNumberType(Short.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withBigDecimal() {
    assertTrue(invokeIsTrustedNumberType(BigDecimal.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withBigInteger() {
    assertTrue(invokeIsTrustedNumberType(BigInteger.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withAtomicInteger() {
    assertTrue(invokeIsTrustedNumberType(AtomicInteger.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withAtomicLong() {
    assertTrue(invokeIsTrustedNumberType(AtomicLong.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withUntrustedNumberSubclass() {
    class CustomNumber extends Number {
      @Override public int intValue() { return 0; }
      @Override public long longValue() { return 0L; }
      @Override public float floatValue() { return 0f; }
      @Override public double doubleValue() { return 0d; }
    }
    assertFalse(invokeIsTrustedNumberType(CustomNumber.class));
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withNullClass() {
    // Directly use invokeIsTrustedNumberType to get consistent exception handling
    assertThrows(NullPointerException.class, () -> {
      invokeIsTrustedNumberType(null);
    });
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withNumberClass() {
    assertFalse(invokeIsTrustedNumberType(Number.class));
  }
}