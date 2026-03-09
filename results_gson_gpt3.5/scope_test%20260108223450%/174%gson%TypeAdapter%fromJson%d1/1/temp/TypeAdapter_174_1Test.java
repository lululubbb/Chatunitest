package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;

class TypeAdapterFromJsonTest {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, String value) {
        // no-op for test
      }

      @Override
      public String read(JsonReader in) {
        try {
          // Read the next string token from JsonReader
          if (in.peek() == com.google.gson.stream.JsonToken.STRING) {
            return in.nextString();
          }
          return null;
        } catch (IOException e) {
          return null;
        }
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJsonString_returnsExpectedValue() throws IOException {
    String json = "\"hello\"";
    String result = typeAdapter.fromJson(json);
    assertEquals("hello", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withEmptyString_returnsNull() throws IOException {
    String json = "";
    String result = typeAdapter.fromJson(json);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withMalformedJson_throwsIOException() {
    String json = "\"hello"; // missing ending quote
    assertThrows(IOException.class, () -> typeAdapter.fromJson(json));
  }

  @Test
    @Timeout(8000)
  void fromJson_invokesPrivateFromJsonReaderUsingReflection() throws Exception {
    String json = "\"reflection\"";
    // Access private final T fromJson(Reader in) method using reflection
    Method fromJsonReader = TypeAdapter.class.getDeclaredMethod("fromJson", java.io.Reader.class);
    fromJsonReader.setAccessible(true);
    String result = (String) fromJsonReader.invoke(typeAdapter, new StringReader(json));
    assertEquals("reflection", result);
  }
}