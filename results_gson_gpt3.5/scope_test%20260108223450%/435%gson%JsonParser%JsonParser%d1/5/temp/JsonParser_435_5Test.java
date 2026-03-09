package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonParser_435_5Test {

  @Test
    @Timeout(8000)
  public void testParseString_validJson_returnsJsonElement() {
    String json = "{\"key\":\"value\"}";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParseString_emptyJson_throwsException() {
    String json = " ";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_validJson_returnsJsonElement() {
    Reader reader = new StringReader("{\"key\":\"value\"}");
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_emptyJson_throwsException() {
    Reader reader = new StringReader(" ");
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_validJson_returnsJsonElement() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    JsonElement element = JsonParser.parseReader(jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_emptyJson_throwsException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(" "));
    JsonElement element = JsonParser.parseReader(jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_String_validJson_returnsJsonElement() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
    parseMethod.setAccessible(true);
    JsonElement element = (JsonElement) parseMethod.invoke(parser, "{\"key\":\"value\"}");
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_Reader_validJson_returnsJsonElement() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
    parseMethod.setAccessible(true);
    JsonElement element = (JsonElement) parseMethod.invoke(parser, new StringReader("{\"key\":\"value\"}"));
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_JsonReader_validJson_returnsJsonElement() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    parseMethod.setAccessible(true);
    JsonReader jr = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    JsonElement element = (JsonElement) parseMethod.invoke(parser, jr);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_String_emptyJson_throwsException() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
    parseMethod.setAccessible(true);
    JsonElement element = (JsonElement) parseMethod.invoke(parser, " ");
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_Reader_emptyJson_throwsException() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
    parseMethod.setAccessible(true);
    JsonElement element = (JsonElement) parseMethod.invoke(parser, new StringReader(" "));
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_JsonReader_emptyJson_throwsException() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    parseMethod.setAccessible(true);
    JsonReader jr = new JsonReader(new StringReader(" "));
    JsonElement element = (JsonElement) parseMethod.invoke(parser, jr);
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }
}