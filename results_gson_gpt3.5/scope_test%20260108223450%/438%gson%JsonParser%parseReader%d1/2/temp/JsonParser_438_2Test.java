package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonParseException;
import com.google.gson.JsonElement;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class JsonParser_438_2Test {

  @Test
    @Timeout(8000)
  public void parseReader_shouldReturnJsonElement_whenStreamsParseSucceeds() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    JsonElement expectedElement = mock(JsonElement.class);

    when(reader.isLenient()).thenReturn(false);
    doNothing().when(reader).setLenient(true);
    doNothing().when(reader).setLenient(false);

    // Mock static method Streams.parse using Mockito inline mock maker
    try (var mockedStreams = Mockito.mockStatic(Streams.class)) {
      mockedStreams.when(() -> Streams.parse(reader)).thenReturn(expectedElement);

      JsonElement actual = JsonParser.parseReader(reader);

      assertSame(expectedElement, actual);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader).setLenient(true);
      inOrder.verify(reader).setLenient(false);

      mockedStreams.verify(() -> Streams.parse(reader));
    }
  }

  @Test
    @Timeout(8000)
  public void parseReader_shouldThrowJsonParseException_whenStreamsParseThrowsStackOverflowError() throws Exception {
    JsonReader reader = mock(JsonReader.class);

    when(reader.isLenient()).thenReturn(true);
    doNothing().when(reader).setLenient(true);
    doNothing().when(reader).setLenient(true); // final setLenient(lenient) uses original lenient true

    try (var mockedStreams = Mockito.mockStatic(Streams.class)) {
      mockedStreams.when(() -> Streams.parse(reader)).thenThrow(new StackOverflowError("stack overflow"));

      JsonParseException ex = assertThrows(JsonParseException.class, () -> JsonParser.parseReader(reader));
      assertTrue(ex.getMessage().contains("Failed parsing JSON source"));
      assertTrue(ex.getCause() instanceof StackOverflowError);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader, times(2)).setLenient(true);

      mockedStreams.verify(() -> Streams.parse(reader));
    }
  }

  @Test
    @Timeout(8000)
  public void parseReader_shouldThrowJsonParseException_whenStreamsParseThrowsOutOfMemoryError() throws Exception {
    JsonReader reader = mock(JsonReader.class);

    when(reader.isLenient()).thenReturn(false);
    doNothing().when(reader).setLenient(true);
    doNothing().when(reader).setLenient(false);

    try (var mockedStreams = Mockito.mockStatic(Streams.class)) {
      mockedStreams.when(() -> Streams.parse(reader)).thenThrow(new OutOfMemoryError("out of memory"));

      JsonParseException ex = assertThrows(JsonParseException.class, () -> JsonParser.parseReader(reader));
      assertTrue(ex.getMessage().contains("Failed parsing JSON source"));
      assertTrue(ex.getCause() instanceof OutOfMemoryError);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader).setLenient(true);
      inOrder.verify(reader).setLenient(false);

      mockedStreams.verify(() -> Streams.parse(reader));
    }
  }
}