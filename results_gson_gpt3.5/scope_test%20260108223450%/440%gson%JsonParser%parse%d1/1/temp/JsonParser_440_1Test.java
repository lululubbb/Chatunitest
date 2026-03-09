package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

public class JsonParser_440_1Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void parse_withValidReader_shouldReturnJsonElement() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    // Use reflection to call parse(Reader) to avoid ambiguity
    Method parseMethod = JsonParser.class.getMethod("parse", Reader.class);
    JsonElement result = (JsonElement) parseMethod.invoke(jsonParser, reader);

    assertNotNull(result);
    // Since parseReader is static and final, we trust it returns a JsonElement for valid JSON.
  }

  @Test
    @Timeout(8000)
  public void parse_withEmptyJson_shouldReturnJsonElement() throws Exception {
    String json = "";
    Reader reader = new StringReader(json);

    Method parseMethod = JsonParser.class.getMethod("parse", Reader.class);
    JsonElement result = (JsonElement) parseMethod.invoke(jsonParser, reader);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void parse_withMalformedJson_shouldThrowJsonSyntaxException() throws Exception {
    String malformedJson = "{key:\"value\""; // Missing closing brace
    Reader reader = new StringReader(malformedJson);

    Method parseMethod = JsonParser.class.getMethod("parse", Reader.class);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      try {
        parseMethod.invoke(jsonParser, reader);
      } catch (Exception e) {
        // unwrap InvocationTargetException
        Throwable cause = e.getCause();
        if (cause instanceof JsonSyntaxException) {
          throw (JsonSyntaxException) cause;
        }
        throw e;
      }
    });

    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void parse_withIOException_shouldThrowJsonIOException() throws Exception {
    Reader reader = mock(Reader.class);
    try {
      when(reader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("IO error"));
    } catch (IOException e) {
      // won't happen on mock setup
    }

    Method parseMethod = JsonParser.class.getMethod("parse", Reader.class);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      try {
        parseMethod.invoke(jsonParser, reader);
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause instanceof JsonIOException) {
          throw (JsonIOException) cause;
        }
        throw e;
      }
    });

    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void parse_withNullReader_shouldThrowNullPointerException() throws Exception {
    Method parseMethod = JsonParser.class.getMethod("parse", Reader.class);

    assertThrows(NullPointerException.class, () -> {
      try {
        parseMethod.invoke(jsonParser, (Reader) null);
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause instanceof NullPointerException) {
          throw (NullPointerException) cause;
        }
        throw e;
      }
    });
  }
}