package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.Streams;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonParser_440_4Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidReader_returnsJsonElement() throws IOException {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonElement result = jsonParser.parse(reader);

    assertNotNull(result);
    // Since parseReader delegates to parseReader(Reader) static method, 
    // and that returns a JsonElement representing the JSON, 
    // we can verify it is a JsonObject with the expected member.
    assertTrue(result.isJsonObject());
    assertEquals("value", result.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testParse_withEmptyJson_returnsJsonNull() {
    String json = "";
    Reader reader = new StringReader(json);

    JsonElement result = jsonParser.parse(reader);

    // The parser returns JsonNull for empty input instead of throwing exception
    assertNotNull(result);
    assertTrue(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testParse_withMalformedJson_throwsJsonSyntaxException() {
    String json = "{key:value"; // malformed json
    Reader reader = new StringReader(json);

    assertThrows(JsonSyntaxException.class, () -> jsonParser.parse(reader));
  }

  @Test
    @Timeout(8000)
  public void testParse_withNullReader_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> jsonParser.parse((Reader) null));
  }
}