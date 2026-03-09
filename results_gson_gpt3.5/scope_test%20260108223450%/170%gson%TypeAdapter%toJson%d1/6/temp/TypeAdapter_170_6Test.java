package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import com.google.gson.JsonIOException;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TypeAdapter_170_6Test {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  public void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        if ("throw".equals(value)) {
          throw new IOException("forced exception");
        }
        out.value(value);
      }

      @Override
      public String read(com.google.gson.stream.JsonReader in) {
        return null;
      }
    };
  }

  @Test
    @Timeout(8000)
  public void toJson_shouldReturnJsonString() {
    String input = "test";
    String json = typeAdapter.toJson(input);
    assertEquals("\"test\"", json);
  }

  @Test
    @Timeout(8000)
  public void toJson_shouldThrowJsonIOException_whenWriteThrowsIOException() {
    String input = "throw";
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> typeAdapter.toJson(input));
    assertEquals(IOException.class, thrown.getCause().getClass());
    assertEquals("forced exception", thrown.getCause().getMessage());
  }
}