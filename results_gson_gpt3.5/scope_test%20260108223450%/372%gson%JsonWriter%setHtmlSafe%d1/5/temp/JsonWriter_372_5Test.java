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
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_372_5Test {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  public void setUp() {
    mockWriter = new StringWriter();
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testSetHtmlSafeTrue() throws NoSuchFieldException, IllegalAccessException {
    jsonWriter.setHtmlSafe(true);

    Field htmlSafeField = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    boolean htmlSafeValue = (boolean) htmlSafeField.get(jsonWriter);

    assertTrue(htmlSafeValue, "htmlSafe field should be true after setHtmlSafe(true)");
  }

  @Test
    @Timeout(8000)
  public void testSetHtmlSafeFalse() throws NoSuchFieldException, IllegalAccessException {
    // Set true first to verify change to false
    jsonWriter.setHtmlSafe(true);
    jsonWriter.setHtmlSafe(false);

    Field htmlSafeField = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    boolean htmlSafeValue = (boolean) htmlSafeField.get(jsonWriter);

    assertFalse(htmlSafeValue, "htmlSafe field should be false after setHtmlSafe(false)");
  }
}