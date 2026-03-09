package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

public class JsonParser_435_2Test {

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
  public void testParseString_nullString_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseString(null));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_validJson_returnsJsonElement() throws Exception {
    Reader reader = new StringReader("{\"number\":123}");
    JsonElement result = JsonParser.parseReader(reader);
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals(123, result.getAsJsonObject().get("number").getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_nullReader_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseReader((Reader) null));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_validJson_returnsJsonElement() throws Exception {
    JsonReader jsonReader = new JsonReader(new StringReader("[1,2,3]"));
    JsonElement result = JsonParser.parseReader(jsonReader);
    assertNotNull(result);
    assertTrue(result.isJsonArray());
    assertEquals(3, result.getAsJsonArray().size());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_null_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseReader((JsonReader) null));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_malformedJson_throwsJsonSyntaxException() {
    String malformedJson = "{unclosedObject:";
    JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));
    assertThrows(com.google.gson.JsonSyntaxException.class, () -> JsonParser.parseReader(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParseString_validJson_returnsJsonElement() {
    JsonParser parser = new JsonParser();
    String json = "{\"foo\":\"bar\"}";
    JsonElement element = parser.parse(json);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("bar", element.getAsJsonObject().get("foo").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParseReader_Reader_validJson_returnsJsonElement() {
    JsonParser parser = new JsonParser();
    Reader reader = new StringReader("{\"foo\":\"bar\"}");
    JsonElement element = parser.parse(reader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("bar", element.getAsJsonObject().get("foo").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParseReader_JsonReader_validJson_returnsJsonElement() {
    JsonParser parser = new JsonParser();
    JsonReader jsonReader = new JsonReader(new StringReader("{\"foo\":\"bar\"}"));
    JsonElement element = parser.parse(jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("bar", element.getAsJsonObject().get("foo").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor_invocation() throws Exception {
    // Using reflection to invoke the deprecated public constructor
    var constructor = JsonParser.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    JsonParser instance = constructor.newInstance();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  public void testPrivateParseMethodReflection() throws Exception {
    // No private method given, but demonstrating reflection invocation on parse(JsonReader)
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    parseMethod.setAccessible(true);

    JsonReader jsonReader = new JsonReader(new StringReader("{\"a\":1}"));
    JsonElement element = (JsonElement) parseMethod.invoke(parser, jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals(1, element.getAsJsonObject().get("a").getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_ioException_throwsJsonIOException() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenThrow(new IOException("IO error"));
    assertThrows(com.google.gson.JsonIOException.class, () -> JsonParser.parseReader(mockReader));
  }
}