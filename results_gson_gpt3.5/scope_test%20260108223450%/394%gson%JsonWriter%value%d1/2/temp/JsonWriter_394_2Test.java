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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

class JsonWriter_value_Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_writesLongValue_correctly() throws Exception {
    // Call value normally, no mocking of private methods
    JsonWriter returned = jsonWriter.value(123456789L);

    assertSame(jsonWriter, returned);
    assertEquals("123456789", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_invokesPrivateWriteDeferredName_andBeforeValue() throws Throwable {
    // Use reflection to invoke private methods to ensure they exist and callable
    var writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);
    var beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // invoke private methods without error
    writeDeferredName.invoke(jsonWriter);
    beforeValue.invoke(jsonWriter);
  }

  @Test
    @Timeout(8000)
  public void value_withNegativeLong_writesCorrectString() throws IOException {
    jsonWriter.value(-9876543210L);

    assertEquals("-9876543210", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_withZero_writesZero() throws IOException {
    jsonWriter.value(0L);

    assertEquals("0", stringWriter.toString());
  }
}