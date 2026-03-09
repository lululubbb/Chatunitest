package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

class TypeAdapter_FromJson_Test {

  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    typeAdapter = new TypeAdapter<String>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, String value) {
        // no-op for this test
      }

      @Override
      public String read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.STRING) {
          return in.nextString();
        } else if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
          in.nextNull();
          return null;
        } else {
          return in.nextString();
        }
      }
    };
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJsonString_returnsExpectedValue() throws IOException {
    String json = "\"testValue\"";
    Reader reader = new StringReader(json);

    String result = typeAdapter.fromJson(reader);

    assertEquals("testValue", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullJson_returnsNull() throws IOException {
    String json = "null";
    Reader reader = new StringReader(json);

    String result = typeAdapter.fromJson(reader);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withEmptyString_throwsException() {
    Reader reader = new StringReader("");

    assertThrows(IOException.class, () -> {
      typeAdapter.fromJson(reader);
    });
  }

  @Test
    @Timeout(8000)
  void fromJson_invokesReadMethodUsingReflection() throws Exception {
    Reader reader = new StringReader("\"reflectionTest\"");
    // fromJson(Reader) is public, so getMethod is sufficient
    Method fromJsonMethod = TypeAdapter.class.getMethod("fromJson", Reader.class);
    fromJsonMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    String result = (String) fromJsonMethod.invoke(typeAdapter, reader);

    assertEquals("reflectionTest", result);
  }
}