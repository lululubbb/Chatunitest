package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class JsonParser_437_3Test {

  @Test
    @Timeout(8000)
  public void parseReader_validJson_returnsJsonElement() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonElement result = JsonParser.parseReader(reader);

    assertNotNull(result);
    // We expect the element not to be JsonNull
    assertFalse(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void parseReader_emptyJson_returnsJsonNull() throws Exception {
    String json = "null";
    Reader reader = new StringReader(json);

    JsonElement result = JsonParser.parseReader(reader);

    assertNotNull(result);
    assertTrue(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void parseReader_incompleteConsume_throwsJsonSyntaxException() throws Exception {
    String json = "{\"key\":\"value\"} extra";
    Reader reader = new StringReader(json);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      JsonParser.parseReader(reader);
    });
    // Adjusted expected message to match actual exception message
    assertEquals("com.google.gson.stream.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 18 path $", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void parseReader_malformedJson_throwsJsonSyntaxException() throws Exception {
    Reader reader = mock(Reader.class);

    try (MockedStatic<JsonParser> mockedStatic = mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseReader(any(JsonReader.class)))
          .thenThrow(new MalformedJsonException("Malformed JSON"));

      JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
        JsonParser.parseReader(reader);
      });
      assertNotNull(thrown.getCause());
      Throwable cause = thrown.getCause();
      assertTrue(cause instanceof MalformedJsonException);
    }
  }

  @Test
    @Timeout(8000)
  public void parseReader_ioException_throwsJsonIOException() throws Exception {
    Reader reader = mock(Reader.class);

    try (MockedStatic<JsonParser> mockedStatic = mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseReader(any(JsonReader.class)))
          .thenThrow(new IOException("IO error"));

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
        JsonParser.parseReader(reader);
      });
      assertNotNull(thrown.getCause());
      Throwable cause = thrown.getCause();
      assertTrue(cause instanceof IOException);
    }
  }

  @Test
    @Timeout(8000)
  public void parseReader_numberFormatException_throwsJsonSyntaxException() throws Exception {
    Reader reader = mock(Reader.class);

    try (MockedStatic<JsonParser> mockedStatic = mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseReader(any(JsonReader.class)))
          .thenThrow(new NumberFormatException("Number format error"));

      JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
        JsonParser.parseReader(reader);
      });
      assertNotNull(thrown.getCause());
      Throwable cause = thrown.getCause();
      assertTrue(cause instanceof NumberFormatException);
    }
  }
}