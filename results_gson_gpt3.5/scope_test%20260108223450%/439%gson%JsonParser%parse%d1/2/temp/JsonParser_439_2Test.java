package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

class JsonParser_439_2Test {

  private JsonParser jsonParser;

  @BeforeEach
  void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  void parse_withValidJsonString_shouldReturnJsonElement() {
    String json = "{\"key\":\"value\"}";

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expectedElement = mock(JsonElement.class);
      mockedStatic.when(() -> JsonParser.parseString(json)).thenReturn(expectedElement);

      JsonElement actualElement = jsonParser.parse(json);

      assertSame(expectedElement, actualElement);
      mockedStatic.verify(() -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  void parse_withInvalidJsonString_shouldThrowJsonSyntaxException() {
    String invalidJson = "{key:\"value\""; // missing closing }

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseString(invalidJson)).thenThrow(new JsonSyntaxException("Malformed JSON"));

      assertThrows(JsonSyntaxException.class, () -> jsonParser.parse(invalidJson));
      mockedStatic.verify(() -> JsonParser.parseString(invalidJson));
    }
  }
}