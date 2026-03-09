package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Streams_39_1Test {

  JsonReader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void parse_shouldReturnJsonElement_whenReaderHasValidJson() throws Exception {
    // Arrange
    when(mockReader.peek()).thenReturn(null); // peek() just returns JsonToken, null is fine for test
    JsonElement expected = mock(JsonElement.class);

    try (MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {
      typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader)).thenReturn(expected);

      // Act
      JsonElement actual = Streams.parse(mockReader);

      // Assert
      assertSame(expected, actual);
      verify(mockReader).peek();
      typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
    }
  }

  @Test
    @Timeout(8000)
  void parse_shouldReturnJsonNull_whenReaderIsEmptyAndEOFExceptionThrown() throws Exception {
    // Arrange
    doThrow(new EOFException()).when(mockReader).peek();

    // Act
    JsonElement result = Streams.parse(mockReader);

    // Assert
    assertSame(JsonNull.INSTANCE, result);
    verify(mockReader).peek();
  }

  @Test
    @Timeout(8000)
  void parse_shouldThrowJsonSyntaxException_whenEOFExceptionThrownAfterPeek() throws Exception {
    // Arrange
    when(mockReader.peek()).thenReturn(null);
    try (MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {
      typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader)).thenThrow(new EOFException());

      // Act & Assert
      JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
      assertTrue(ex.getCause() instanceof EOFException);
      verify(mockReader).peek();
      typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
    }
  }

  @Test
    @Timeout(8000)
  void parse_shouldThrowJsonSyntaxException_whenMalformedJsonExceptionThrown() throws Exception {
    // Arrange
    when(mockReader.peek()).thenReturn(null);
    try (MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {
      typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader))
          .thenThrow(new MalformedJsonException("malformed"));

      // Act & Assert
      JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
      assertTrue(ex.getCause() instanceof MalformedJsonException);
      verify(mockReader).peek();
      typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
    }
  }

  @Test
    @Timeout(8000)
  void parse_shouldThrowJsonIOException_whenIOExceptionThrown() throws Exception {
    // Arrange
    when(mockReader.peek()).thenReturn(null);
    try (MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {
      typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader))
          .thenThrow(new IOException("io"));

      // Act & Assert
      JsonIOException ex = assertThrows(JsonIOException.class, () -> Streams.parse(mockReader));
      assertTrue(ex.getCause() instanceof IOException);
      verify(mockReader).peek();
      typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
    }
  }

  @Test
    @Timeout(8000)
  void parse_shouldThrowJsonSyntaxException_whenNumberFormatExceptionThrown() throws Exception {
    // Arrange
    when(mockReader.peek()).thenReturn(null);
    try (MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {
      typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader))
          .thenThrow(new NumberFormatException("number format"));

      // Act & Assert
      JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
      assertTrue(ex.getCause() instanceof NumberFormatException);
      verify(mockReader).peek();
      typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
    }
  }
}