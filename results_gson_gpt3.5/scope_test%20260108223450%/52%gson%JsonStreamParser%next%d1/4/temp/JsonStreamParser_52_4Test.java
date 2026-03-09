package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class JsonStreamParser_52_4Test {

  private JsonStreamParser parser;
  private JsonReader mockJsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonStreamParser with an empty JSON string
    parser = new JsonStreamParser("{}");

    // Use reflection to set the private final parser field to a mock
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    mockJsonReader = mock(JsonReader.class);
    parserField.set(parser, mockJsonReader);

    // Use reflection to set the private final lock field to a dummy object
    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    lockField.set(parser, new Object());
  }

  @Test
    @Timeout(8000)
  public void next_whenHasNextFalse_throwsNoSuchElementException() throws Exception {
    // Arrange: mock hasNext() to return false using reflection to invoke private method or override
    // Since hasNext is public, just spy on parser and mock hasNext
    JsonStreamParser spyParser = Mockito.spy(parser);
    doReturn(false).when(spyParser).hasNext();

    // Act & Assert
    assertThrows(NoSuchElementException.class, spyParser::next);
  }

  @Test
    @Timeout(8000)
  public void next_whenStreamsParseReturnsJsonElement_returnsThatElement() throws Exception {
    // Arrange
    JsonElement expectedElement = mock(JsonElement.class);

    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(mockJsonReader)).thenReturn(expectedElement);

      // Act
      JsonElement actual = parser.next();

      // Assert
      assertSame(expectedElement, actual);
      streamsStatic.verify(() -> Streams.parse(mockJsonReader));
    }
  }

  @Test
    @Timeout(8000)
  public void next_whenStreamsParseThrowsStackOverflowError_throwsJsonParseException() throws Exception {
    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(mockJsonReader)).thenThrow(new StackOverflowError());

      JsonParseException ex = assertThrows(JsonParseException.class, () -> parser.next());
      assertTrue(ex.getMessage().contains("Failed parsing JSON source to Json"));
      assertTrue(ex.getCause() instanceof StackOverflowError);
    }
  }

  @Test
    @Timeout(8000)
  public void next_whenStreamsParseThrowsOutOfMemoryError_throwsJsonParseException() throws Exception {
    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(mockJsonReader)).thenThrow(new OutOfMemoryError());

      JsonParseException ex = assertThrows(JsonParseException.class, () -> parser.next());
      assertTrue(ex.getMessage().contains("Failed parsing JSON source to Json"));
      assertTrue(ex.getCause() instanceof OutOfMemoryError);
    }
  }
}