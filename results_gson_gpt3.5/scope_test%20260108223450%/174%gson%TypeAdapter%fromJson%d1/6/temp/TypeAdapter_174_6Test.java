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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

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
      public String read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        if (token == JsonToken.STRING) {
          return in.nextString();
        }
        if (token == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        throw new IOException("Unexpected token: " + token);
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
  void fromJson_withEmptyJsonString_returnsNull() throws IOException {
    String json = "null";
    String result = typeAdapter.fromJson(json);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withInvalidJson_throwsIOException() {
    String json = "{invalid json}";
    assertThrows(IOException.class, () -> {
      TypeAdapter<String> spyAdapter = spy(typeAdapter);
      doThrow(new IOException("mock io exception")).when(spyAdapter).read(any(JsonReader.class));
      spyAdapter.fromJson(json);
    });
  }

  @Test
    @Timeout(8000)
  void fromJson_invokesPrivateFromJsonReaderMethod() throws Exception {
    String json = "\"test\"";
    Method fromJsonMethod = TypeAdapter.class.getDeclaredMethod("fromJson", Reader.class);
    fromJsonMethod.setAccessible(true);
    String result = (String) fromJsonMethod.invoke(typeAdapter, new StringReader(json));
    assertEquals("test", result);
  }
}