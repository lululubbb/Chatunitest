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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.lang.reflect.Method;

class TypeAdapter_fromJsonTree_Test {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  public void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        // no-op for test
      }

      @Override
      public String read(JsonReader in) throws IOException {
        // For testing, just return a constant string or read a string token
        if (in.peek() == JsonToken.STRING) {
          return in.nextString();
        } else if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        return "readValue";
      }
    };
  }

  @Test
    @Timeout(8000)
  public void fromJsonTree_returnsReadValue() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    try (MockedStatic<JsonTreeReader> jsonTreeReaderStatic = mockStatic(JsonTreeReader.class)) {
      JsonTreeReader jsonTreeReader = mock(JsonTreeReader.class);
      jsonTreeReaderStatic.when(() -> new JsonTreeReader(jsonElement)).thenReturn(jsonTreeReader);
      when(jsonTreeReader.peek()).thenReturn(JsonToken.STRING);
      when(jsonTreeReader.nextString()).thenReturn("testValue");

      TypeAdapter<String> spyAdapter = spy(typeAdapter);
      doReturn("testValue").when(spyAdapter).read(jsonTreeReader);

      String result = spyAdapter.fromJsonTree(jsonElement);

      assertEquals("testValue", result);
      verify(spyAdapter).read(jsonTreeReader);
    }
  }

  @Test
    @Timeout(8000)
  public void fromJsonTree_throwsJsonIOException_whenIOException() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    try (MockedStatic<JsonTreeReader> jsonTreeReaderStatic = mockStatic(JsonTreeReader.class)) {
      JsonTreeReader jsonTreeReader = mock(JsonTreeReader.class);
      jsonTreeReaderStatic.when(() -> new JsonTreeReader(jsonElement)).thenReturn(jsonTreeReader);

      TypeAdapter<String> spyAdapter = spy(typeAdapter);
      doThrow(new IOException("IO error")).when(spyAdapter).read(jsonTreeReader);

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> spyAdapter.fromJsonTree(jsonElement));
      assertEquals("java.io.IOException: IO error", thrown.getCause().toString());
    }
  }

  @Test
    @Timeout(8000)
  public void fromJsonTree_invokesPrivateMethodUsingReflection() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    JsonTreeReader jsonTreeReader = mock(JsonTreeReader.class);

    Method fromJsonTreeMethod = TypeAdapter.class.getDeclaredMethod("fromJsonTree", JsonElement.class);
    fromJsonTreeMethod.setAccessible(true);

    try (MockedStatic<JsonTreeReader> jsonTreeReaderStatic = mockStatic(JsonTreeReader.class)) {
      jsonTreeReaderStatic.when(() -> new JsonTreeReader(jsonElement)).thenReturn(jsonTreeReader);

      TypeAdapter<String> spyAdapter = spy(typeAdapter);
      doReturn("reflectionValue").when(spyAdapter).read(jsonTreeReader);

      String result = (String) fromJsonTreeMethod.invoke(spyAdapter, jsonElement);
      assertEquals("reflectionValue", result);
      verify(spyAdapter).read(jsonTreeReader);
    }
  }
}