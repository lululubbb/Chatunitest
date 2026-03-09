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
import java.lang.reflect.Field;

public class JsonParser_441_3Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidJsonReader_returnsJsonElement() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    JsonElement element = jsonParser.parse(jsonReader);
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testParse_withMalformedJson_throwsJsonSyntaxException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{key:\"value\"")); // Missing closing }
    assertThrows(JsonSyntaxException.class, () -> jsonParser.parse(jsonReader));
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
  public void testParse_withClosedJsonReader_throwsIllegalStateException() throws IOException, Exception {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));
    jsonReader.close();

    // The parse(JsonReader) method throws IllegalStateException if JsonReader is closed,
    // so assert that exception type instead of JsonIOException.
    assertThrows(IllegalStateException.class, () -> jsonParser.parse(jsonReader));
  }

}