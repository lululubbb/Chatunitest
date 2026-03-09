package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

class JsonParser_437_5Test {

  @Test
    @Timeout(8000)
  void parseReader_validJson_consumesEntireDocument() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonElement element = JsonParser.parseReader(reader);

    assertNotNull(element);
    assertFalse(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void parseReader_nonNullElementButNotEndDocument_throwsJsonSyntaxException() throws Exception {
    Reader reader = mock(Reader.class);
    JsonReader jsonReader = mock(JsonReader.class);
    JsonElement element = mock(JsonElement.class);

    try (MockedStatic<JsonParser> parserStatic = Mockito.mockStatic(JsonParser.class)) {
      parserStatic.when(() -> JsonParser.parseReader(jsonReader)).thenReturn(element);
      when(element.isJsonNull()).thenReturn(false);
      when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

      try (var jsonReaderConstructor = Mockito.mockConstruction(JsonReader.class,
          (mock, context) -> {
            // do nothing, we will override static parseReader(JsonReader) instead
          })) {
        Executable executable = () -> JsonParser.parseReader(reader);
        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, executable);
        assertEquals("Did not consume the entire document.", ex.getMessage());
      }
    }
  }

  @Test
    @Timeout(8000)
  void parseReader_malformedJsonExceptionThrown_throwsJsonSyntaxException() throws Exception {
    Reader reader = mock(Reader.class);
    try (MockedStatic<JsonReader> jsonReaderStatic = Mockito.mockStatic(JsonReader.class)) {
      // Correct mocking of constructor throwing checked exception using thenAnswer
      jsonReaderStatic.when(() -> new JsonReader(reader)).thenAnswer(invocation -> {
        throw new MalformedJsonException("malformed");
      });

      JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
      assertTrue(ex.getCause() instanceof MalformedJsonException);
      assertEquals("malformed", ex.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void parseReader_ioExceptionThrown_throwsJsonIOException() throws Exception {
    Reader reader = mock(Reader.class);
    try (MockedStatic<JsonReader> jsonReaderStatic = Mockito.mockStatic(JsonReader.class)) {
      // Correct mocking of constructor throwing checked exception using thenAnswer
      jsonReaderStatic.when(() -> new JsonReader(reader)).thenAnswer(invocation -> {
        throw new IOException("io error");
      });

      JsonIOException ex = assertThrows(JsonIOException.class, () -> JsonParser.parseReader(reader));
      assertTrue(ex.getCause() instanceof IOException);
      assertEquals("io error", ex.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void parseReader_numberFormatExceptionThrown_throwsJsonSyntaxException() throws Exception {
    Reader reader = mock(Reader.class);
    JsonReader jsonReader = mock(JsonReader.class);

    try (MockedStatic<JsonParser> parserStatic = Mockito.mockStatic(JsonParser.class)) {
      parserStatic.when(() -> JsonParser.parseReader(jsonReader)).thenThrow(new NumberFormatException("number format"));

      try (var jsonReaderConstructor = Mockito.mockConstruction(JsonReader.class,
          (mock, context) -> {
            // do nothing
          })) {
        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
        assertTrue(ex.getCause() instanceof NumberFormatException);
        assertEquals("number format", ex.getCause().getMessage());
      }
    }
  }
}