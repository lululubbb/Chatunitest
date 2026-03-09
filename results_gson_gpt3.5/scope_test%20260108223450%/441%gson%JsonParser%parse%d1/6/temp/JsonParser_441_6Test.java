package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonParser_441_6Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidJsonReader_shouldReturnJsonElement() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    JsonElement result = jsonParser.parse(jsonReader);
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals("value", result.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testParse_withMalformedJson_shouldThrowJsonSyntaxException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{key:}"));
    assertThrows(JsonSyntaxException.class, () -> {
      jsonParser.parse(jsonReader);
    });
  }

  @Test
    @Timeout(8000)
  public void testParse_withIOException_shouldThrowJsonIOException() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenThrow(new IOException("IO error"));
    assertThrows(JsonIOException.class, () -> {
      jsonParser.parse(jsonReader);
    });
  }

  @Test
    @Timeout(8000)
  public void testParse_withClosedJsonReader_shouldThrowJsonIOException() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenThrow(new IOException("Stream closed"));
    assertThrows(JsonIOException.class, () -> {
      jsonParser.parse(jsonReader);
    });
  }

  @Test
    @Timeout(8000)
  public void testParse_withNullJsonReader_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      jsonParser.parse((JsonReader) null);
    });
  }
}