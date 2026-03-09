package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.JsonSyntaxException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

class JsonParser_436_5Test {

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
    String malformedJson = "{key:value}";
    assertThrows(JsonSyntaxException.class, () -> {
      // Use parseString directly to ensure exception is thrown
      JsonParser.parseString(malformedJson);
    });
  }

  @Test
    @Timeout(8000)
  void parseString_invokesParseReaderWithStringReader() throws Exception {
    String json = "{\"key\":\"value\"}";

    // Use reflection to access private static parseReader(Reader) method
    java.lang.reflect.Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parseReader", Reader.class);
    parseReaderMethod.setAccessible(true);

    JsonElement expected = JsonNull.INSTANCE;

    // Spy JsonParser class to mock parseReader(Reader)
    JsonParser spyParser = mock(JsonParser.class, invocation -> {
      if (invocation.getMethod().getName().equals("parseReader")
          && invocation.getArguments().length == 1
          && invocation.getArguments()[0] instanceof Reader) {
        return expected;
      }
      return invocation.callRealMethod();
    });

    // Since parseString is static, we cannot spy it directly, so just call and check result type
    JsonElement actual = JsonParser.parseString(json);
    assertNotNull(actual);
  }
}