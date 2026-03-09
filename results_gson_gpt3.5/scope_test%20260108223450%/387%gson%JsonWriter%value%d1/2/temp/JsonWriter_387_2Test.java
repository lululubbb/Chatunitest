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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_value_Test {

  private JsonWriter writer;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    writer = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_withNonNullString_writesString() throws Exception {
    writer.value("hello");
    String output = stringWriter.toString();
    assertTrue(output.contains("\"hello\""));
  }

  @Test
    @Timeout(8000)
  void value_withNullString_callsNullValue() throws IOException {
    JsonWriter spyWriter = spy(writer);

    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((String) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void private_beforeValue_invocation_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    Method push = JsonWriter.class.getDeclaredMethod("push", int.class);
    push.setAccessible(true);

    push.invoke(writer, 1); // EMPTY_DOCUMENT
    beforeValue.invoke(writer);

    push.invoke(writer, 3); // EMPTY_ARRAY
    beforeValue.invoke(writer);

    push.invoke(writer, 4); // NONEMPTY_ARRAY
    beforeValue.invoke(writer);

    push.invoke(writer, 2); // DANGLING_NAME
    beforeValue.invoke(writer);

    push.invoke(writer, 6); // NONEMPTY_OBJECT
    beforeValue.invoke(writer);

    push.invoke(writer, 5); // NONEMPTY_DOCUMENT
    beforeValue.invoke(writer);
  }

  @Test
    @Timeout(8000)
  void private_string_invocation_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);

    stringMethod.invoke(writer, "abc");
    stringMethod.invoke(writer, "");
  }

  @Test
    @Timeout(8000)
  void value_returnsThis_forNonNull() throws IOException {
    JsonWriter result = writer.value("test");
    assertSame(writer, result);
  }

  @Test
    @Timeout(8000)
  void value_returnsNullValue_result_forNull() throws IOException {
    JsonWriter spy = spy(writer);
    doReturn(spy).when(spy).nullValue();

    JsonWriter result = spy.value((String) null);

    assertSame(spy, result);
  }
}