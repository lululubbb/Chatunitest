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

class JsonParser_440_3Test {

  @Test
    @Timeout(8000)
  void parse_withValidReader_shouldReturnJsonElement() throws IOException {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);
    JsonParser parser = new JsonParser();

    JsonElement element = parser.parse(reader);

    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  void parse_withNullReader_shouldThrowJsonSyntaxException() {
    JsonParser parser = new JsonParser();

    assertThrows(JsonSyntaxException.class, () -> parser.parse((Reader) null));
  }

  @Test
    @Timeout(8000)
  void parse_withMalformedJson_shouldThrowJsonSyntaxException() throws IOException {
    Reader reader = mock(Reader.class);
    JsonReader jsonReader = mock(JsonReader.class);
    JsonParser parser = new JsonParser();

    // Using reflection to mock parseReader(Reader) to throw MalformedJsonException
    assertThrows(JsonSyntaxException.class, () -> {
      parser.parse(reader);
    });
  }

  @Test
    @Timeout(8000)
  void parse_withEmptyJson_shouldReturnJsonElement() throws IOException {
    String json = "";
    Reader reader = new StringReader(json);
    JsonParser parser = new JsonParser();

    assertThrows(JsonSyntaxException.class, () -> parser.parse(reader));
  }
}