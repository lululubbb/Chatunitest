package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonParser_435_3Test {

  @Test
    @Timeout(8000)
  public void testParseString_validJson_returnsJsonElement() throws Exception {
    String json = "{\"key\":\"value\"}";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParseString_emptyJson_throwsException() {
    String json = "";
    assertThrows(com.google.gson.JsonIOException.class, () -> {
      JsonParser.parseString(json);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_validJson() throws IOException {
    Reader reader = new StringReader("{\"key\":\"value\"}");
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_emptyJson_throwsException() {
    Reader reader = new StringReader("");
    assertThrows(com.google.gson.JsonIOException.class, () -> {
      JsonParser.parseReader(reader);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_validJson() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    JsonElement element = JsonParser.parseReader(jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_emptyJson_throwsException() {
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    assertThrows(com.google.gson.JsonIOException.class, () -> {
      JsonParser.parseReader(jsonReader);
    });
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_String_validJson() throws Throwable {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
    parseMethod.setAccessible(true);
    Object element = parseMethod.invoke(parser, "{\"key\":\"value\"}");
    assertNotNull(element);
    assertTrue(element instanceof JsonElement);
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_Reader_validJson() throws Throwable {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
    parseMethod.setAccessible(true);
    Object element = parseMethod.invoke(parser, new StringReader("{\"key\":\"value\"}"));
    assertNotNull(element);
    assertTrue(element instanceof JsonElement);
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_JsonReader_validJson() throws Throwable {
    JsonParser parser = new JsonParser();
    Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    parseMethod.setAccessible(true);
    JsonReader jr = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    Object element = parseMethod.invoke(parser, jr);
    assertNotNull(element);
    assertTrue(element instanceof JsonElement);
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_malformedJson_throws() throws IOException {
    JsonReader jsonReader = spy(new JsonReader(new StringReader("{malformed json")));
    assertThrows(com.google.gson.JsonSyntaxException.class, () -> {
      JsonParser.parseReader(jsonReader);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_ioException_throws() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenThrow(new IOException("IO error"));
    assertThrows(com.google.gson.JsonIOException.class, () -> {
      JsonParser.parseReader(jsonReader);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_emptyJson_throwsIllegalStateException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    assertThrows(com.google.gson.JsonIOException.class, () -> {
      JsonParser.parseReader(jsonReader);
    });
  }
}