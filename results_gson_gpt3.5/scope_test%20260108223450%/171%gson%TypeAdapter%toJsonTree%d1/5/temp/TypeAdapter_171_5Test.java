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

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterToJsonTreeTest {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        if ("throw".equals(value)) {
          throw new IOException("forced exception");
        }
        if (out instanceof JsonTreeWriter) {
          ((JsonTreeWriter) out).value(value);
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
  void toJsonTree_returnsJsonElement() {
    JsonElement jsonElement = typeAdapter.toJsonTree("test");
    assertNotNull(jsonElement);
    assertEquals("test", jsonElement.getAsString());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_throwsJsonIOException_onIOException() {
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      typeAdapter.toJsonTree("throw");
    });
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("forced exception", thrown.getCause().getMessage());
  }
}