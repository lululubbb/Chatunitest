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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

class JsonWriter_value_Boolean_Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() throws IOException {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
  }

  @Test
    @Timeout(8000)
  public void value_null_returnsNullValue() throws IOException {
    JsonWriter returned = jsonWriter.value((Boolean) null);

    assertSame(jsonWriter, returned);
    assertEquals("[null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_true_writesTrue() throws IOException {
    JsonWriter returned = jsonWriter.value(Boolean.TRUE);

    assertSame(jsonWriter, returned);
    assertEquals("[true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_false_writesFalse() throws IOException {
    JsonWriter returned = jsonWriter.value(Boolean.FALSE);

    assertSame(jsonWriter, returned);
    assertEquals("[false", stringWriter.toString());
  }
}