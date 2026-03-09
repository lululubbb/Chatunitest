package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

public class JsonStreamParser_51_4Test {

  private JsonReader mockJsonReader;

  @BeforeEach
  void setup() {
    mockJsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void testConstructor_withReader_setsLenientTrue() throws Exception {
    Reader reader = new StringReader("{\"key\":\"value\"}");
    JsonStreamParser parser = new JsonStreamParser(reader);

    // Using reflection to verify private final fields
    var parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader innerReader = (JsonReader) parserField.get(parser);

    assertNotNull(innerReader);
    // JsonReader#setLenient is void, so no direct way to verify except by behavior
  }

  @Test
    @Timeout(8000)
  void testConstructor_withString_invokesReaderConstructor() throws Exception {
    String json = "{\"a\":1}";
    // Use reflection to get the constructor JsonStreamParser(String)
    Constructor<JsonStreamParser> ctor = JsonStreamParser.class.getDeclaredConstructor(String.class);
    ctor.setAccessible(true);
    JsonStreamParser parser = ctor.newInstance(json);

    assertNotNull(parser);
  }

  @Test
    @Timeout(8000)
  void testHasNext_trueWhenNextTokenIsNotEndDocument() throws IOException {
    try (MockedConstruction<JsonReader> ignored = mockConstruction(JsonReader.class,
        (mock, context) -> {
          when(mock.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
        })) {
      JsonStreamParser parser = new JsonStreamParser(new StringReader("{}"));
      assertTrue(parser.hasNext());
    }
  }

  @Test
    @Timeout(8000)
  void testHasNext_falseWhenNextTokenIsEndDocument() throws IOException {
    try (MockedConstruction<JsonReader> ignored = mockConstruction(JsonReader.class,
        (mock, context) -> {
          when(mock.peek()).thenReturn(JsonToken.END_DOCUMENT);
        })) {
      JsonStreamParser parser = new JsonStreamParser(new StringReader("{}"));
      assertFalse(parser.hasNext());
    }
  }

  @Test
    @Timeout(8000)
  void testHasNext_throwsRuntimeExceptionOnIOException() throws IOException {
    try (MockedConstruction<JsonReader> ignored = mockConstruction(JsonReader.class,
        (mock, context) -> {
          when(mock.peek()).thenThrow(new IOException("IO error"));
        })) {
      JsonStreamParser parser = new JsonStreamParser(new StringReader("{}"));
      RuntimeException thrown = assertThrows(RuntimeException.class, parser::hasNext);
      assertTrue(thrown.getCause() instanceof IOException);
    }
  }

  @Test
    @Timeout(8000)
  void testNext_returnsJsonElement() throws IOException {
    JsonElement expectedElement = mock(JsonElement.class);

    try (MockedConstruction<JsonReader> ignored = mockConstruction(JsonReader.class,
        (mock, context) -> {
          when(mock.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
          // Streams.parse(JsonReader) is static and final, cannot mock easily.
          // So test next() behavior indirectly by invoking real method on real parser.
        })) {
      String json = "{\"key\":\"value\"}";
      JsonStreamParser parser = new JsonStreamParser(new StringReader(json));
      JsonElement element = parser.next();
      assertNotNull(element);
    }
  }

  @Test
    @Timeout(8000)
  void testNext_throwsNoSuchElementExceptionWhenNoNext() throws IOException {
    try (MockedConstruction<JsonReader> ignored = mockConstruction(JsonReader.class,
        (mock, context) -> {
          when(mock.peek()).thenReturn(JsonToken.END_DOCUMENT);
        })) {
      JsonStreamParser parser = new JsonStreamParser(new StringReader("{}"));
      assertThrows(NoSuchElementException.class, parser::next);
    }
  }

  @Test
    @Timeout(8000)
  void testNext_throwsRuntimeExceptionOnMalformedJsonException() throws IOException {
    try (MockedConstruction<JsonReader> ignored = mockConstruction(JsonReader.class,
        (mock, context) -> {
          when(mock.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
          doThrow(new MalformedJsonException("malformed")).when(mock).peek();
        })) {
      JsonStreamParser parser = new JsonStreamParser(new StringReader("{}"));
      // Because peek throws MalformedJsonException, next will throw RuntimeException
      RuntimeException thrown = assertThrows(RuntimeException.class, parser::next);
      assertTrue(thrown.getCause() instanceof MalformedJsonException);
    }
  }

  @Test
    @Timeout(8000)
  void testRemove_throwsUnsupportedOperationException() {
    JsonStreamParser parser = new JsonStreamParser(new StringReader("{}"));
    assertThrows(UnsupportedOperationException.class, parser::remove);
  }
}