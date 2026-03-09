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
import java.lang.reflect.Method;

public class JsonParser_436_3Test {

  @Test
    @Timeout(8000)
  public void testParseString_validJson_returnsJsonElement() throws Exception {
    String json = "{\"key\":\"value\"}";

    // Call the focal method
    JsonElement result = JsonParser.parseString(json);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testParseString_emptyString_returnsJsonElement() throws Exception {
    String json = "";

    JsonElement result = JsonParser.parseString(json);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testParseString_nullString_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseString(null));
  }

  @Test
    @Timeout(8000)
  public void testParseString_malformedJson_throwsJsonSyntaxException() {
    String malformedJson = "{key:value";

    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(malformedJson));
  }

  @Test
    @Timeout(8000)
  public void testParseString_invokesParseReaderWithStringReader() throws Exception {
    String json = "{\"key\":\"value\"}";

    // Use reflection to access private static parseReader(Reader) method
    Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parseReader", Reader.class);
    parseReaderMethod.setAccessible(true);

    // Spy on JsonParser class to verify parseReader(Reader) is called by parseString
    JsonParser parserSpy = mock(JsonParser.class, invocation -> {
      if (invocation.getMethod().equals(parseReaderMethod)) {
        return JsonParser.parseReader((Reader) invocation.getArgument(0));
      }
      return invocation.callRealMethod();
    });

    // Since parseString is static, we can't spy it directly.
    // Instead, verify parseReader(Reader) behavior indirectly by calling parseReader directly.
    JsonElement directResult = (JsonElement) parseReaderMethod.invoke(null, new StringReader(json));
    JsonElement parseStringResult = JsonParser.parseString(json);

    assertEquals(directResult.getClass(), parseStringResult.getClass());
  }
}