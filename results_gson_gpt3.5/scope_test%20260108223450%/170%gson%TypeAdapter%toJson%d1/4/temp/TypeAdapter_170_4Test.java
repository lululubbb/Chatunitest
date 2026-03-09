package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterToJsonTest {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        if (value == null) {
          out.nullValue();
        } else {
          out.value(value);
        }
      }

      @Override
      public String read(com.google.gson.stream.JsonReader in) {
        return null; // Not needed for this test
      }
    };
  }

  @Test
    @Timeout(8000)
  void toJson_shouldReturnJsonString_whenValueIsNonNull() {
    String input = "test";
    String json = typeAdapter.toJson(input);
    assertEquals("\"test\"", json);
  }

  @Test
    @Timeout(8000)
  void toJson_shouldReturnJsonNull_whenValueIsNull() {
    String json = typeAdapter.toJson(null);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  void toJson_shouldThrowJsonIOException_whenWriteThrowsIOException() {
    TypeAdapter<String> mockAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        throw new IOException("forced");
      }

      @Override
      public String read(com.google.gson.stream.JsonReader in) {
        return null;
      }
    };

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> mockAdapter.toJson("anything"));
    assertTrue(thrown.getCause() instanceof IOException);
    assertTrue(thrown.getCause().getMessage().contains("forced"));
  }
}