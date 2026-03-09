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
        // no-op for this test
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
        return null;
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldReturnValue_whenValidJsonProvided() throws IOException {
    String json = "\"testValue\"";
    Reader reader = new StringReader(json);

    String result = typeAdapter.fromJson(reader);

    assertEquals("testValue", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldReturnNull_whenJsonIsNull() throws IOException {
    String json = "null";
    Reader reader = new StringReader(json);

    String result = typeAdapter.fromJson(reader);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldThrowIOException_whenReaderThrows() throws IOException {
    Reader reader = mock(Reader.class);
    when(reader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("read error"));

    IOException thrown = assertThrows(IOException.class, () -> typeAdapter.fromJson(reader));
    assertEquals("read error", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void fromJson_invokesReadMethodUsingReflection() throws Exception {
    Reader reader = new StringReader("\"reflectionTest\"");
    Method fromJsonMethod = TypeAdapter.class.getDeclaredMethod("fromJson", Reader.class);
    fromJsonMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    String result = (String) fromJsonMethod.invoke(typeAdapter, reader);

    assertEquals("reflectionTest", result);
  }
}