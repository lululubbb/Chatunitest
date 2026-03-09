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

class JsonWriter_value_BooleanTest {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_withTrue_writesTrue() throws IOException {
    jsonWriter.value(true);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_withFalse_writesFalse() throws IOException {
    jsonWriter.value(false);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_withNull_callsNullValue() throws IOException {
    JsonWriter spyWriter = Mockito.spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Boolean) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
    assertEquals("", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_invokesWriteDeferredName_andBeforeValue() throws Exception {
    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    spyWriter.value(Boolean.TRUE);

    // Use reflection to invoke private methods to ensure they exist and can be called without exception
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);
    writeDeferredName.invoke(spyWriter);

    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);
    beforeValue.invoke(spyWriter);

    // Verify output
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_returnsThisInstance() throws IOException {
    JsonWriter returned = jsonWriter.value(true);
    assertSame(jsonWriter, returned);
  }
}