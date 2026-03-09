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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_387_5Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_null_callsNullValue() throws IOException {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((String) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void value_nonNull_callsDeferredName_beforeValue_and_string() throws IOException, Exception {
    JsonWriter spyWriter = spy(jsonWriter);

    // Begin an object to set correct state before calling name()
    spyWriter.beginObject();

    // Prepare deferredName to test writeDeferredName invocation effect
    spyWriter.name("aName");

    // Use reflection to access private fields and verify after call
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);

    // Call real method for value(String)
    doCallRealMethod().when(spyWriter).value(anyString());

    // Call value with non-null string
    JsonWriter result = spyWriter.value("testValue");

    // Check deferredName field is null (means writeDeferredName was called)
    assertNull(deferredNameField.get(spyWriter));

    // Check output contains the string value with quotes
    String output = stringWriter.toString();
    assertTrue(output.contains("\"testValue\""));

    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void value_emptyString_writesEmptyQuotes() throws IOException {
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.value("");
    String output = stringWriter.toString();
    assertTrue(output.contains("\"\""));
  }

  @Test
    @Timeout(8000)
  public void value_multipleCalls_writesMultipleValues() throws IOException {
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginArray();
    writer.value("one");
    writer.value("two");
    writer.endArray();

    String output = stringWriter.toString();
    assertEquals("[\"one\",\"two\"]", output);
  }

  @Test
    @Timeout(8000)
  public void value_withDeferredName_writesNameAndValue() throws IOException {
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginObject();
    writer.name("key");
    writer.value("val");
    writer.endObject();

    String output = stringWriter.toString();
    assertEquals("{\"key\":\"val\"}", output);
  }
}