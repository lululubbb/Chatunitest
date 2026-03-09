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
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterFromJsonTest {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, String value) {
        // no-op for test
      }

      @Override
      public String read(JsonReader in) {
        try {
          if (in.peek() == JsonToken.STRING) {
            return in.nextString();
          } else if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
          }
          throw new IllegalStateException("Unexpected token");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJsonString_returnsParsedValue() throws Exception {
    String json = "\"testValue\"";
    String result = typeAdapter.fromJson(json);
    assertEquals("testValue", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullJsonString_returnsNull() throws Exception {
    String json = "null";
    String result = typeAdapter.fromJson(json);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withInvalidJson_throwsIOException() {
    String invalidJson = "{ invalid json }";
    assertThrows(IOException.class, () -> {
      // Use reflection to invoke private final fromJson(Reader) to simulate IOException from read()
      Method fromJsonReaderMethod = TypeAdapter.class.getDeclaredMethod("fromJson", java.io.Reader.class);
      fromJsonReaderMethod.setAccessible(true);

      // Create a TypeAdapter that throws IOException from read()
      TypeAdapter<String> adapter = new TypeAdapter<String>() {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, String value) {
          // no-op
        }

        @Override
        public String read(JsonReader in) throws IOException {
          throw new IOException("forced");
        }
      };

      fromJsonReaderMethod.invoke(adapter, new StringReader(invalidJson));
    });
  }

  @Test
    @Timeout(8000)
  void fromJson_invokesPrivateFromJsonReader() throws Exception {
    String json = "\"reflectionTest\"";

    Method fromJsonReaderMethod = TypeAdapter.class.getDeclaredMethod("fromJson", java.io.Reader.class);
    fromJsonReaderMethod.setAccessible(true);

    Object result = fromJsonReaderMethod.invoke(typeAdapter, new StringReader(json));
    assertEquals("reflectionTest", result);
  }
}