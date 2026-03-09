package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterFromJsonTest {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, String value) {
        // no-op for test
      }

      @Override
      public String read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.STRING) {
          return in.nextString();
        } else if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        throw new IOException("Unexpected token");
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJson_validString_returnsValue() throws IOException {
    String json = "\"test\"";
    Reader reader = new StringReader(json);
    String result = typeAdapter.fromJson(reader);
    assertEquals("test", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_nullValue_returnsNull() throws IOException {
    String json = "null";
    Reader reader = new StringReader(json);
    String result = typeAdapter.fromJson(reader);
    assertEquals(null, result);
  }

  @Test
    @Timeout(8000)
  void fromJson_invalidToken_throwsIOException() {
    String json = "123";
    Reader reader = new StringReader(json);
    assertThrows(IOException.class, () -> typeAdapter.fromJson(reader));
  }

  @Test
    @Timeout(8000)
  void fromJson_readerThrowsIOException_propagatesException() {
    Reader reader = mock(Reader.class);
    try {
      when(reader.read()).thenThrow(new IOException("read error"));
    } catch (IOException e) {
      // won't happen here
    }
    assertThrows(IOException.class, () -> typeAdapter.fromJson(reader));
  }
}