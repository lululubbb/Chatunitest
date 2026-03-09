package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonParser_437_1Test {

  @Test
    @Timeout(8000)
  void parseReader_validJson_consumesEntireDocument() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
    assertFalse(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void parseReader_emptyJson_returnsJsonNull() throws Exception {
    String json = "null";
    Reader reader = new StringReader(json);
    JsonElement element = JsonParser.parseReader(reader);
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void parseReader_partialConsumption_throwsJsonSyntaxException() throws Exception {
    // We cannot subclass final JsonParser or override static methods.
    // So test by feeding JSON that leaves trailing data.

    String json = "{\"key\":\"value\"} trailing";
    Reader stringReader = new StringReader(json);

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      // The exception thrown on trailing data is wrapped with MalformedJsonException
      // So check the message of the cause, not the exception itself.
      JsonParser.parseReader(stringReader);
    });
    assertNotNull(ex.getCause());
    assertTrue(ex.getCause() instanceof MalformedJsonException);
    assertEquals("Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 18 path $", ex.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void parseReader_malformedJson_throwsJsonSyntaxException() {
    String json = "{malformed json";
    Reader reader = new StringReader(json);

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      JsonParser.parseReader(reader);
    });
    assertNotNull(ex.getCause());
    assertTrue(ex.getCause() instanceof MalformedJsonException);
  }

  @Test
    @Timeout(8000)
  void parseReader_ioException_throwsJsonIOException() throws IOException {
    Reader reader = mock(Reader.class);
    when(reader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("io exception"));

    JsonIOException ex = assertThrows(JsonIOException.class, () -> {
      JsonParser.parseReader(reader);
    });
    assertNotNull(ex.getCause());
    assertTrue(ex.getCause() instanceof IOException);
  }

  @Test
    @Timeout(8000)
  void parseReader_numberFormatException_throwsJsonSyntaxException() {
    try {
      JsonReader jsonReader = mock(JsonReader.class);
      when(jsonReader.peek()).thenReturn(JsonToken.NUMBER);
      when(jsonReader.nextString()).thenThrow(new NumberFormatException("number format"));

      Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parseReader", JsonReader.class);
      parseReaderMethod.setAccessible(true);

      Executable executable = () -> {
        try {
          parseReaderMethod.invoke(null, jsonReader);
        } catch (InvocationTargetException e) {
          // unwrap and rethrow the cause to be caught by assertThrows
          throw e.getCause();
        }
      };

      JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, executable);
      assertNotNull(ex.getCause());
      assertTrue(ex.getCause() instanceof NumberFormatException);
    } catch (NoSuchMethodException e) {
      fail("parseReader(JsonReader) method not found");
    } catch (Throwable t) {
      fail("Unexpected throwable: " + t);
    }
  }
}