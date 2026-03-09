package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class TypeAdapter_171_6Test {

  TypeAdapter<String> typeAdapter;

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
        return null;
      }
    };
  }

  @Test
    @Timeout(8000)
  void toJsonTree_shouldReturnJsonElementWithCorrectValue() throws IOException {
    JsonElement jsonElement = typeAdapter.toJsonTree("testValue");
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals("testValue", jsonElement.getAsString());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_shouldReturnJsonNullForNullValue() throws IOException {
    JsonElement jsonElement = typeAdapter.toJsonTree(null);
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_shouldThrowJsonIOExceptionOnWriteError() throws IOException {
    TypeAdapter<String> throwingAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        throw new IOException("write failed");
      }

      @Override
      public String read(com.google.gson.stream.JsonReader in) {
        return null;
      }
    };
    JsonIOException ex = assertThrows(JsonIOException.class, () -> throwingAdapter.toJsonTree("value"));
    assertNotNull(ex.getCause());
    assertEquals(IOException.class, ex.getCause().getClass());
    assertEquals("write failed", ex.getCause().getMessage());
  }
}