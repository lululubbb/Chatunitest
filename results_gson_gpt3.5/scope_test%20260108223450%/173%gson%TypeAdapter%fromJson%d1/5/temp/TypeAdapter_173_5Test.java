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
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterFromJsonTest {

  TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        // not needed for fromJson tests
      }

      @Override
      public String read(JsonReader in) {
        try {
          if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
          }
          return in.nextString();
        } catch (IOException e) {
          return null;
        }
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJson_callsReadAndReturnsValue() throws IOException {
    String json = "\"testValue\"";
    Reader reader = new StringReader(json);
    String result = typeAdapter.fromJson(reader);
    assertEquals("testValue", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withEmptyJson_callsReadAndReturnsValue() throws IOException {
    String json = "\"\"";
    Reader reader = new StringReader(json);
    String result = typeAdapter.fromJson(reader);
    assertEquals("", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullJson_callsReadAndReturnsNull() throws IOException {
    String json = "null";
    Reader reader = new StringReader(json);
    String result = typeAdapter.fromJson(reader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withIOExceptionFromJsonReader_throwsIOException() {
    Reader mockReader = mock(Reader.class);
    try {
      doThrow(new IOException("read error")).when(mockReader).read(any(char[].class), anyInt(), anyInt());
    } catch (IOException e) {
      // won't happen here
    }

    assertThrows(IOException.class, () -> typeAdapter.fromJson(mockReader));
  }
}