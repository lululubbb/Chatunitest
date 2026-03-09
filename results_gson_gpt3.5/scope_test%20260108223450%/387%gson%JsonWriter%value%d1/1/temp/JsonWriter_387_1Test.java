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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_value_Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_null_callsNullValueAndReturnsJsonWriter() throws IOException {
    JsonWriter spyWriter = spy(jsonWriter);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter returned = spyWriter.value((String) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void value_nonNull_writesString() throws IOException {
    JsonWriter writer = new JsonWriter(stringWriter);

    JsonWriter returned = writer.value("testString");

    String output = stringWriter.toString();
    assertTrue(output.contains("\"testString\""));

    assertSame(writer, returned);
  }

  @Test
    @Timeout(8000)
  public void value_emptyString_callsString() throws IOException {
    JsonWriter writer = new JsonWriter(stringWriter);

    JsonWriter returned = writer.value("");

    String output = stringWriter.toString();
    assertTrue(output.contains("\"\"")); // empty string serialized

    assertSame(writer, returned);
  }

  @Test
    @Timeout(8000)
  public void value_stringWithSpecialCharacters_callsString() throws IOException {
    JsonWriter writer = new JsonWriter(stringWriter);

    String special = "line\nbreak\tand\"quotes\\slash";

    JsonWriter returned = writer.value(special);

    String output = stringWriter.toString();
    assertTrue(output.contains("line"));
    assertTrue(output.contains("break"));
    assertTrue(output.contains("quotes"));
    assertTrue(output.contains("slash"));

    assertSame(writer, returned);
  }
}