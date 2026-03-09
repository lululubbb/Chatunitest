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

class TypeAdapter_169_6Test {

  private TypeAdapter<String> originalAdapter;
  private TypeAdapter<String> nullSafeAdapter;
  private JsonWriter mockWriter;
  private JsonReader mockReader;

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
  void write_whenValueIsNull_callsNullValue() throws IOException {
    nullSafeAdapter.write(mockWriter, null);
    verify(mockWriter).nullValue();
    verifyNoMoreInteractions(mockWriter);
  }

  @Test
    @Timeout(8000)
  void write_whenValueIsNotNull_delegatesToOriginalWrite() throws IOException {
    String testValue = "test";
    nullSafeAdapter.write(mockWriter, testValue);
    verify(mockWriter).value(testValue);
    verifyNoMoreInteractions(mockWriter);
  }

  @Test
    @Timeout(8000)
  void read_whenNextTokenIsNull_returnsNull() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(mockReader).nextNull();

    String result = nullSafeAdapter.read(mockReader);

    verify(mockReader).peek();
    verify(mockReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_whenNextTokenIsNotNull_delegatesToOriginalRead() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.STRING);
    when(mockReader.nextString()).thenReturn("value");

    String result = nullSafeAdapter.read(mockReader);

    verify(mockReader).peek();
    verify(mockReader).nextString();
    assertEquals("value", result);
  }
}