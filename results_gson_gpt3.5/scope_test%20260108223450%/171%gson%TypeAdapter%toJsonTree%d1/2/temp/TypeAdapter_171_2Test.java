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

class TypeAdapter_171_2Test {

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
  void toJsonTree_shouldReturnJsonElement_whenValueIsNonNull() throws IOException {
    // Arrange
    String input = "test";

    // Act
    JsonElement result = typeAdapter.toJsonTree(input);

    // Assert
    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals(input, result.getAsString());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_shouldReturnJsonNull_whenValueIsNull() throws IOException {
    // Act
    JsonElement result = typeAdapter.toJsonTree(null);

    // Assert
    assertNotNull(result);
    assertTrue(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_shouldThrowJsonIOException_whenWriteThrowsIOException() throws IOException {
    TypeAdapter<String> throwingAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        throw new IOException("forced");
      }

      @Override
      public String read(com.google.gson.stream.JsonReader in) {
        return null;
      }
    };

    JsonIOException exception = assertThrows(JsonIOException.class, () -> throwingAdapter.toJsonTree("value"));
    assertEquals(IOException.class, exception.getCause().getClass());
    assertEquals("forced", exception.getCause().getMessage());
  }
}