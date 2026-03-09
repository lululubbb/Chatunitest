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

import com.google.gson.stream.JsonWriter;

class JsonWriter_370_5Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void testSetLenientTrue() {
    jsonWriter.setLenient(true);
    assertTrue(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  void testSetLenientFalse() {
    jsonWriter.setLenient(false);
    assertFalse(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  void testSetLenientViaReflection() throws Exception {
    Method setLenientMethod = JsonWriter.class.getDeclaredMethod("setLenient", boolean.class);
    setLenientMethod.setAccessible(true);

    // invoke with true
    setLenientMethod.invoke(jsonWriter, true);
    Method isLenientMethod = JsonWriter.class.getDeclaredMethod("isLenient");
    isLenientMethod.setAccessible(true);
    boolean lenientTrue = (boolean) isLenientMethod.invoke(jsonWriter);
    assertTrue(lenientTrue);

    // invoke with false
    setLenientMethod.invoke(jsonWriter, false);
    boolean lenientFalse = (boolean) isLenientMethod.invoke(jsonWriter);
    assertFalse(lenientFalse);
  }
}