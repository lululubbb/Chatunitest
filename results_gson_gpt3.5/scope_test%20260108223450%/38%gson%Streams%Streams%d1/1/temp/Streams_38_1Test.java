package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Streams_38_1Test {

  private JsonReader mockReader;
  private JsonWriter mockWriter;

  @BeforeEach
  void setUp() {
    mockReader = mock(JsonReader.class);
    mockWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_throwsUnsupportedOperationException() throws Exception {
    Constructor<Streams> constructor = Streams.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance();
    });
    assertTrue(ex.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  void parse_returnsJsonNull_whenEOFException() throws IOException {
    when(mockReader.peek()).thenThrow(new EOFException());
    JsonElement result = Streams.parse(mockReader);
    assertSame(JsonNull.INSTANCE, result);
  }

  @Test
    @Timeout(8000)
  void parse_throwsJsonParseException_whenMalformedJsonException() throws IOException {
    when(mockReader.peek()).thenThrow(new MalformedJsonException("malformed"));
    JsonParseException ex = assertThrows(JsonParseException.class, () -> Streams.parse(mockReader));
    assertTrue(ex.getMessage().contains("malformed"));
  }

  @Test
    @Timeout(8000)
  void parse_throwsJsonIOException_whenIOException() throws IOException {
    when(mockReader.peek()).thenThrow(new IOException("io error"));
    JsonIOException ex = assertThrows(JsonIOException.class, () -> Streams.parse(mockReader));
    assertTrue(ex.getMessage().contains("io error"));
  }

  @Test
    @Timeout(8000)
  void parse_throwsRuntimeException_whenRuntimeException() throws IOException {
    when(mockReader.peek()).thenThrow(new RuntimeException("runtime"));
    RuntimeException ex = assertThrows(RuntimeException.class, () -> Streams.parse(mockReader));
    assertTrue(ex.getMessage().contains("runtime"));
  }

  @Test
    @Timeout(8000)
  void write_callsTypeAdaptersWrite() throws IOException {
    JsonElement element = JsonNull.INSTANCE;

    // Use a real JsonWriter with a dummy Writer.
    java.io.StringWriter stringWriter = new java.io.StringWriter();
    JsonWriter realWriter = new JsonWriter(stringWriter);
    JsonWriter spyWriter = spy(realWriter);

    Streams.write(element, spyWriter);

    verify(spyWriter).nullValue();
    verify(spyWriter).flush();
    // Optionally verify output contains "null"
    assertTrue(stringWriter.toString().contains("null"));
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_returnsWriter_forAppendable() throws IOException {
    Appendable appendable = new StringBuilder();
    Writer writer = Streams.writerForAppendable(appendable);
    assertNotNull(writer);
    // The returned writer is a Streams.AppendableWriter wrapping the appendable,
    // so it is not equal to appendable itself.
    // Instead, verify it writes correctly:
    writer.write("test");
    writer.flush();
    assertEquals("test", appendable.toString());
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_returnsWriter_forWriter() throws IOException {
    Writer writer = mock(Writer.class);
    Writer result = Streams.writerForAppendable(writer);
    assertSame(writer, result);
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_throwsNullPointerException_forNull() {
    assertThrows(NullPointerException.class, () -> Streams.writerForAppendable((Appendable) null));
  }
}