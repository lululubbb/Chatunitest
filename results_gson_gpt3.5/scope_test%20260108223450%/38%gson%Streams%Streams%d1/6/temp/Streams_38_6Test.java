package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Streams_38_6Test {

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Constructor<Streams> constructor = Streams.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertNotNull(thrown);
    assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  void parse_validJson_returnsJsonElement() throws IOException {
    String json = "\"hello\"";
    JsonReader reader = new JsonReader(new StringReader(json));
    JsonElement element = Streams.parse(reader);
    assertNotNull(element);
    assertEquals("hello", element.getAsString());
  }

  @Test
    @Timeout(8000)
  void parse_emptyJson_throwsJsonSyntaxException() {
    JsonReader reader = new JsonReader(new StringReader(""));
    // Advance the reader to trigger EOFException inside parse, which should be wrapped as JsonSyntaxException
    assertThrows(JsonSyntaxException.class, () -> {
      Streams.parse(reader);
    });
  }

  @Test
    @Timeout(8000)
  void parse_malformedJson_throwsJsonSyntaxException() {
    JsonReader reader = new JsonReader(new StringReader("{ this is not json }"));
    assertThrows(JsonSyntaxException.class, () -> Streams.parse(reader));
  }

  @Test
    @Timeout(8000)
  void parse_ioException_throwsJsonIOException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new IOException("io error"));
    assertThrows(JsonIOException.class, () -> Streams.parse(reader));
  }

  @Test
    @Timeout(8000)
  void write_writesJsonElement() throws IOException {
    JsonElement element = TypeAdapters.JSON_ELEMENT.read(new JsonReader(new StringReader("\"test\"")));
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    Streams.write(element, writer);
    writer.flush();
    assertEquals("\"test\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void write_nullElement_writesNull() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    Streams.write(JsonNull.INSTANCE, writer);
    writer.flush();
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_withStringBuilder_returnsWriter() throws IOException {
    StringBuilder sb = new StringBuilder();
    Writer writer = Streams.writerForAppendable(sb);
    assertNotNull(writer);
    writer.write("abc");
    writer.flush();
    assertEquals("abc", sb.toString());
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_withWriter_returnsSameInstance() throws IOException {
    StringWriter sw = new StringWriter();
    Writer writer = Streams.writerForAppendable(sw);
    assertSame(sw, writer);
  }
}