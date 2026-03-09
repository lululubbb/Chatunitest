package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapter_FromJson_Test {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, String value) {
        // no-op for test
      }

      @Override
      public String read(JsonReader in) throws IOException {
        if (in == null) {
          throw new NullPointerException();
        }
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        return in.nextString();
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJsonString_shouldReturnValue() throws IOException {
    String json = "\"hello\"";
    String result = typeAdapter.fromJson(json);
    assertEquals("hello", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withEmptyString_shouldThrowException() {
    String json = "";
    assertThrows(IOException.class, () -> typeAdapter.fromJson(json));
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullString_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> typeAdapter.fromJson((String) null));
  }
}