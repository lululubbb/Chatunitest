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

public class JsonWriter_387_3Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_withNonNullString_writesStringAndReturnsThis() throws Exception {
    // Cannot mock private methods directly, so call real method and verify result

    JsonWriter spyWriter = spy(jsonWriter);

    // Call the method under test
    JsonWriter result = spyWriter.value("testValue");

    // Verify the method returns this
    assertSame(spyWriter, result);

    // Optionally verify the output contains the string value
    String output = stringWriter.toString();
    assertTrue(output.contains("testValue"));
  }

  @Test
    @Timeout(8000)
  public void value_withNullString_callsNullValueAndReturnsResult() throws Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    JsonWriter nullValueResult = mock(JsonWriter.class);
    doReturn(nullValueResult).when(spyWriter).nullValue();

    // disambiguate call to value(null) by casting to String
    JsonWriter result = spyWriter.value((String) null);

    verify(spyWriter).nullValue();
    assertSame(nullValueResult, result);
  }

  @Test
    @Timeout(8000)
  public void value_privateMethodsAreInvoked() throws Exception {
    // Use reflection to invoke private methods to cover branches

    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);

    // invoke private methods directly - no exception means coverage
    writeDeferredNameMethod.invoke(jsonWriter);
    beforeValueMethod.invoke(jsonWriter);
    stringMethod.invoke(jsonWriter, "reflectionTest");
  }

  @Test
    @Timeout(8000)
  public void value_integrationTest_writesJsonString() throws IOException {
    // Integration test to check actual output for a simple value
    jsonWriter.beginObject();
    jsonWriter.name("key");
    jsonWriter.value("value");
    jsonWriter.endObject();
    jsonWriter.flush();

    String json = stringWriter.toString();
    assertTrue(json.contains("\"key\""));
    assertTrue(json.contains("\"value\""));
    assertTrue(json.startsWith("{"));
    assertTrue(json.endsWith("}"));
  }
}