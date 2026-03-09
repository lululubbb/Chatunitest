package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterFromJsonTest {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        // no-op for this test
      }

      @Override
      public String read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.STRING) {
          return in.nextString();
        } else if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } else {
          return in.nextString();
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
  void fromJson_withJsonNullString_returnsNull() throws IOException {
    String json = "null";
    String result = typeAdapter.fromJson(json);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withEmptyString_throwsException() {
    String json = "";
    assertThrows(IOException.class, () -> typeAdapter.fromJson(json));
  }

  @Test
    @Timeout(8000)
  void fromJson_withMalformedJson_throwsException() {
    String json = "\"unterminated string";
    assertThrows(IOException.class, () -> typeAdapter.fromJson(json));
  }

  @Test
    @Timeout(8000)
  void fromJson_withWhitespaceJsonString_returnsExpectedValue() throws IOException {
    String json = "   \"test\"   ";
    String result = typeAdapter.fromJson(json);
    assertEquals("test", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withNonStringJsonToken_returnsValue() throws IOException {
    TypeAdapter<String> adapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        // no-op
      }

      @Override
      public String read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NUMBER) {
          return in.nextString();
        }
        return null;
      }
    };
    String json = "123";
    String result = adapter.fromJson(json);
    assertEquals("123", result);
  }
}