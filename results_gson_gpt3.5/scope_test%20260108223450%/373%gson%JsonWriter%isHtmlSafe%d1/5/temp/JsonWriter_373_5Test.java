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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_373_5Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testIsHtmlSafe_defaultFalse() throws Exception {
    // By default, htmlSafe should be false
    boolean result = jsonWriter.isHtmlSafe();
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsHtmlSafe_afterSetHtmlSafeTrue() throws Exception {
    jsonWriter.setHtmlSafe(true);
    boolean result = jsonWriter.isHtmlSafe();
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsHtmlSafe_afterSetHtmlSafeFalse() throws Exception {
    jsonWriter.setHtmlSafe(true);
    jsonWriter.setHtmlSafe(false);
    boolean result = jsonWriter.isHtmlSafe();
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsHtmlSafe_reflectivelySetTrue() throws Exception {
    // Use reflection to set private field htmlSafe to true
    Field htmlSafeField = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    htmlSafeField.setBoolean(jsonWriter, true);

    boolean result = jsonWriter.isHtmlSafe();
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsHtmlSafe_reflectivelySetFalse() throws Exception {
    // Use reflection to set private field htmlSafe to false
    Field htmlSafeField = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    htmlSafeField.setBoolean(jsonWriter, false);

    boolean result = jsonWriter.isHtmlSafe();
    assertFalse(result);
  }
}