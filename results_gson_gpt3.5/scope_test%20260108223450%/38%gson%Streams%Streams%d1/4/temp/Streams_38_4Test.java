package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.internal.bind.TypeAdapters;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

public class Streams_38_4Test {

  @Test
    @Timeout(8000)
  public void testPrivateConstructor() throws Exception {
    Constructor<Streams> constructor = Streams.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Throwable thrown = assertThrows(Throwable.class, constructor::newInstance);
    // The thrown is InvocationTargetException, whose cause is UnsupportedOperationException
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidJsonElement() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("true"));
    JsonElement element = Streams.parse(reader);
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertTrue(element.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testParse_withMalformedJsonException() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("malformed json"));
    assertThrows(JsonSyntaxException.class, () -> Streams.parse(reader));
  }

  @Test
    @Timeout(8000)
  public void testParse_withIOException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new IOException("IO error"));
    assertThrows(JsonIOException.class, () -> Streams.parse(reader));
  }

  @Test
    @Timeout(8000)
  public void testParse_withEOFException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new EOFException());
    JsonElement element = Streams.parse(reader);
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test
    @Timeout(8000)
  public void testWrite_withValidJsonElement() throws Throwable {
    // Use a real JsonElement parsed from a valid JSON string instead of mock
    JsonReader reader = new JsonReader(new StringReader("\"test\""));
    JsonElement element = Streams.parse(reader);

    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);

    Streams.write(element, writer);
    writer.flush();

    assertEquals("\"test\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testWrite_withIOException() throws Throwable {
    // Use a real JsonElement parsed from a valid JSON string instead of mock
    JsonReader reader = new JsonReader(new StringReader("\"test\""));
    JsonElement element = Streams.parse(reader);

    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = spy(new JsonWriter(stringWriter));

    // Throw IOException when writer.value(...) is called to simulate IOException during write
    doThrow(new IOException("write error")).when(writer).value(anyString());

    JsonIOException ex = assertThrows(JsonIOException.class, () -> Streams.write(element, writer));
    assertEquals("write error", ex.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  public void testWriterForAppendable_withStringBuilder() throws IOException {
    StringBuilder sb = new StringBuilder();
    Writer writer = Streams.writerForAppendable(sb);
    assertNotNull(writer);
    writer.write("test");
    writer.flush();
    assertEquals("test", sb.toString());
  }

  @Test
    @Timeout(8000)
  public void testWriterForAppendable_withWriter() throws IOException {
    StringWriter sw = new StringWriter();
    Writer writer = Streams.writerForAppendable(sw);
    assertSame(sw, writer);
  }
}