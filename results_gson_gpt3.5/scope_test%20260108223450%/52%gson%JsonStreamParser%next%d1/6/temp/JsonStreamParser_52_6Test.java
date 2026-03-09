package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonStreamParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class JsonStreamParser_52_6Test {

  private JsonStreamParser parser;
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() throws Exception {
    parser = new JsonStreamParser("{}");
    // Access private final field 'parser' to mock it
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    jsonReader = mock(JsonReader.class);
    parserField.set(parser, jsonReader);
  }

  @Test
    @Timeout(8000)
  void next_whenHasNextFalse_throwsNoSuchElementException() {
    JsonStreamParser spyParser = spy(parser);
    doReturn(false).when(spyParser).hasNext();
    assertThrows(NoSuchElementException.class, spyParser::next);
  }

  @Test
    @Timeout(8000)
  void next_whenStreamsParseReturnsJsonElement_returnsThatElement() throws Exception {
    JsonElement element = mock(JsonElement.class);
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader)).thenReturn(element);
      JsonElement result = parser.next();
      assertSame(element, result);
    }
  }

  @Test
    @Timeout(8000)
  void next_whenStreamsParseThrowsStackOverflowError_throwsJsonParseException() {
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader)).thenThrow(new StackOverflowError("stack overflow"));
      JsonParseException ex = assertThrows(JsonParseException.class, () -> parser.next());
      assertTrue(ex.getCause() instanceof StackOverflowError);
      assertEquals("Failed parsing JSON source to Json", ex.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void next_whenStreamsParseThrowsOutOfMemoryError_throwsJsonParseException() {
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader)).thenThrow(new OutOfMemoryError("out of memory"));
      JsonParseException ex = assertThrows(JsonParseException.class, () -> parser.next());
      assertTrue(ex.getCause() instanceof OutOfMemoryError);
      assertEquals("Failed parsing JSON source to Json", ex.getMessage());
    }
  }
}