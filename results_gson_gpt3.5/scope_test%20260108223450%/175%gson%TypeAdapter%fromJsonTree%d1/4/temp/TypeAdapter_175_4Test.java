package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.JsonIOException;
import com.google.gson.JsonElement;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TypeAdapter_175_4Test {

  @Test
    @Timeout(8000)
  void fromJsonTree_readsSuccessfully() throws IOException {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    JsonTreeReader jsonTreeReader = mock(JsonTreeReader.class);
    TypeAdapter<String> adapter = new TypeAdapter<String>() {
      @Override
      public String read(JsonReader in) throws IOException {
        return "readValue";
      }
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        // no-op
      }
    };

    // We cannot inject the mock JsonTreeReader directly because fromJsonTree instantiates it.
    // So we test the normal flow with a real JsonElement and the read method.

    // Act
    String result = adapter.fromJsonTree(jsonElement);

    // Assert
    assertEquals("readValue", result);
  }

  @Test
    @Timeout(8000)
  void fromJsonTree_throwsJsonIOExceptionOnIOException() {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    TypeAdapter<String> adapter = new TypeAdapter<String>() {
      @Override
      public String read(JsonReader in) throws IOException {
        throw new IOException("read failed");
      }
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        // no-op
      }
    };

    // Act & Assert
    JsonIOException ex = assertThrows(JsonIOException.class, () -> adapter.fromJsonTree(jsonElement));
    assertTrue(ex.getCause() instanceof IOException);
    assertEquals("read failed", ex.getCause().getMessage());
  }
}