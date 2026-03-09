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

class JsonWriter_388_5Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void jsonValue_null_returnsNullValueAndAppendsNull() throws IOException {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.jsonValue(null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void jsonValue_nonNull_callsWriteDeferredNameBeforeValueAndAppends() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    // Initialize the JsonWriter state with beginArray() to allow one top-level value
    spyWriter.beginArray();

    String testValue = "{\"key\":\"value\"}";
    JsonWriter result = spyWriter.jsonValue(testValue);

    // end the array to flush the structure and allow output
    spyWriter.endArray();

    // Verify output string contains the test value inside array brackets
    assertEquals("[" + testValue + "]", stringWriter.toString());
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void invokePrivateBeforeValueMethodViaReflection() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // invoke to ensure no exceptions
    beforeValue.invoke(jsonWriter);
  }

  @Test
    @Timeout(8000)
  void invokePrivateWriteDeferredNameMethodViaReflection() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);

    // invoke to ensure no exceptions
    writeDeferredName.invoke(jsonWriter);
  }
}