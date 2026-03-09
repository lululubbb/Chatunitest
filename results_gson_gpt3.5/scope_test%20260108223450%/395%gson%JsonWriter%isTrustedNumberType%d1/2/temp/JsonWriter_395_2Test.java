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

class JsonWriter_395_2Test {

  private static Method isTrustedNumberTypeMethod;

  @BeforeAll
  static void setUp() throws Exception {
    isTrustedNumberTypeMethod = JsonWriter.class.getDeclaredMethod("isTrustedNumberType", Class.class);
    isTrustedNumberTypeMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withInteger() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, Integer.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withLong() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, Long.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withDouble() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, Double.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withFloat() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, Float.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withByte() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, Byte.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withShort() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, Short.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withBigDecimal() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, BigDecimal.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withBigInteger() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, BigInteger.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withAtomicInteger() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, AtomicInteger.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withAtomicLong() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, AtomicLong.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withNonTrustedClass() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, Number.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType_withNull() throws Exception {
    boolean result = (boolean) isTrustedNumberTypeMethod.invoke(null, new Object() {
      @Override
      public String toString() {
        return "NotANumberClass";
      }
    }.getClass());
    assertFalse(result);
  }
}