package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterFromJsonTest {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        // no-op for test
      }

      @Override
      public String read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        if (token == JsonToken.STRING) {
          return in.nextString();
        }
        if (token == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        if (token == JsonToken.END_DOCUMENT) {
          return "default";
        }
        throw new IOException("Invalid JSON");
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
  void fromJson_withJsonNull_shouldReturnNull() throws IOException {
    String json = "null";
    String result = typeAdapter.fromJson(json);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withInvalidJson_shouldThrowIOException() throws Exception {
    String invalidJson = "{invalid json}";

    Method fromJsonReaderMethod = TypeAdapter.class.getDeclaredMethod("fromJson", java.io.Reader.class);
    fromJsonReaderMethod.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        fromJsonReaderMethod.invoke(typeAdapter, new StringReader(invalidJson));
      } catch (InvocationTargetException e) {
        // unwrap the IOException thrown by the invoked method
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          throw (IOException) cause;
        } else {
          throw e;
        }
      }
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void fromJson_withEmptyString_shouldReturnDefault() throws IOException {
    String json = "";
    String result = typeAdapter.fromJson(json);
    assertEquals("default", result);
  }
}