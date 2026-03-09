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
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JsonParser_441_2Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidJsonReader_returnsJsonElement() throws IOException {
    String json = "{\"key\":\"value\"}";
    JsonReader reader = new JsonReader(new StringReader(json));
    JsonElement element = jsonParser.parse(reader);
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testParse_withMalformedJson_throwsJsonSyntaxException() throws IOException {
    String malformedJson = "{key:\"value\""; // missing closing brace
    JsonReader reader = new JsonReader(new StringReader(malformedJson));
    assertThrows(JsonSyntaxException.class, () -> jsonParser.parse(reader));
  }

  @Test
    @Timeout(8000)
  public void testParse_withIOException_throwsJsonIOException() throws IOException {
    Reader mockReader = mock(Reader.class);
    JsonReader jsonReader = new JsonReader(mockReader);
    doThrow(new IOException("IO error")).when(mockReader).read(any(char[].class), anyInt(), anyInt());

    assertThrows(JsonIOException.class, () -> jsonParser.parse(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testParse_withNullJsonReader_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> jsonParser.parse((JsonReader) null));
  }

  @Test
    @Timeout(8000)
  public void testParse_callsParseReader() throws Exception {
    // Use reflection to invoke private parseReader(JsonReader) to check coverage indirectly
    String json = "{\"key\":\"value\"}";
    JsonReader reader = new JsonReader(new StringReader(json));

    // invoke parseReader(JsonReader) statically via JsonParser
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
  }
}