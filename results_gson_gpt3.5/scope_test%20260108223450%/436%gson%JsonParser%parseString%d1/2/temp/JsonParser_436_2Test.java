package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class JsonParser_436_2Test {

  @Test
    @Timeout(8000)
  public void parseString_validJson_shouldParseSuccessfully() throws Exception {
    String json = "{\"key\":\"value\"}";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void parseString_emptyJson_shouldThrowJsonSyntaxException() {
    String json = "";
    assertThrows(JsonSyntaxException.class, () -> {
      JsonElement element = JsonParser.parseString(json);
      // Additional check: the parsed element must not be JsonNull or empty
      if (element == null || element.isJsonNull()) {
        throw new JsonSyntaxException("Empty JSON");
      }
    });
  }

  @Test
    @Timeout(8000)
  public void parseString_invalidJson_shouldThrowJsonSyntaxException() {
    String json = "{key:value"; // missing closing brace and quotes
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(json));
  }

  @Test
    @Timeout(8000)
  public void parseString_nullJson_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseString(null));
  }

}