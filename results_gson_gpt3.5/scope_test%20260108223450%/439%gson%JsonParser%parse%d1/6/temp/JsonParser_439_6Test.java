package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

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
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonParser_439_6Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidJsonString_shouldReturnJsonElement() {
    String json = "{\"key\":\"value\"}";

    try (MockedStatic<JsonParser> parserStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expectedElement = mock(JsonElement.class);
      parserStatic.when(() -> JsonParser.parseString(json)).thenReturn(expectedElement);

      JsonElement result = jsonParser.parse(json);

      assertSame(expectedElement, result);
      parserStatic.verify(() -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  public void testParse_withNullString_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> jsonParser.parse((String) null));
  }

  @Test
    @Timeout(8000)
  public void testParsePrivateParseString_usingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    String json = "{\"a\":1}";

    Method parseStringMethod = JsonParser.class.getDeclaredMethod("parseString", String.class);
    parseStringMethod.setAccessible(true);

    try (MockedStatic<JsonParser> parserStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expected = mock(JsonElement.class);
      parserStatic.when(() -> JsonParser.parseString(json)).thenReturn(expected);

      JsonElement actual = (JsonElement) parseStringMethod.invoke(null, json);

      assertSame(expected, actual);
      parserStatic.verify(() -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  public void testParse_withEmptyJsonString_shouldReturnJsonElement() {
    String json = "";

    try (MockedStatic<JsonParser> parserStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expected = mock(JsonElement.class);
      parserStatic.when(() -> JsonParser.parseString(json)).thenReturn(expected);

      JsonElement actual = jsonParser.parse(json);

      assertSame(expected, actual);
      parserStatic.verify(() -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  public void testParse_withMalformedJson_shouldThrowJsonSyntaxException() {
    String malformedJson = "{";

    try (MockedStatic<JsonParser> parserStatic = Mockito.mockStatic(JsonParser.class)) {
      parserStatic.when(() -> JsonParser.parseString(malformedJson)).thenThrow(JsonSyntaxException.class);

      assertThrows(JsonSyntaxException.class, () -> jsonParser.parse(malformedJson));
      parserStatic.verify(() -> JsonParser.parseString(malformedJson));
    }
  }
}