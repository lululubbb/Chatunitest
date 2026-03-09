package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
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
import java.lang.reflect.Method;

public class JsonParser_435_6Test {

  private JsonParser parser;

  @BeforeEach
  public void setUp() {
    parser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParseString_validJson_returnsJsonElement() throws Exception {
    String json = "{\"key\":\"value\"}";

    JsonElement result = JsonParser.parseString(json);

    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals("value", result.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testParseString_nullInput_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseString(null));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_validJson_returnsJsonElement() throws IOException {
    String json = "[1,2,3]";
    Reader reader = new StringReader(json);

    JsonElement result = JsonParser.parseReader(reader);

    assertNotNull(result);
    assertTrue(result.isJsonArray());
    assertEquals(3, result.getAsJsonArray().size());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_nullReader_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseReader((Reader) null));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_validJson_returnsJsonElement() throws IOException {
    String json = "true";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    JsonElement result = JsonParser.parseReader(jsonReader);

    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertTrue(result.getAsJsonPrimitive().getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_nullReader_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseReader((JsonReader) null));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_malformedJson_throwsJsonSyntaxException() throws IOException {
    String json = "{unclosed_object:";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_String_callsParseString() throws Exception {
    String json = "{\"a\":1}";

    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
    parseMethod.setAccessible(true);
    JsonElement result = (JsonElement) parseMethod.invoke(parser, json);

    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals(1, result.getAsJsonObject().get("a").getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_Reader_callsParseReader() throws Exception {
    String json = "123";
    Reader reader = new StringReader(json);

    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
    parseMethod.setAccessible(true);
    JsonElement result = (JsonElement) parseMethod.invoke(parser, reader);

    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals(123, result.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_JsonReader_callsParseReader() throws Exception {
    String json = "\"string\"";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    parseMethod.setAccessible(true);
    JsonElement result = (JsonElement) parseMethod.invoke(parser, jsonReader);

    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals("string", result.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_callsStreams_parseThrowsIOException_throwsJsonIOException() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader))
          .thenAnswer(invocation -> { throw new IOException("io exception"); });

      assertThrows(JsonIOException.class, () -> JsonParser.parseReader(jsonReader));
    }
  }

  @Test
    @Timeout(8000)
  public void testParseReader_callsStreams_parseThrowsMalformedJsonException_throwsJsonSyntaxException() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader))
          .thenAnswer(invocation -> { throw new MalformedJsonException("malformed"); });

      assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(jsonReader));
    }
  }
}