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

public class JsonWriter_387_4Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testValue_withNonNullString_invokesExpectedMethodsAndReturnsThis() throws Exception {
    // Arrange
    String testValue = "testString";

    // Spy on jsonWriter to verify calls
    JsonWriter spyWriter = spy(jsonWriter);

    // Begin a JSON array to allow multiple values (avoids IllegalStateException)
    spyWriter.beginArray();

    // Act
    JsonWriter returned = spyWriter.value(testValue);

    // End the array to complete the JSON structure
    spyWriter.endArray();

    // Assert
    assertSame(spyWriter, returned);

    // We cannot verify private method calls directly.
    // Instead, verify that the JSON output contains the test value.
    spyWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains(testValue));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNullString_callsNullValueAndReturnsResult() throws IOException {
    JsonWriter spyWriter = spy(jsonWriter);

    // Begin a JSON array to allow multiple values (avoids IllegalStateException)
    spyWriter.beginArray();

    // Stub nullValue to verify it is called
    doReturn(spyWriter).when(spyWriter).nullValue();

    // Explicitly call value((String) null) to avoid ambiguity
    JsonWriter returned = spyWriter.value((String) null);

    // End the array to complete the JSON structure
    spyWriter.endArray();

    assertSame(spyWriter, returned);
    verify(spyWriter, times(1)).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testValue_multipleCalls_producesExpectedOutput() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value("one");
    jsonWriter.value("two");
    jsonWriter.endArray();
    jsonWriter.flush();

    String output = stringWriter.toString();
    assertNotNull(output);
    assertTrue(output.contains("one"));
    assertTrue(output.contains("two"));
  }
}