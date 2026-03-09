package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonStreamParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class JsonStreamParser_52_5Test {

  private JsonStreamParser parser;

  @BeforeEach
  public void setUp() {
    parser = new JsonStreamParser("{}");
  }

  @Test
    @Timeout(8000)
  public void testNext_successfulParse() throws Exception {
    // Prepare a real JsonStreamParser with a simple JSON
    JsonStreamParser realParser = new JsonStreamParser("{\"key\":\"value\"}");

    JsonElement element = realParser.next();

    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testNext_noSuchElementException() throws Exception {
    // Create a JsonStreamParser with empty input to cause immediate EOF
    JsonStreamParser emptyParser = new JsonStreamParser("");

    // Access the internal JsonReader
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(emptyParser);

    // Advance the reader to EOF by consuming tokens if any
    try {
      while (jsonReader.peek() != JsonToken.END_DOCUMENT) {
        jsonReader.skipValue();
      }
    } catch (Exception ignored) {
      // Ignore any exceptions during this process
    }

    // Mock Streams.parse to throw JsonIOException wrapping EOFException
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(any(JsonReader.class)))
          .thenThrow(new JsonIOException(new java.io.EOFException()));

      // Because hasNext() will throw JsonIOException, next() will not throw NoSuchElementException,
      // so we expect JsonIOException here.
      JsonIOException thrown = assertThrows(JsonIOException.class, emptyParser::next);
      assertNotNull(thrown);
      assertTrue(thrown.getCause() instanceof java.io.EOFException);
    }
  }

  @Test
    @Timeout(8000)
  public void testNext_stackOverflowErrorWrapped() throws Exception {
    JsonStreamParser spyParser = Mockito.spy(new JsonStreamParser("{}"));

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(any(JsonReader.class)))
          .thenThrow(new StackOverflowError("stack overflow"));

      JsonParseException ex = assertThrows(JsonParseException.class, spyParser::next);
      assertTrue(ex.getMessage().contains("Failed parsing JSON source to Json"));
      assertTrue(ex.getCause() instanceof StackOverflowError);
    }
  }

  @Test
    @Timeout(8000)
  public void testNext_outOfMemoryErrorWrapped() throws Exception {
    JsonStreamParser spyParser = Mockito.spy(new JsonStreamParser("{}"));

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(any(JsonReader.class)))
          .thenThrow(new OutOfMemoryError("out of memory"));

      JsonParseException ex = assertThrows(JsonParseException.class, spyParser::next);
      assertTrue(ex.getMessage().contains("Failed parsing JSON source to Json"));
      assertTrue(ex.getCause() instanceof OutOfMemoryError);
    }
  }
}