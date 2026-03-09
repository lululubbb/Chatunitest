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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class TypeAdapter_FromJsonTree_Test {

  private TypeAdapter<String> typeAdapter;
  private JsonElement jsonElementMock;

  @BeforeEach
  void setUp() {
    jsonElementMock = mock(JsonElement.class);

    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        // no-op for test
      }

      @Override
      public String read(JsonReader in) throws IOException {
        return "readValue";
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJsonTree_returnsReadValue() throws Exception {
    // Spy typeAdapter to allow partial mocking if needed
    TypeAdapter<String> spyAdapter = spy(typeAdapter);

    // Since fromJsonTree creates new JsonTreeReader internally,
    // we cannot inject the mock JsonTreeReader easily.
    // But read method is overridden to return "readValue" so this test should pass.

    String result = spyAdapter.fromJsonTree(jsonElementMock);

    assertEquals("readValue", result);
  }

  @Test
    @Timeout(8000)
  void fromJsonTree_throwsJsonIOExceptionOnIOException() throws Exception {
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

    JsonElement jsonElement = mock(JsonElement.class);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> adapter.fromJsonTree(jsonElement));
    assertEquals("java.io.IOException: read failure", thrown.getCause().toString());
  }
}