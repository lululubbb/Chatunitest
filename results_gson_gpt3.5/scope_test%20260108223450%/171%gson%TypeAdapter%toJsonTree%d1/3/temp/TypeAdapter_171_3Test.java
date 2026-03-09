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

class TypeAdapter_171_3Test {

  private TypeAdapter<Object> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<Object>() {
      @Override
      public void write(JsonWriter out, Object value) throws IOException {
        // For testing, write a simple JsonElement via JsonTreeWriter
        if (value == null) {
          out.nullValue();
        } else {
          out.value(value.toString());
        }
      }

      @Override
      public Object read(com.google.gson.stream.JsonReader in) {
        return null; // Not needed for this test
      }
    };
  }

  @Test
    @Timeout(8000)
  void toJsonTree_returnsJsonElement_whenValueIsNonNull() throws IOException {
    Object value = "testValue";
    JsonElement jsonElement = typeAdapter.toJsonTree(value);
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals("testValue", jsonElement.getAsString());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_returnsJsonNull_whenValueIsNull() throws IOException {
    Object value = null;
    JsonElement jsonElement = typeAdapter.toJsonTree(value);
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_throwsJsonIOException_whenWriteThrowsIOException() {
    TypeAdapter<Object> throwingAdapter = new TypeAdapter<Object>() {
      @Override
      public void write(JsonWriter out, Object value) throws IOException {
        throw new IOException("forced exception");
      }

      @Override
      public Object read(com.google.gson.stream.JsonReader in) {
        return null;
      }
    };

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      throwingAdapter.toJsonTree("anything");
    });
    assertEquals("java.io.IOException: forced exception", thrown.getMessage());
  }
}