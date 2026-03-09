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

public class JsonParser_437_2Test {

  @Test
    @Timeout(8000)
  public void parseReader_validJson_consumesEntireDocument() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonElement result = JsonParser.parseReader(reader);

    assertNotNull(result);
    assertFalse(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void parseReader_nullJson_returnsJsonNull() throws Exception {
    String json = "null";
    Reader reader = new StringReader(json);

    JsonElement result = JsonParser.parseReader(reader);

    assertNotNull(result);
    assertTrue(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void parseReader_partialConsumption_throwsJsonSyntaxException() throws Exception {
    String json = "{} extra";
    Reader reader = new StringReader(json);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      try {
        JsonParser.parseReader(reader);
      } catch (JsonSyntaxException e) {
        // The exception message is from MalformedJsonException wrapped in JsonSyntaxException
        // So we check the cause's type and message instead of the exception message itself
        Throwable cause = e.getCause();
        if (cause instanceof MalformedJsonException) {
          throw e;
        }
        throw e;
      }
    });
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof MalformedJsonException);
    assertTrue(cause.getMessage().contains("Use JsonReader.setLenient(true) to accept malformed JSON"));
  }

  @Test
    @Timeout(8000)
  public void parseReader_malformedJson_throwsJsonSyntaxException() throws Exception {
    String malformedJson = "{unclosed";
    Reader malformedReader = new StringReader(malformedJson);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      // Wrap the reader to set lenient false to trigger MalformedJsonException cause
      JsonReader jsonReader = new JsonReader(malformedReader);
      jsonReader.setLenient(false); // set lenient false to cause MalformedJsonException
      // Use reflection to invoke private static parseReader(JsonReader)
      java.lang.reflect.Method method = JsonParser.class.getDeclaredMethod("parseReader", JsonReader.class);
      method.setAccessible(true);
      try {
        method.invoke(null, jsonReader);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof MalformedJsonException);
  }

  @Test
    @Timeout(8000)
  public void parseReader_ioException_throwsJsonIOException() throws Exception {
    Reader reader = mock(Reader.class);
    when(reader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("io error"));

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      JsonParser.parseReader(reader);
    });
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
  }

  @Test
    @Timeout(8000)
  public void parseReader_numberFormatException_throwsJsonSyntaxException() throws Exception {
    // Create a JsonReader subclass that throws NumberFormatException when peek() is called
    Reader reader = new StringReader("123");
    JsonReader jsonReader = new JsonReader(reader) {
      @Override
      public JsonToken peek() throws IOException {
        throw new NumberFormatException("number format");
      }
    };

    // Use reflection to invoke private static parseReader(JsonReader)
    java.lang.reflect.Method method = JsonParser.class.getDeclaredMethod("parseReader", JsonReader.class);
    method.setAccessible(true);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      try {
        method.invoke(null, jsonReader);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof NumberFormatException);
  }
}