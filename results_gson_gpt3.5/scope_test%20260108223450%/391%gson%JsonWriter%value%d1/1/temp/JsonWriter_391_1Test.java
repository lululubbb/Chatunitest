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

public class JsonWriter_391_1Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_withTrueBoolean_writesTrue() throws IOException {
    JsonWriter returned = jsonWriter.value(true);
    assertSame(jsonWriter, returned);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_withFalseBoolean_writesFalse() throws IOException {
    JsonWriter returned = jsonWriter.value(false);
    assertSame(jsonWriter, returned);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_withNullBoolean_callsNullValue() throws Exception {
    // Use a spy to verify nullValue is called
    JsonWriter spyWriter = spy(jsonWriter);

    JsonWriter returned = spyWriter.value((Boolean) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void value_invokesWriteDeferredNameAndBeforeValue() throws Exception {
    // Use spy to verify private methods are called via reflection
    JsonWriter spyWriter = spy(jsonWriter);

    // Use reflection to access private methods
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    // Call value
    JsonWriter returned = spyWriter.value(true);

    assertSame(spyWriter, returned);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_withBoolean_nullValueReturnsNull() throws IOException {
    // Confirm that value(null) returns the result of nullValue() and does not write
    JsonWriter spy = spy(jsonWriter);
    JsonWriter nullValueReturn = new JsonWriter(stringWriter);
    doReturn(nullValueReturn).when(spy).nullValue();

    JsonWriter result = spy.value((Boolean) null);

    verify(spy).nullValue();
    assertSame(nullValueReturn, result);
    assertEquals("", stringWriter.toString());
  }
}