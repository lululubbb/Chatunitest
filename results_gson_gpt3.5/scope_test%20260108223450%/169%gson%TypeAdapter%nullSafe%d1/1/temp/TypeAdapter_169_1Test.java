package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapter_169_1Test {

  private TypeAdapter<String> originalAdapter;
  private JsonWriter mockWriter;
  private JsonReader mockReader;
  private TypeAdapter<String> nullSafeAdapter;

  @BeforeEach
  void setUp() {
    originalAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
      }

      @Override
      public String read(JsonReader in) throws IOException {
        return in.nextString();
      }
    };
    nullSafeAdapter = originalAdapter.nullSafe();
    mockWriter = mock(JsonWriter.class);
    mockReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void write_withNonNullValue_callsOriginalWrite() throws IOException {
    String value = "test";
    nullSafeAdapter.write(mockWriter, value);
    verify(mockWriter, times(1)).value(value);
    verify(mockWriter, never()).nullValue();
  }

  @Test
    @Timeout(8000)
  void write_withNullValue_callsNullValue() throws IOException {
    nullSafeAdapter.write(mockWriter, null);
    verify(mockWriter, times(1)).nullValue();
    verify(mockWriter, never()).value(anyString());
  }

  @Test
    @Timeout(8000)
  void read_withNullToken_returnsNull() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(mockReader).nextNull();

    String result = nullSafeAdapter.read(mockReader);

    verify(mockReader, times(1)).peek();
    verify(mockReader, times(1)).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_withNonNullToken_callsOriginalRead() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.STRING);
    when(mockReader.nextString()).thenReturn("hello");

    String result = nullSafeAdapter.read(mockReader);

    verify(mockReader, times(1)).peek();
    assertEquals("hello", result);
  }
}