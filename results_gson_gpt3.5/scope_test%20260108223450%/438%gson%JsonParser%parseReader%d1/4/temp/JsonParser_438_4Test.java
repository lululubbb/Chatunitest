package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class JsonParser_438_4Test {

  @Test
    @Timeout(8000)
  void parseReader_shouldReturnParsedJsonElement_andRestoreLenient() throws Exception {
    // Arrange
    JsonReader reader = mock(JsonReader.class);
    JsonElement expectedElement = mock(JsonElement.class);

    when(reader.isLenient()).thenReturn(false);
    // We want to verify that setLenient(true) is called before Streams.parse,
    // and setLenient(false) is called after.

    // Mock Streams.parse to return expectedElement
    // Streams.parse is static, so use Mockito.mockStatic
    try (var streamsMock = mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(reader)).thenReturn(expectedElement);

      // Act
      JsonElement actual = JsonParser.parseReader(reader);

      // Assert
      assertSame(expectedElement, actual);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader).setLenient(true);
      streamsMock.verify(() -> Streams.parse(reader));
      inOrder.verify(reader).setLenient(false);
    }
  }

  @Test
    @Timeout(8000)
  void parseReader_shouldThrowJsonParseExceptionOnStackOverflowError_andRestoreLenient() {
    JsonReader reader = mock(JsonReader.class);

    when(reader.isLenient()).thenReturn(true);

    try (var streamsMock = mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(reader)).thenThrow(new StackOverflowError("stack overflow"));

      JsonParseException ex = assertThrows(JsonParseException.class, () -> JsonParser.parseReader(reader));
      assertTrue(ex.getMessage().contains("Failed parsing JSON source"));
      assertTrue(ex.getCause() instanceof StackOverflowError);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader).setLenient(true);
      streamsMock.verify(() -> Streams.parse(reader));
      inOrder.verify(reader).setLenient(true);
    }
  }

  @Test
    @Timeout(8000)
  void parseReader_shouldThrowJsonParseExceptionOnOutOfMemoryError_andRestoreLenient() {
    JsonReader reader = mock(JsonReader.class);

    when(reader.isLenient()).thenReturn(true);

    try (var streamsMock = mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(reader)).thenThrow(new OutOfMemoryError("out of memory"));

      JsonParseException ex = assertThrows(JsonParseException.class, () -> JsonParser.parseReader(reader));
      assertTrue(ex.getMessage().contains("Failed parsing JSON source"));
      assertTrue(ex.getCause() instanceof OutOfMemoryError);

      InOrder inOrder = inOrder(reader);
      inOrder.verify(reader).isLenient();
      inOrder.verify(reader).setLenient(true);
      streamsMock.verify(() -> Streams.parse(reader));
      inOrder.verify(reader).setLenient(true);
    }
  }
}