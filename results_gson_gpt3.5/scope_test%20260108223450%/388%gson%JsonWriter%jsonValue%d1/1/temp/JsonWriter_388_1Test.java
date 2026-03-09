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

class JsonWriter_388_1Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void jsonValue_null_callsNullValue() throws IOException {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.jsonValue(null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void jsonValue_nonNull_writesValue() throws Throwable {
    String testValue = "\"test\"";

    JsonWriter spyWriter = spy(jsonWriter);

    // Since private methods cannot be stubbed directly, remove stubbing attempts.
    // Just invoke normally and verify output.

    JsonWriter result = spyWriter.jsonValue(testValue);

    assertEquals(testValue, stringWriter.toString());
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void jsonValue_nonNull_actualWriteDeferredNameAndBeforeValue() throws IOException {
    // This test covers actual private methods indirectly by not mocking them.
    // We test that jsonValue appends the string to the writer.

    String testValue = "\"hello\"";

    JsonWriter writer = new JsonWriter(stringWriter);

    JsonWriter result = writer.jsonValue(testValue);

    assertEquals(testValue, stringWriter.toString());
    assertSame(writer, result);
  }

  @Test
    @Timeout(8000)
  void jsonValue_reflectionInvoke() throws Throwable {
    String testValue = "\"reflect\"";

    Method jsonValueMethod = JsonWriter.class.getDeclaredMethod("jsonValue", String.class);
    jsonValueMethod.setAccessible(true);

    Object result = jsonValueMethod.invoke(jsonWriter, testValue);

    assertSame(jsonWriter, result);
    assertEquals(testValue, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void jsonValue_reflectionInvoke_null() throws Throwable {
    Method jsonValueMethod = JsonWriter.class.getDeclaredMethod("jsonValue", String.class);
    jsonValueMethod.setAccessible(true);

    Object result = jsonValueMethod.invoke(jsonWriter, new Object[] {null});

    assertNotNull(result);
    assertTrue(result instanceof JsonWriter);
  }
}