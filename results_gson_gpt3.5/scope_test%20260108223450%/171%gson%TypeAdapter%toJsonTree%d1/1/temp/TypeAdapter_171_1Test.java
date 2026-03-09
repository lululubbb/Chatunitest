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
import org.mockito.InOrder;

class TypeAdapter_171_1Test {

  private TypeAdapter<String> typeAdapter;
  private JsonTreeWriter jsonTreeWriterMock;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        // Simulate writing by calling methods on JsonWriter
        if ("throw".equals(value)) {
          throw new IOException("Forced IOException");
        }
        out.value(value);
      }

      @Override
      public String read(JsonReader in) {
        return null;
      }
    };
  }

  @Test
    @Timeout(8000)
  void toJsonTree_shouldReturnJsonElementForValidValue() throws IOException {
    JsonElement jsonElement = typeAdapter.toJsonTree("test");
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals("test", jsonElement.getAsString());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_shouldThrowJsonIOExceptionWhenWriteThrowsIOException() {
    JsonIOException exception = assertThrows(JsonIOException.class, () -> typeAdapter.toJsonTree("throw"));
    assertNotNull(exception.getCause());
    assertTrue(exception.getCause() instanceof IOException);
    assertEquals("Forced IOException", exception.getCause().getMessage());
  }
}