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
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.reflect.Method;

class JsonWriter_395_4Test {

  @Test
    @Timeout(8000)
  void testIsTrustedNumberType() throws Exception {
    Method method = JsonWriter.class.getDeclaredMethod("isTrustedNumberType", Class.class);
    method.setAccessible(true);

    // Trusted types
    assertTrue((Boolean) method.invoke(null, Integer.class));
    assertTrue((Boolean) method.invoke(null, Long.class));
    assertTrue((Boolean) method.invoke(null, Double.class));
    assertTrue((Boolean) method.invoke(null, Float.class));
    assertTrue((Boolean) method.invoke(null, Byte.class));
    assertTrue((Boolean) method.invoke(null, Short.class));
    assertTrue((Boolean) method.invoke(null, BigDecimal.class));
    assertTrue((Boolean) method.invoke(null, BigInteger.class));
    assertTrue((Boolean) method.invoke(null, AtomicInteger.class));
    assertTrue((Boolean) method.invoke(null, AtomicLong.class));

    // Untrusted types
    assertFalse((Boolean) method.invoke(null, Number.class));
    assertFalse((Boolean) method.invoke(null, String.class));
    assertFalse((Boolean) method.invoke(null, Object.class));
    assertFalse((Boolean) method.invoke(null, Void.class));
    assertFalse((Boolean) method.invoke(null, Exception.class));
  }
}