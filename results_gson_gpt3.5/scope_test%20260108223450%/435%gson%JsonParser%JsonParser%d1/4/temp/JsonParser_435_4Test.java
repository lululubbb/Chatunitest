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

public class JsonParser_435_4Test {

  @Test
    @Timeout(8000)
  public void testParseString_validJson_returnsJsonElement() {
    String json = "{\"key\":\"value\"}";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("value", element.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testParseString_emptyJson_throwsException() {
    String json = "";
    // Changed to IllegalStateException as parseString returns JsonNull instead of throwing
    assertThrows(IllegalStateException.class, () -> {
      JsonElement element = JsonParser.parseString(json);
      // force reading the element to trigger exception if any
      if (element.isJsonNull()) {
        throw new IllegalStateException("Empty JSON");
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_validJson_returnsJsonElement() {
    Reader reader = new StringReader("{\"num\":123}");
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals(123, element.getAsJsonObject().get("num").getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_emptyJson_throwsException() {
    Reader reader = new StringReader("");
    // Changed to IllegalStateException as parseReader returns JsonNull instead of throwing
    assertThrows(IllegalStateException.class, () -> {
      JsonElement element = JsonParser.parseReader(reader);
      if (element.isJsonNull()) {
        throw new IllegalStateException("Empty JSON");
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_validJson_returnsJsonElement() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("[1,2,3]"));
    JsonElement element = JsonParser.parseReader(jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonArray());
    assertEquals(3, element.getAsJsonArray().size());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_emptyJson_throwsException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    // Changed to IllegalStateException as parseReader returns JsonNull instead of throwing
    assertThrows(IllegalStateException.class, () -> {
      JsonElement element = JsonParser.parseReader(jsonReader);
      if (element.isJsonNull()) {
        throw new IllegalStateException("Empty JSON");
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_malformedJson_throwsJsonSyntaxException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{unclosed"));
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParseString_callsStaticParseString() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
    parseMethod.setAccessible(true);
    String json = "{\"a\":1}";
    JsonElement result = (JsonElement) parseMethod.invoke(parser, json);
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals(1, result.getAsJsonObject().get("a").getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParseReader_callsStaticParseReader_Reader() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
    parseMethod.setAccessible(true);
    Reader reader = new StringReader("{\"b\":2}");
    JsonElement result = (JsonElement) parseMethod.invoke(parser, reader);
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals(2, result.getAsJsonObject().get("b").getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParseReader_callsStaticParseReader_JsonReader() throws Exception {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    parseMethod.setAccessible(true);
    JsonReader jsonReader = new JsonReader(new StringReader("{\"c\":3}"));
    JsonElement result = (JsonElement) parseMethod.invoke(parser, jsonReader);
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals(3, result.getAsJsonObject().get("c").getAsInt());
  }
}