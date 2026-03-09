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

class JsonWriter_388_4Test {
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
  void jsonValue_nonNull_writesValueProperly() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // Instead of stubbing private methods (not possible), we just invoke jsonValue directly.
    // We cannot verify private method calls via Mockito, so we verify the output instead.

    String testValue = "{\"test\":123}";
    JsonWriter result = spyWriter.jsonValue(testValue);

    assertEquals(testValue, stringWriter.toString());
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void writeDeferredName_and_beforeValue_invoked_viaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // We just invoke to check no exception thrown (void methods, private)
    writeDeferredName.invoke(jsonWriter);
    beforeValue.invoke(jsonWriter);
  }

  @Test
    @Timeout(8000)
  void jsonValue_appendsValueWithoutModification() throws IOException {
    String json = "123";
    jsonWriter.jsonValue(json);
    assertEquals(json, stringWriter.toString());
  }
}