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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;

class TypeAdapter_169_3Test {

  private TypeAdapter<String> originalAdapter;
  private TypeAdapter<String> nullSafeAdapter;
  private JsonWriter jsonWriter;
  private JsonReader jsonReader;

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

    jsonWriter = mock(JsonWriter.class);
    jsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void nullSafe_write_nullValue_callsNullValue() throws IOException {
    nullSafeAdapter.write(jsonWriter, null);
    verify(jsonWriter).nullValue();
    verifyNoMoreInteractions(jsonWriter);
  }

  @Test
    @Timeout(8000)
  void nullSafe_write_nonNullValue_delegatesToOriginalWrite() throws IOException {
    nullSafeAdapter.write(jsonWriter, "test");
    InOrder inOrder = inOrder(jsonWriter);
    inOrder.verify(jsonWriter).value("test");
    inOrder.verifyNoMoreInteractions();
  }

  @Test
    @Timeout(8000)
  void nullSafe_read_nullToken_returnsNull() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    String result = nullSafeAdapter.read(jsonReader);

    verify(jsonReader).peek();
    verify(jsonReader).nextNull();
    verifyNoMoreInteractions(jsonReader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void nullSafe_read_nonNullToken_delegatesToOriginalRead() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("value");

    String result = nullSafeAdapter.read(jsonReader);

    verify(jsonReader).peek();
    verify(jsonReader).nextString();
    assertEquals("value", result);
  }
}