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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import com.google.gson.stream.JsonWriter;
import com.google.gson.JsonIOException;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapter_170_1Test {

  private TypeAdapter<String> typeAdapterSpy;

  @BeforeEach
  void setUp() {
    TypeAdapter<String> typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        // For testing, write the value as a JSON string
        out.value(value);
      }

      @Override
      public String read(JsonReader in) {
        return null;
      }
    };
    typeAdapterSpy = spy(typeAdapter);
  }

  @Test
    @Timeout(8000)
  void toJson_returnsCorrectJsonString() {
    String input = "testString";
    String json = typeAdapterSpy.toJson(input);
    assertEquals("\"testString\"", json);
  }

  @Test
    @Timeout(8000)
  void toJson_throwsJsonIOException_whenIOExceptionOccurs() throws IOException {
    doThrow(new IOException("mock IOException")).when(typeAdapterSpy).write(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> typeAdapterSpy.toJson("value"));
    assertEquals("java.io.IOException: mock IOException", thrown.getMessage());
  }
}