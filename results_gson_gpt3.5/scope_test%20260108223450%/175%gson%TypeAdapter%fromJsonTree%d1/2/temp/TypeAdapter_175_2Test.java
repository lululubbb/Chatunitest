package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

class TypeAdapterFromJsonTreeTest {

  @Test
    @Timeout(8000)
  void fromJsonTree_shouldReturnReadValue_whenNoException() throws IOException {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);

    // Mock construction of JsonTreeReader to pass a mock to read()
    try (MockedConstruction<JsonTreeReader> mocked = Mockito.mockConstruction(JsonTreeReader.class,
        (mock, context) -> {
          // no additional behavior needed
        })) {

      TypeAdapter<String> adapter = new TypeAdapter<String>() {
        @Override
        public void write(JsonWriter out, String value) {
          // no-op
        }

        @Override
        public String read(JsonReader in) throws IOException {
          // Verify that the JsonReader passed is the mocked JsonTreeReader
          assertTrue(mocked.constructed().contains(in));
          return "readValue";
        }
      };

      // Act
      String result = adapter.fromJsonTree(jsonElement);

      // Assert
      assertEquals("readValue", result);
    }
  }

  @Test
    @Timeout(8000)
  void fromJsonTree_shouldThrowJsonIOException_whenReadThrowsIOException() {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    TypeAdapter<String> adapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        // no-op
      }

      @Override
      public String read(JsonReader in) throws IOException {
        throw new IOException("read failure");
      }
    };

    // Act & Assert
    JsonIOException ex = assertThrows(JsonIOException.class, () -> adapter.fromJsonTree(jsonElement));
    assertEquals("java.io.IOException: read failure", ex.getMessage());
    assertTrue(ex.getCause() instanceof IOException);
  }
}