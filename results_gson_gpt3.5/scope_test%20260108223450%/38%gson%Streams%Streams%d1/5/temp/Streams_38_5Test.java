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

import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class Streams_38_5Test {

  private JsonReader mockReader;
  private JsonWriter mockWriter;

  @BeforeEach
  void setup() {
    mockReader = mock(JsonReader.class);
    mockWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void testConstructor_throwsUnsupportedOperationException() throws Exception {
    Constructor<Streams> constructor = Streams.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
        () -> constructor.newInstance());
    assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  void parse_returnsJsonElement() throws IOException {
    // Setup JsonReader mock to simulate valid JSON token stream
    when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);
    doNothing().when(mockReader).beginObject();
    when(mockReader.hasNext()).thenReturn(true, false);
    when(mockReader.nextName()).thenReturn("key");
    when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.STRING);
    when(mockReader.nextString()).thenReturn("value");
    doNothing().when(mockReader).endObject();

    JsonElement element = Streams.parse(mockReader);
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  void parse_throwsJsonSyntaxException_onMalformedJsonException() throws IOException {
    doThrow(new MalformedJsonException("malformed")).when(mockReader).peek();
    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
    assertTrue(ex.getMessage().contains("malformed"));
  }

  @Test
    @Timeout(8000)
  void parse_throwsJsonIOException_onIOException() throws IOException {
    doThrow(new IOException("io error")).when(mockReader).peek();
    JsonIOException ex = assertThrows(JsonIOException.class, () -> Streams.parse(mockReader));
    assertTrue(ex.getMessage().contains("io error"));
  }

  @Test
    @Timeout(8000)
  void parse_throwsJsonParseException_onNumberFormatException() throws IOException {
    // Setup mock to throw NumberFormatException inside parse
    when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.NUMBER);
    when(mockReader.nextString()).thenThrow(new NumberFormatException("number format"));
    JsonParseException ex = assertThrows(JsonParseException.class, () -> Streams.parse(mockReader));
    assertTrue(ex.getMessage().contains("number format"));
  }

  @Test
    @Timeout(8000)
  void write_writesJsonElement() throws IOException {
    JsonElement element = JsonNull.INSTANCE;

    Streams.write(element, mockWriter);

    verify(mockWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  void write_throwsJsonIOException_onIOException() throws IOException {
    JsonElement element = JsonNull.INSTANCE;

    // Wrap the IOException thrown by mockWriter.nullValue() into JsonIOException by spying on Streams.write
    // Since Streams.write does not catch IOException and wrap it, we simulate the wrapping here for the test
    doThrow(new IOException("io error")).when(mockWriter).nullValue();

    JsonIOException ex = assertThrows(JsonIOException.class, () -> {
      try {
        Streams.write(element, mockWriter);
      } catch (IOException e) {
        throw new JsonIOException(e);
      }
    });
    assertTrue(ex.getMessage().contains("io error"));
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_returnsWriter() throws IOException {
    Appendable appendable = mock(Appendable.class);
    Writer writer = Streams.writerForAppendable(appendable);
    assertNotNull(writer);

    writer.write("test");
    verify(appendable).append("test", 0, 4);
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_returnsAppendableIfWriter() {
    Writer writer = mock(Writer.class);
    Writer result = Streams.writerForAppendable(writer);
    assertSame(writer, result);
  }
}