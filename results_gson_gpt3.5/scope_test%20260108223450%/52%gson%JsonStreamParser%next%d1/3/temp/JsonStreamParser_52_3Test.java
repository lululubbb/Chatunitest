package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class JsonStreamParser_52_3Test {

  private JsonStreamParser parser;
  private JsonReader mockJsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Use a Reader with valid JSON to create parser instance
    parser = new JsonStreamParser(new StringReader("{\"key\":\"value\"}"));

    // Inject a mock JsonReader into the parser via reflection to control behavior
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    mockJsonReader = Mockito.mock(JsonReader.class);
    parserField.set(parser, mockJsonReader);

    // Also inject the lock object to avoid NullPointerException if needed
    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    lockField.set(parser, new Object());
  }

  @Test
    @Timeout(8000)
  public void testNextReturnsParsedJsonElement() throws Exception {
    // Mock hasNext() to return true
    JsonStreamParser spyParser = Mockito.spy(parser);
    doReturn(true).when(spyParser).hasNext();

    JsonElement expectedElement = Mockito.mock(JsonElement.class);

    // Mock static Streams.parse to return expectedElement
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(mockJsonReader)).thenReturn(expectedElement);

      JsonElement actual = spyParser.next();

      assertSame(expectedElement, actual);
      streamsMock.verify(() -> Streams.parse(mockJsonReader));
    }
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsNoSuchElementExceptionWhenNoNext() {
    JsonStreamParser spyParser = Mockito.spy(parser);
    doReturn(false).when(spyParser).hasNext();

    assertThrows(NoSuchElementException.class, spyParser::next);
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsJsonParseExceptionOnStackOverflowError() {
    JsonStreamParser spyParser = Mockito.spy(parser);
    doReturn(true).when(spyParser).hasNext();

    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(mockJsonReader)).thenThrow(new StackOverflowError("stack overflow"));

      JsonParseException ex = assertThrows(JsonParseException.class, spyParser::next);
      assertEquals("Failed parsing JSON source to Json", ex.getMessage());
      assertTrue(ex.getCause() instanceof StackOverflowError);
    }
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsJsonParseExceptionOnOutOfMemoryError() {
    JsonStreamParser spyParser = Mockito.spy(parser);
    doReturn(true).when(spyParser).hasNext();

    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(mockJsonReader)).thenThrow(new OutOfMemoryError("out of memory"));

      JsonParseException ex = assertThrows(JsonParseException.class, spyParser::next);
      assertEquals("Failed parsing JSON source to Json", ex.getMessage());
      assertTrue(ex.getCause() instanceof OutOfMemoryError);
    }
  }
}