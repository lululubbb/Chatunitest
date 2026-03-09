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
  void value_withNull_callsNullValue_and_returnsThis() throws IOException {
    JsonWriter spyWriter = Mockito.spy(jsonWriter);
    // stub nullValue to return spyWriter itself
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Boolean) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
    assertEquals("", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_deferredNameIsWritten_beforeValueCalled_andValueWritten() throws Exception {
    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    // Use reflection to set deferredName non-null to force writeDeferredName to write something
    java.lang.reflect.Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(spyWriter, "name");

    // Use reflection to invoke private writeDeferredName() and beforeValue() methods instead of stubbing
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    // We also need to push a context on stack because beforeValue uses peek and stackSize
    java.lang.reflect.Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyWriter, 1);

    java.lang.reflect.Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stackArray = (int[]) stackField.get(spyWriter);
    stackArray[0] = JsonScope.EMPTY_OBJECT; // typical initial state

    // Instead of stubbing private methods, invoke them explicitly before calling value
    // So spyWriter.value(true) will call them normally, but we verify calls by spying on the public method only.
    // Since we cannot verify private method calls, we check output and behavior instead.

    // Call value(true) normally
    JsonWriter result = spyWriter.value(true);

    // Verify output contains the name and the value true
    String output = stringWriter.toString();
    assertTrue(output.contains("true"));
    assertTrue(output.contains("name"));

    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void value_withDeferredNameNull_writesValueDirectly() throws IOException {
    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    // deferredName is null by default

    // Call value(false) normally
    JsonWriter result = spyWriter.value(false);

    assertSame(spyWriter, result);
    assertEquals("false", stringWriter.toString());
  }
}