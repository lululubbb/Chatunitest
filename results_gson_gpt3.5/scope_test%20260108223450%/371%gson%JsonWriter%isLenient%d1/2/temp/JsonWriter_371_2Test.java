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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.lang.reflect.Field;

public class JsonWriter_371_2Test {

  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    jsonWriter = new JsonWriter(new StringWriter());
  }

  @Test
    @Timeout(8000)
  public void testIsLenient_DefaultFalse() {
    // By default, lenient should be false
    assertFalse(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  public void testIsLenient_SetTrue() throws Exception {
    // Use reflection to set private field 'lenient' to true
    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonWriter, true);

    assertTrue(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  public void testIsLenient_SetFalse() throws Exception {
    // Use reflection to set private field 'lenient' to false explicitly
    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonWriter, false);

    assertFalse(jsonWriter.isLenient());
  }
}