package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.EOFException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;

class Streams_38_3Test {

  @Test
    @Timeout(8000)
  void testConstructorThrowsUnsupportedOperationException() throws Exception {
    Constructor<Streams> constructor = Streams.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Throwable thrown = assertThrows(Throwable.class, constructor::newInstance);
    // The thrown exception is InvocationTargetException wrapping UnsupportedOperationException
    assertTrue(thrown instanceof java.lang.reflect.InvocationTargetException);
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  void testParse_validJsonElement() throws IOException {
    // Prepare a JsonReader that returns a simple JSON null token
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    JsonElement element = Streams.parse(reader);

    assertNotNull(element);
    assertTrue(element.isJsonNull());
    verify(reader).nextNull();
  }

  @Test
    @Timeout(8000)
  void testParse_throwsJsonSyntaxExceptionOnMalformedJson() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new MalformedJsonException("Malformed JSON"));

    JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> Streams.parse(reader));
    assertTrue(exception.getMessage().contains("Malformed JSON"));
  }

  @Test
    @Timeout(8000)
  void testParse_throwsJsonIOExceptionOnIOException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new IOException("IO failure"));

    JsonIOException exception = assertThrows(JsonIOException.class, () -> Streams.parse(reader));
    assertTrue(exception.getCause() instanceof IOException);
  }

  @Test
    @Timeout(8000)
  void testParse_throwsJsonParseExceptionOnNumberFormatException() throws IOException {
    String malformedNumberJson = "NaN";
    JsonReader realReader = new JsonReader(new StringReader(malformedNumberJson));
    realReader.setLenient(true);

    JsonParseException exception = assertThrows(JsonParseException.class, () -> Streams.parse(realReader));
    assertNotNull(exception.getCause());
    assertTrue(exception.getCause() instanceof NumberFormatException);
  }

  @Test
    @Timeout(8000)
  void testWrite_writesJsonElement() throws IOException {
    StringWriter sw = new StringWriter();
    JsonWriter writer = new JsonWriter(sw);
    JsonElement element = JsonNull.INSTANCE;

    Streams.write(element, writer);

    // No exceptions means success
  }

  @Test
    @Timeout(8000)
  void testWrite_throwsJsonIOExceptionOnIOException() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    // Mock writer.nullValue() to throw IOException wrapped in JsonIOException by Streams.write
    doThrow(new IOException("IO error")).when(writer).nullValue();
    JsonElement element = JsonNull.INSTANCE;

    JsonIOException exception = assertThrows(JsonIOException.class, () -> Streams.write(element, writer));
    assertTrue(exception.getCause() instanceof IOException);
  }

  @Test
    @Timeout(8000)
  void testWriterForAppendable_withStringBuilder() throws IOException {
    StringBuilder sb = new StringBuilder();
    Writer writer = Streams.writerForAppendable(sb);
    assertNotNull(writer);
    writer.write("test");
    writer.flush();
    assertEquals("test", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testWriterForAppendable_withWriter() throws IOException {
    StringWriter sw = new StringWriter();
    Writer writer = Streams.writerForAppendable(sw);
    assertSame(sw, writer);
  }
}