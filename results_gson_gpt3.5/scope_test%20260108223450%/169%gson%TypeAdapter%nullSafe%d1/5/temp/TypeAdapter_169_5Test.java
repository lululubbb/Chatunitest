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

class TypeAdapterNullSafeTest {

  TypeAdapter<String> originalAdapter;
  JsonWriter mockWriter;
  JsonReader mockReader;

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
    mockWriter = mock(JsonWriter.class);
    mockReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void write_withNull_callsNullValue() throws IOException {
    TypeAdapter<String> nullSafeAdapter = originalAdapter.nullSafe();

    nullSafeAdapter.write(mockWriter, null);

    verify(mockWriter).nullValue();
    verify(mockWriter, never()).value(anyString());
  }

  @Test
    @Timeout(8000)
  void write_withNonNull_callsOriginalWrite() throws IOException {
    TypeAdapter<String> nullSafeAdapter = originalAdapter.nullSafe();

    nullSafeAdapter.write(mockWriter, "test");

    verify(mockWriter).value("test");
    verify(mockWriter, never()).nullValue();
  }

  @Test
    @Timeout(8000)
  void read_withNullToken_returnsNull() throws IOException {
    TypeAdapter<String> nullSafeAdapter = originalAdapter.nullSafe();

    when(mockReader.peek()).thenReturn(JsonToken.NULL);
    doAnswer(invocation -> null).when(mockReader).nextNull();

    String result = nullSafeAdapter.read(mockReader);

    verify(mockReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_withNonNullToken_callsOriginalRead() throws IOException {
    TypeAdapter<String> nullSafeAdapter = originalAdapter.nullSafe();

    when(mockReader.peek()).thenReturn(JsonToken.STRING);
    when(mockReader.nextString()).thenReturn("value");

    String result = nullSafeAdapter.read(mockReader);

    assertEquals("value", result);
  }
}