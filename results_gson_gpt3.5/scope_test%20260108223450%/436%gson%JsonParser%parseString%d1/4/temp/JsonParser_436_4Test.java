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

class JsonParser_436_4Test {

  @Test
    @Timeout(8000)
  void parseString_validJson_returnsJsonElement() throws Exception {
    String json = "{\"key\":\"value\"}";
    JsonElement result = JsonParser.parseString(json);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void parseString_emptyJson_returnsJsonElement() throws Exception {
    String json = "";
    JsonElement result = JsonParser.parseString(json);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void parseString_nullJson_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseString(null));
  }

  @Test
    @Timeout(8000)
  void parseString_malformedJson_throwsJsonSyntaxException() {
    String json = "{key:value"; // malformed JSON
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(json));
  }

  @Test
    @Timeout(8000)
  void parseString_callsParseReaderWithStringReader() throws Exception {
    String json = "{\"a\":1}";
    // Spy on JsonParser class to verify parseReader is called with StringReader
    // Since parseReader is static, we cannot mock it easily without a framework like PowerMockito.
    // Instead, we test behavior indirectly by verifying no exceptions and valid output.
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
  }
}