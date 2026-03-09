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
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JsonWriter_isTrustedNumberType_Test {

  private static Method isTrustedNumberTypeMethod;

  @BeforeAll
  static void setup() throws Exception {
    isTrustedNumberTypeMethod = JsonWriter.class.getDeclaredMethod("isTrustedNumberType", Class.class);
    isTrustedNumberTypeMethod.setAccessible(true);
  }

  @SuppressWarnings("unchecked")
  private boolean invokeIsTrustedNumberType(Class<?> clazz) throws Exception {
    return (boolean) isTrustedNumberTypeMethod.invoke(null, clazz);
  }

  @Test
    @Timeout(8000)
  void testTrustedTypes() throws Exception {
    assertTrue(invokeIsTrustedNumberType(Integer.class));
    assertTrue(invokeIsTrustedNumberType(Long.class));
    assertTrue(invokeIsTrustedNumberType(Double.class));
    assertTrue(invokeIsTrustedNumberType(Float.class));
    assertTrue(invokeIsTrustedNumberType(Byte.class));
    assertTrue(invokeIsTrustedNumberType(Short.class));
    assertTrue(invokeIsTrustedNumberType(BigDecimal.class));
    assertTrue(invokeIsTrustedNumberType(BigInteger.class));
    assertTrue(invokeIsTrustedNumberType(AtomicInteger.class));
    assertTrue(invokeIsTrustedNumberType(AtomicLong.class));
  }

  @Test
    @Timeout(8000)
  void testUntrustedTypes() throws Exception {
    // Common Number subclasses not trusted
    assertFalse(invokeIsTrustedNumberType(Number.class));
    assertFalse(invokeIsTrustedNumberType(java.util.concurrent.atomic.AtomicLongArray.class));
    assertFalse(invokeIsTrustedNumberType(java.util.concurrent.atomic.AtomicIntegerArray.class));
    assertFalse(invokeIsTrustedNumberType(java.math.MathContext.class));
    // Null class should throw NPE, but method expects non-null, so skip null test
  }

  @Test
    @Timeout(8000)
  void testCustomNumberSubclass() throws Exception {
    class CustomNumber extends Number {
      private static final long serialVersionUID = 1L;
      @Override public int intValue() { return 0; }
      @Override public long longValue() { return 0L; }
      @Override public float floatValue() { return 0f; }
      @Override public double doubleValue() { return 0d; }
    }
    assertFalse(invokeIsTrustedNumberType(CustomNumber.class));
  }
}