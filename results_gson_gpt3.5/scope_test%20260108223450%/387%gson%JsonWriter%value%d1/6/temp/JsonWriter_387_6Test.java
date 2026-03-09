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
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_null_callsNullValue() throws IOException {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter returned = spyWriter.value((String) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  void value_nonNull_executesExpectedFlow() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    JsonWriter spyWriter = spy(jsonWriter);

    // Use reflection to invoke private methods writeDeferredName() and beforeValue()
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    writeDeferredNameMethod.invoke(spyWriter);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
    beforeValueMethod.invoke(spyWriter);

    // Use reflection to invoke private string(String) method to ensure no exceptions
    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);

    JsonWriter returned = spyWriter.value("testValue");

    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  void value_emptyString() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    JsonWriter spyWriter = spy(jsonWriter);

    // Invoke private methods directly via reflection
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    writeDeferredNameMethod.invoke(spyWriter);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
    beforeValueMethod.invoke(spyWriter);

    JsonWriter returned = spyWriter.value("");

    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  void value_specialCharactersString() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    String special = "\"\\\b\f\n\r\t\u2028\u2029";
    JsonWriter spyWriter = spy(jsonWriter);

    // Invoke private methods directly via reflection
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    writeDeferredNameMethod.invoke(spyWriter);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
    beforeValueMethod.invoke(spyWriter);

    JsonWriter returned = spyWriter.value(special);

    assertSame(spyWriter, returned);
  }
}