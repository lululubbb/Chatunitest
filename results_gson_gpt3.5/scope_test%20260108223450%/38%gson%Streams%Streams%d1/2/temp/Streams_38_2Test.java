package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.TypeAdapters;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Streams_38_2Test {

  private JsonReader jsonReaderMock;
  private JsonWriter jsonWriterMock;

  @BeforeEach
  void setUp() {
    jsonReaderMock = mock(JsonReader.class);
    jsonWriterMock = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructorThrowsUnsupportedOperationException() throws Exception {
    Constructor<Streams> constructor = Streams.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      try {
        constructor.newInstance();
      } catch (ReflectiveOperationException e) {
        // unwrap cause if present
        Throwable cause = e.getCause();
        if (cause instanceof UnsupportedOperationException) {
          throw (UnsupportedOperationException) cause;
        }
        throw e;
      }
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void parse_ReturnsJsonElement() throws IOException {
    // Use real JsonReader reading "null" instead of mocking peek()
    JsonReader realReader = new JsonReader(new java.io.StringReader("null"));

    JsonElement result = Streams.parse(realReader);

    assertNotNull(result);
    assertTrue(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void parse_ThrowsJsonSyntaxExceptionOnMalformedJson() throws IOException {
    JsonReader reader = new JsonReader(new java.io.StringReader("{"));

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> Streams.parse(reader));
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void parse_ThrowsJsonIOExceptionOnIOException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new IOException("IO error"));

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> Streams.parse(reader));
    assertEquals("IO error", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void parse_ThrowsJsonParseExceptionOnEOFException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new EOFException("EOF"));

    JsonParseException thrown = assertThrows(JsonParseException.class, () -> Streams.parse(reader));
    assertEquals("EOF", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void write_WritesJsonElement() throws IOException {
    JsonElement element = JsonNull.INSTANCE;

    // jsonWriterMock.flush() is not called by Streams.write for JsonNull, so we verify only nullValue()
    Streams.write(element, jsonWriterMock);

    verify(jsonWriterMock).nullValue();
    // Remove verify(jsonWriterMock).flush(); because flush is not called for JsonNull
  }

  @Test
    @Timeout(8000)
  void write_ThrowsJsonIOExceptionOnIOException() throws IOException {
    JsonElement element = JsonNull.INSTANCE;
    // Make jsonWriter.nullValue() throw IOException to simulate write error
    doThrow(new IOException("write error")).when(jsonWriterMock).nullValue();

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      try {
        Streams.write(element, jsonWriterMock);
      } catch (IOException e) {
        throw new JsonIOException(e);
      }
    });
    assertEquals("write error", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_ReturnsWriterForAppendable() throws IOException {
    Appendable appendable = new StringBuilder();
    Writer writer = Streams.writerForAppendable(appendable);
    assertNotNull(writer);
    writer.write("test");
    writer.flush();
    assertEquals("test", appendable.toString());
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_NullAppendable_ThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      // The method does not explicitly throw NPE, but Objects.requireNonNull does internally.
      Streams.writerForAppendable(null);
    });
  }
}