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
import org.mockito.Mockito;

class JsonWriter_value_Boolean_Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_nullValue_callsNullValueAndReturnsJsonWriter() throws IOException {
    // Spy to verify nullValue() call
    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    JsonWriter returned = spyWriter.value((Boolean) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  void value_true_writesTrue() throws IOException {
    JsonWriter writer = jsonWriter.value(true);

    assertSame(jsonWriter, writer);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_false_writesFalse() throws IOException {
    JsonWriter writer = jsonWriter.value(false);

    assertSame(jsonWriter, writer);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_callsWriteDeferredNameAndBeforeValue() throws Exception {
    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    // Setup deferredName to non-null to trigger writeDeferredName() logic
    spyWriter.name("key");

    // Clear the StringWriter before calling value to isolate output
    stringWriter.getBuffer().setLength(0);

    spyWriter.value(true);

    // Verify private methods were called via spy using Mockito verify (not reflection invoke)
    verify(spyWriter).writeDeferredName();
    verify(spyWriter).beforeValue();
  }

  @Test
    @Timeout(8000)
  void value_afterBeginObjectWithName_writesNameAndBoolean() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.name("bool");
    jsonWriter.value(true);
    jsonWriter.endObject();

    assertEquals("{\"bool\":true}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_afterBeginArray_writesBoolean() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(false);
    jsonWriter.endArray();

    assertEquals("[false]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_withLenientTrue_writesBoolean() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(true);

    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_withHtmlSafeTrue_writesBoolean() throws IOException {
    jsonWriter.setHtmlSafe(true);
    jsonWriter.value(false);

    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_withSerializeNullsFalse_andNullValue_returnsJsonWriter() throws IOException {
    JsonWriter spyWriter = Mockito.spy(jsonWriter);
    spyWriter.setSerializeNulls(false);

    JsonWriter returned = spyWriter.value((Boolean) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, returned);
  }
}