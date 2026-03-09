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

public class JsonWriter_389_3Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testNullValue_noDeferredName_serializesNull() throws IOException {
    // deferredName == null, serializeNulls true by default
    jsonWriter.nullValue();
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testNullValue_deferredNameNullsTrue_writesDeferredNameAndNull() throws IOException {
    // Setup JsonWriter state to be valid for writing deferredName
    jsonWriter.beginObject();
    setField(jsonWriter, "deferredName", "key");
    jsonWriter.setSerializeNulls(true);

    jsonWriter.nullValue();

    assertNull(getField(jsonWriter, "deferredName"));

    String output = stringWriter.toString();
    assertTrue(output.contains("\"key\""));
    assertTrue(output.contains("null"));
  }

  @Test
    @Timeout(8000)
  public void testNullValue_deferredNameNullsFalse_skipsNameAndWritesNull() throws IOException {
    jsonWriter.setSerializeNulls(false);
    jsonWriter.nullValue();

    assertNull(getField(jsonWriter, "deferredName"));

    // Output should contain only "null" (no name)
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testNullValue_beforeValueCalled() throws IOException {
    jsonWriter.nullValue();
    assertEquals("null", stringWriter.toString());
  }

  // Utility method to set private fields via reflection
  private static void setField(Object target, String fieldName, Object value) {
    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Utility method to get private field value via reflection
  private static Object getField(Object target, String fieldName) {
    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}