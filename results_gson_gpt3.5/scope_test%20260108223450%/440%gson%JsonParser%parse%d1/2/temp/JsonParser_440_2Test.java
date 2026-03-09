package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

class JsonParser_440_2Test {

  @Test
    @Timeout(8000)
  void parse_withValidReader_returnsJsonElement() throws IOException {
    // Prepare a Reader with valid JSON string
    String jsonString = "{\"key\":\"value\"}";
    Reader reader = new StringReader(jsonString);

    JsonElement result = new JsonParser().parse(reader);

    assertNotNull(result);
    // Since we don't have JsonElement details, just check it is not null
  }

  @Test
    @Timeout(8000)
  void parse_withNullReader_throwsNullPointerException() {
    JsonParser parser = new JsonParser();
    assertThrows(NullPointerException.class, () -> parser.parse((Reader) null));
  }

  @Test
    @Timeout(8000)
  void parse_withMalformedJson_throwsJsonSyntaxException() {
    String malformedJson = "{key: 'value'"; // Missing closing brace
    Reader reader = new StringReader(malformedJson);
    JsonParser parser = new JsonParser();

    assertThrows(JsonSyntaxException.class, () -> parser.parse(reader));
  }

  @Test
    @Timeout(8000)
  void parse_withIOException_throwsJsonIOException() throws IOException {
    Reader reader = mock(Reader.class);
    when(reader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("IO error"));

    JsonParser parser = new JsonParser();

    assertThrows(JsonIOException.class, () -> parser.parse(reader));
  }

  @Test
    @Timeout(8000)
  void parse_withEmptyJson_returnsJsonNull() {
    Reader reader = new StringReader("");
    JsonParser parser = new JsonParser();

    JsonElement element = parser.parse(reader);
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }
}