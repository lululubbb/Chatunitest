package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonParser_440_5Test {

  @Test
    @Timeout(8000)
  void parse_WithValidReader_DelegatesToParseReader() throws Exception {
    // Arrange
    JsonParser parser = new JsonParser();
    Reader reader = new StringReader("{\"key\":\"value\"}");

    // Act
    JsonElement result = parser.parse(reader);

    // Assert
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void parse_WithNullReader_ThrowsNullPointerException() {
    JsonParser parser = new JsonParser();
    assertThrows(NullPointerException.class, () -> parser.parse((Reader) null));
  }

  @Test
    @Timeout(8000)
  void parse_WithMalformedJson_ThrowsJsonSyntaxException() throws IOException {
    Reader reader = new StringReader("{malformedJson:");
    JsonParser parser = new JsonParser();

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> parser.parse(reader));
    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void parse_WithIOException_ThrowsJsonIOException() throws IOException {
    Reader mockReader = mock(Reader.class);
    when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("IO error"));
    JsonParser parser = new JsonParser();

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> parser.parse(mockReader));
    assertEquals("IO error", thrown.getCause().getMessage());
  }
}