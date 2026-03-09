package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TypeAdapterFromJsonTest {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        // not needed for fromJson tests
      }

      @Override
      public String read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        return in.nextString();
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJsonString_readsValue() throws IOException {
    String json = "\"hello\"";
    Reader reader = new StringReader(json);
    String result = typeAdapter.fromJson(reader);
    assertEquals("hello", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withEmptyReader_readsNull() throws IOException {
    Reader reader = new StringReader("");
    // The JsonReader will throw EOFException on empty input, so we expect an IOException
    assertThrows(IOException.class, () -> typeAdapter.fromJson(reader));
  }

  @Test
    @Timeout(8000)
  void fromJson_withMalformedJson_throwsIOException() {
    Reader reader = new StringReader("{ malformed json");
    assertThrows(IOException.class, () -> typeAdapter.fromJson(reader));
  }

  @Test
    @Timeout(8000)
  void fromJson_callsReadWithJsonReader() throws Exception {
    TypeAdapter<String> spyAdapter = Mockito.spy(typeAdapter);

    // Use reflection to access fromJson(Reader)
    java.lang.reflect.Method fromJsonMethod = TypeAdapter.class.getMethod("fromJson", Reader.class);

    // Use a real StringReader with valid JSON string
    String json = "\"test\"";
    Reader realReader = new StringReader(json);
    String result = (String) fromJsonMethod.invoke(spyAdapter, realReader);

    verify(spyAdapter).read(any(JsonReader.class));
    assertEquals("test", result);
  }
}