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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class JsonParser_438_3Test {

  private JsonReader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  public void parseReader_shouldReturnParsedJsonElement_whenStreamsParseSucceeds() throws Exception {
    // Arrange
    when(mockReader.isLenient()).thenReturn(false);
    JsonElement expectedElement = mock(JsonElement.class);

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(mockReader)).thenReturn(expectedElement);

      // Act
      JsonElement actualElement = JsonParser.parseReader(mockReader);

      // Assert
      assertSame(expectedElement, actualElement);
      verify(mockReader).isLenient();
      verify(mockReader).setLenient(true);
      verify(mockReader).setLenient(false);
    }
  }

  @Test
    @Timeout(8000)
  public void parseReader_shouldRestoreLenientFlagAfterParsing() throws Exception {
    // Arrange
    when(mockReader.isLenient()).thenReturn(true);
    JsonElement expectedElement = mock(JsonElement.class);

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(mockReader)).thenReturn(expectedElement);

      // Act
      JsonParser.parseReader(mockReader);

      // Assert
      verify(mockReader).isLenient();
      verify(mockReader).setLenient(true);
      verify(mockReader).setLenient(true); // called twice: before parsing and restored after parsing
    }
  }

  @Test
    @Timeout(8000)
  public void parseReader_shouldThrowJsonParseExceptionOnStackOverflowError() throws Exception {
    // Arrange
    when(mockReader.isLenient()).thenReturn(false);
    StackOverflowError error = new StackOverflowError("stack overflow");

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(mockReader)).thenThrow(error);

      // Act & Assert
      JsonParseException thrown = assertThrows(JsonParseException.class, () -> {
        JsonParser.parseReader(mockReader);
      });
      assertTrue(thrown.getMessage().contains("Failed parsing JSON source"));
      assertSame(error, thrown.getCause());
      verify(mockReader).setLenient(true);
      verify(mockReader).setLenient(false);
    }
  }

  @Test
    @Timeout(8000)
  public void parseReader_shouldThrowJsonParseExceptionOnOutOfMemoryError() throws Exception {
    // Arrange
    when(mockReader.isLenient()).thenReturn(false);
    OutOfMemoryError error = new OutOfMemoryError("out of memory");

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(mockReader)).thenThrow(error);

      // Act & Assert
      JsonParseException thrown = assertThrows(JsonParseException.class, () -> {
        JsonParser.parseReader(mockReader);
      });
      assertTrue(thrown.getMessage().contains("Failed parsing JSON source"));
      assertSame(error, thrown.getCause());
      verify(mockReader).setLenient(true);
      verify(mockReader).setLenient(false);
    }
  }
}