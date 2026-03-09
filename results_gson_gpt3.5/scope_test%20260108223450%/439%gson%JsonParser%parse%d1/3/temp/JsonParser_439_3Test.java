package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

public class JsonParser_439_3Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidJsonString_returnsJsonElement() throws Exception {
    String json = "{\"key\":\"value\"}";

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expected = mock(JsonElement.class);
      mockedStatic.when(() -> JsonParser.parseString(json)).thenReturn(expected);

      JsonElement actual = jsonParser.parse(json);

      assertSame(expected, actual);
      mockedStatic.verify(() -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  public void testParse_withNullString_throwsException() {
    assertThrows(NullPointerException.class, () -> jsonParser.parse((String) null));
  }

  @Test
    @Timeout(8000)
  public void testParse_withReader_callsParseReader() throws Exception {
    Reader reader = new StringReader("{\"key\":\"value\"}");

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expected = mock(JsonElement.class);
      // Need to use reflection to call private parse(Reader) method
      mockedStatic.when(() -> JsonParser.parseReader(reader)).thenReturn(expected);

      Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
      parseReaderMethod.setAccessible(true);

      JsonElement actual = (JsonElement) parseReaderMethod.invoke(jsonParser, reader);

      assertSame(expected, actual);
      mockedStatic.verify(() -> JsonParser.parseReader(reader));
    }
  }

  @Test
    @Timeout(8000)
  public void testParse_withJsonReader_callsParseReader() throws Exception {
    JsonReader jsonReader = mock(JsonReader.class);

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expected = mock(JsonElement.class);
      // Need to use reflection to call private parse(JsonReader) method
      mockedStatic.when(() -> JsonParser.parseReader(jsonReader)).thenReturn(expected);

      Method parseJsonReaderMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
      parseJsonReaderMethod.setAccessible(true);

      JsonElement actual = (JsonElement) parseJsonReaderMethod.invoke(jsonParser, jsonReader);

      assertSame(expected, actual);
      mockedStatic.verify(() -> JsonParser.parseReader(jsonReader));
    }
  }

  @Test
    @Timeout(8000)
  public void testParseString_withMalformedJson_throwsJsonSyntaxException() {
    String malformedJson = "{key:\"value\""; // missing closing brace

    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(malformedJson));
  }

  @Test
    @Timeout(8000)
  public void testParseString_withEmptyJson_returnsJsonNull() {
    String emptyJson = "";

    JsonElement result = JsonParser.parseString(emptyJson);
    assertTrue(result instanceof JsonNull, "Expected JsonNull for empty JSON string");
  }
}