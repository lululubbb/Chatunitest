package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
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

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class JsonStreamParser_52_1Test {

  private JsonStreamParser parser;
  private JsonReader mockJsonReader;

  @BeforeEach
  void setUp() throws Exception {
    // Use the constructor with Reader so we can inject a StringReader
    parser = new JsonStreamParser(new StringReader("{}"));

    // Use reflection to replace the private final parser field with a mock
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    mockJsonReader = mock(JsonReader.class);
    parserField.set(parser, mockJsonReader);
  }

  @Test
    @Timeout(8000)
  void next_whenHasNextFalse_throwsNoSuchElementException() throws Exception {
    // Use reflection to mock hasNext() to return false
    JsonStreamParser spyParser = Mockito.spy(parser);
    doReturn(false).when(spyParser).hasNext();

    assertThrows(NoSuchElementException.class, spyParser::next);
  }

  @Test
    @Timeout(8000)
  void next_whenStreamsParseReturnsJsonElement_returnsThatElement() throws Exception {
    JsonElement expectedElement = mock(JsonElement.class);

    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(mockJsonReader)).thenReturn(expectedElement);

      // Spy on parser to make hasNext() return true
      JsonStreamParser spyParser = Mockito.spy(parser);
      doReturn(true).when(spyParser).hasNext();

      JsonElement actual = spyParser.next();
      assertSame(expectedElement, actual);
    }
  }

  @Test
    @Timeout(8000)
  void next_whenStreamsParseThrowsStackOverflowError_throwsJsonParseException() throws Exception {
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(mockJsonReader)).thenThrow(new StackOverflowError());

      JsonStreamParser spyParser = Mockito.spy(parser);
      doReturn(true).when(spyParser).hasNext();

      JsonParseException ex = assertThrows(JsonParseException.class, spyParser::next);
      assertTrue(ex.getMessage().contains("Failed parsing JSON source to Json"));
      assertTrue(ex.getCause() instanceof StackOverflowError);
    }
  }

  @Test
    @Timeout(8000)
  void next_whenStreamsParseThrowsOutOfMemoryError_throwsJsonParseException() throws Exception {
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(mockJsonReader)).thenThrow(new OutOfMemoryError());

      JsonStreamParser spyParser = Mockito.spy(parser);
      doReturn(true).when(spyParser).hasNext();

      JsonParseException ex = assertThrows(JsonParseException.class, spyParser::next);
      assertTrue(ex.getMessage().contains("Failed parsing JSON source to Json"));
      assertTrue(ex.getCause() instanceof OutOfMemoryError);
    }
  }
}