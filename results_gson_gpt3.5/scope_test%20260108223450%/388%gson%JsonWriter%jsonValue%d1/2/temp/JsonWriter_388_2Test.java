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

class JsonWriter_388_2Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void jsonValue_null_callsNullValueAndReturnsThis() throws IOException {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.jsonValue(null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void jsonValue_nonNull_writesValue() throws Exception {
    String value = "{\"a\":1}";
    JsonWriter spyWriter = spy(jsonWriter);

    // Remove stubbing of private methods; call jsonValue directly
    JsonWriter result = spyWriter.jsonValue(value);

    assertSame(spyWriter, result);
    assertEquals(value, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void jsonValue_invokesPrivateMethodsAndAppendsValue() throws Exception {
    String value = "123";

    // Reset stack for a fresh document state to avoid IllegalStateException
    Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTop.setAccessible(true);

    // Set stack to EMPTY_DOCUMENT (value 6) to simulate fresh state
    replaceTop.invoke(jsonWriter, 6);

    JsonWriter result = jsonWriter.jsonValue(value);

    assertSame(jsonWriter, result);
    assertTrue(stringWriter.toString().contains(value));
  }
}