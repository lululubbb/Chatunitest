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

class JsonParser_436_1Test {

  @Test
    @Timeout(8000)
  void parseString_validJson_returnsJsonElement() {
    String json = "{\"key\":\"value\"}";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  void parseString_emptyJson_returnsJsonElement() {
    String json = "";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  void parseString_invalidJson_throwsJsonSyntaxException() {
    String json = "{key:\"value\""; // missing closing brace
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(json));
  }

  @Test
    @Timeout(8000)
  void parseString_nullString_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseString(null));
  }

  @Test
    @Timeout(8000)
  void parseString_jsonWithWhitespace_returnsJsonElement() {
    String json = "  { \"key\" : \"value\" }  ";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  void parseString_jsonArray_returnsJsonElement() {
    String json = "[1, 2, 3]";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
  }

}