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

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class JsonParser_438_6Test {

  @Test
    @Timeout(8000)
  void parseReader_returnsParsedJsonElement_andResetsLenient() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    JsonElement expectedElement = mock(JsonElement.class);

    when(reader.isLenient()).thenReturn(false);

    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(reader)).thenReturn(expectedElement);

      JsonElement result = JsonParser.parseReader(reader);

      assertSame(expectedElement, result);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader).setLenient(true);
      inOrder.verify(reader).setLenient(false);

      streamsStatic.verify(() -> Streams.parse(reader));
    }
  }

  @Test
    @Timeout(8000)
  void parseReader_stackOverflowError_throwsJsonParseException() throws Exception {
    JsonReader reader = mock(JsonReader.class);

    when(reader.isLenient()).thenReturn(true);

    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(reader)).thenThrow(new StackOverflowError("stack overflow"));

      JsonParseException thrown = assertThrows(JsonParseException.class, () -> JsonParser.parseReader(reader));
      assertTrue(thrown.getMessage().contains("Failed parsing JSON source"));
      assertNotNull(thrown.getCause());
      assertTrue(thrown.getCause() instanceof StackOverflowError);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader).setLenient(true);
      inOrder.verify(reader).setLenient(true);
    }
  }

  @Test
    @Timeout(8000)
  void parseReader_outOfMemoryError_throwsJsonParseException() throws Exception {
    JsonReader reader = mock(JsonReader.class);

    when(reader.isLenient()).thenReturn(true);

    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(reader)).thenThrow(new OutOfMemoryError("out of memory"));

      JsonParseException thrown = assertThrows(JsonParseException.class, () -> JsonParser.parseReader(reader));
      assertTrue(thrown.getMessage().contains("Failed parsing JSON source"));
      assertNotNull(thrown.getCause());
      assertTrue(thrown.getCause() instanceof OutOfMemoryError);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader).setLenient(true);
      inOrder.verify(reader).setLenient(true);
    }
  }
}