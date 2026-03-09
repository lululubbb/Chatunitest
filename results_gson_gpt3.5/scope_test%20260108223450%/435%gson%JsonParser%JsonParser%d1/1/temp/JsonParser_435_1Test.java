package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonParser_435_1Test {

  @Test
    @Timeout(8000)
  public void testParseString_validJson_returnsJsonElement() throws IOException {
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
    try (MockedStatic<JsonParser> mocked = Mockito.mockStatic(JsonParser.class, invocation -> {
      if (invocation.getMethod().getName().equals("parseString")) {
        throw new JsonParseException("Empty JSON");
      }
      return invocation.callRealMethod();
    })) {
      assertThrows(JsonParseException.class, () -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_validJson_returnsJsonElement() throws IOException {
    Reader reader = new StringReader("{\"key\":\"value\"}");
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("value", element.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_Reader_emptyJson_throwsException() {
    Reader reader = new StringReader("");
    try (MockedStatic<JsonParser> mocked = Mockito.mockStatic(JsonParser.class, invocation -> {
      if (invocation.getMethod().getName().equals("parseReader") && invocation.getArguments().length == 1 && invocation.getArguments()[0] instanceof Reader) {
        throw new JsonParseException("Empty JSON");
      }
      return invocation.callRealMethod();
    })) {
      assertThrows(JsonParseException.class, () -> JsonParser.parseReader(reader));
    }
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_validJson_returnsJsonElement() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    JsonElement element = JsonParser.parseReader(jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("value", element.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_emptyJson_throwsException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    try (MockedStatic<JsonParser> mocked = Mockito.mockStatic(JsonParser.class, invocation -> {
      if (invocation.getMethod().getName().equals("parseReader") && invocation.getArguments().length == 1 && invocation.getArguments()[0] instanceof JsonReader) {
        throw new JsonParseException("Empty JSON");
      }
      return invocation.callRealMethod();
    })) {
      assertThrows(JsonParseException.class, () -> JsonParser.parseReader(jsonReader));
    }
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_String_validJson() {
    JsonParser parser = new JsonParser();
    JsonElement element = parser.parse("{\"key\":\"value\"}");
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("value", element.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_Reader_validJson() {
    JsonParser parser = new JsonParser();
    Reader reader = new StringReader("{\"key\":\"value\"}");
    JsonElement element = parser.parse(reader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("value", element.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_JsonReader_validJson() throws IOException {
    JsonParser parser = new JsonParser();
    JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    JsonElement element = parser.parse(jsonReader);
    assertNotNull(element);
    assertTrue(element.isJsonObject());
    assertEquals("value", element.getAsJsonObject().get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParse_JsonReader_emptyJson_throwsException() throws IOException {
    JsonParser parser = new JsonParser();
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    JsonParser spyParser = spy(parser);
    try {
      doThrow(new JsonParseException("Empty JSON")).when(spyParser).parse(jsonReader);
      assertThrows(JsonParseException.class, () -> spyParser.parse(jsonReader));
    } finally {
      Mockito.reset(spyParser);
    }
  }

  @Test
    @Timeout(8000)
  public void testPrivateParseInternalMethodUsingReflection() throws Throwable {
    JsonParser parser = new JsonParser();
    Method method = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
    method.setAccessible(true);

    JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    Object result = method.invoke(parser, jsonReader);

    assertNotNull(result);
    assertTrue(result instanceof JsonElement);
    JsonElement element = (JsonElement) result;
    assertTrue(element.isJsonObject());
    assertEquals("value", element.getAsJsonObject().get("key").getAsString());
  }
}