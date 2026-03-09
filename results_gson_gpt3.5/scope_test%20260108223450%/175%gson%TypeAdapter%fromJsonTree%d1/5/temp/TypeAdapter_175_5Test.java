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
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

class TypeAdapter_FromJsonTree_Test {

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
        throw new IOException("Unexpected token");
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJsonTree_shouldReturnValue_whenReadSucceeds() throws IOException {
    JsonElement jsonElement = mock(JsonElement.class);
    try (MockedConstruction<JsonTreeReader> mockedConstruction = Mockito.mockConstruction(JsonTreeReader.class,
        (mock, context) -> {
          when(mock.peek()).thenReturn(JsonToken.STRING);
          when(mock.nextString()).thenReturn("testValue");
        })) {
      String result = typeAdapter.fromJsonTree(jsonElement);
      assertEquals("testValue", result);
      // Verify JsonTreeReader was constructed with jsonElement
      assertEquals(1, mockedConstruction.constructed().size());
      JsonTreeReader createdReader = mockedConstruction.constructed().get(0);
      // No close verification as fromJsonTree does not close
    }
  }

  @Test
    @Timeout(8000)
  void fromJsonTree_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
    JsonElement jsonElement = mock(JsonElement.class);
    try (MockedConstruction<JsonTreeReader> mockedConstruction = Mockito.mockConstruction(JsonTreeReader.class,
        (mock, context) -> {
          when(mock.peek()).thenReturn(JsonToken.NULL);
          doNothing().when(mock).nextNull();
        })) {
      String result = typeAdapter.fromJsonTree(jsonElement);
      assertNull(result);
      assertEquals(1, mockedConstruction.constructed().size());
    }
  }

  @Test
    @Timeout(8000)
  void fromJsonTree_shouldThrowJsonIOException_whenReadThrowsIOException() throws IOException {
    JsonElement jsonElement = mock(JsonElement.class);
    TypeAdapter<String> spyAdapter = spy(typeAdapter);
    try (MockedConstruction<JsonTreeReader> mockedConstruction = Mockito.mockConstruction(JsonTreeReader.class,
        (mock, context) -> {
          when(mock.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
        })) {
      // Delay getting constructed instance until inside try block
      JsonTreeReader jsonTreeReader = mockedConstruction.constructed().get(0);
      doThrow(new IOException("read error")).when(spyAdapter).read(jsonTreeReader);

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> spyAdapter.fromJsonTree(jsonElement));
      assertEquals("java.io.IOException: read error", thrown.getMessage());
    }
  }
}