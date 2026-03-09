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
import java.io.Writer;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_372_3Test {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  public void setUp() {
    mockWriter = new StringWriter();
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testSetHtmlSafe_true() throws Exception {
    // Initially htmlSafe is false
    assertFalse(jsonWriter.isHtmlSafe());

    // Use reflection to invoke setHtmlSafe
    Method setHtmlSafeMethod = JsonWriter.class.getDeclaredMethod("setHtmlSafe", boolean.class);
    setHtmlSafeMethod.setAccessible(true);
    setHtmlSafeMethod.invoke(jsonWriter, true);

    // Verify htmlSafe is now true
    assertTrue(jsonWriter.isHtmlSafe());
  }

  @Test
    @Timeout(8000)
  public void testSetHtmlSafe_false() throws Exception {
    // Set htmlSafe true first
    Method setHtmlSafeMethod = JsonWriter.class.getDeclaredMethod("setHtmlSafe", boolean.class);
    setHtmlSafeMethod.setAccessible(true);
    setHtmlSafeMethod.invoke(jsonWriter, true);
    assertTrue(jsonWriter.isHtmlSafe());

    // Now set htmlSafe false
    setHtmlSafeMethod.invoke(jsonWriter, false);
    assertFalse(jsonWriter.isHtmlSafe());
  }
}