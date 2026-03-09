package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonParser_439_4Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidJsonString_shouldReturnJsonElement() {
    String json = "{\"key\":\"value\"}";

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expectedElement = new JsonPrimitive("mocked");
      mockedStatic.when(() -> JsonParser.parseString(json)).thenReturn(expectedElement);

      JsonElement result = jsonParser.parse(json);
      assertSame(expectedElement, result);

      mockedStatic.verify(() -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  public void testParse_withNullString_shouldThrowException() {
    assertThrows(NullPointerException.class, () -> jsonParser.parse((String) null));
  }

  @Test
    @Timeout(8000)
  public void testParseString_withValidJson_shouldReturnJsonElement() {
    String json = "{\"number\":123}";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParseString_withMalformedJson_shouldThrowJsonSyntaxException() {
    String json = "{invalid json}";
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(json));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withValidReader_shouldReturnJsonElement() throws IOException {
    String json = "[1,2,3]";
    Reader reader = new StringReader(json);
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
    assertTrue(element.isJsonArray());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withMalformedJson_shouldThrowJsonSyntaxException() {
    Reader reader = new StringReader("{bad json");
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withNullReader_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseReader((Reader) null));
  }

  @Test
    @Timeout(8000)
  public void testParse_withReader_shouldReturnJsonElement() throws Exception {
    String json = "{\"a\":1}";
    Reader reader = new StringReader(json);

    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
    parseMethod.setAccessible(true);

    JsonElement element = (JsonElement) parseMethod.invoke(jsonParser, reader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParse_withJsonReader_shouldReturnJsonElement() throws Exception {
    String json = "{\"b\":2}";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    parseMethod.setAccessible(true);

    JsonElement element = (JsonElement) parseMethod.invoke(jsonParser, jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParse_withJsonReader_malformedJson_shouldThrowInvocationTargetException() throws Exception {
    JsonReader jsonReader = new JsonReader(new StringReader("{bad json"));

    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    parseMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> parseMethod.invoke(jsonParser, jsonReader));
    assertTrue(thrown.getCause() instanceof JsonSyntaxException);
  }
}