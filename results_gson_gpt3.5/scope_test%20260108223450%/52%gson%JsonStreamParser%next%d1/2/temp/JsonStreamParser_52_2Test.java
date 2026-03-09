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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class JsonStreamParser_52_2Test {

  private JsonStreamParser parser;
  private String json = "{\"key\":\"value\"}";

  @BeforeEach
  public void setUp() {
    parser = new JsonStreamParser(json);
  }

  @Test
    @Timeout(8000)
  public void testNextReturnsParsedElement() throws Exception {
    // Using real parser to parse valid JSON, next should return a JsonElement
    JsonElement element = parser.next();
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsNoSuchElementExceptionWhenHasNextFalse() {
    // Use a JSON input that represents an empty JSON array to avoid EOFException
    JsonStreamParser emptyParser = new JsonStreamParser("[]");
    // consume the single element (the array itself)
    assertTrue(emptyParser.hasNext());
    emptyParser.next();
    // now no more elements
    assertFalse(emptyParser.hasNext());
    assertThrows(NoSuchElementException.class, emptyParser::next);
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsJsonParseExceptionOnStackOverflowError() {
    JsonReader mockReader = mock(JsonReader.class);
    JsonStreamParser spyParser = createParserWithMockReader(mockReader);

    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(mockReader)).thenThrow(new StackOverflowError());

      JsonParseException ex = assertThrows(JsonParseException.class, spyParser::next);
      assertTrue(ex.getCause() instanceof StackOverflowError);
      assertEquals("Failed parsing JSON source to Json", ex.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsJsonParseExceptionOnOutOfMemoryError() {
    JsonReader mockReader = mock(JsonReader.class);
    JsonStreamParser spyParser = createParserWithMockReader(mockReader);

    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(mockReader)).thenThrow(new OutOfMemoryError());

      JsonParseException ex = assertThrows(JsonParseException.class, spyParser::next);
      assertTrue(ex.getCause() instanceof OutOfMemoryError);
      assertEquals("Failed parsing JSON source to Json", ex.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testNextUsesHasNextPrivateMethodViaReflection() throws Exception {
    // Use reflection to invoke private 'hasNext' method for coverage
    Method hasNextMethod = JsonStreamParser.class.getDeclaredMethod("hasNext");
    hasNextMethod.setAccessible(true);

    // Should return true for valid JSON
    assertTrue((boolean) hasNextMethod.invoke(parser));
  }

  private JsonStreamParser createParserWithMockReader(JsonReader mockReader) {
    // Create JsonStreamParser instance using reflection and inject mockReader into private field 'parser'
    try {
      JsonStreamParser instance = (JsonStreamParser) 
          JsonStreamParser.class.getConstructor(Reader.class).newInstance(new StringReader(""));
      var parserField = JsonStreamParser.class.getDeclaredField("parser");
      parserField.setAccessible(true);
      parserField.set(instance, mockReader);

      var lockField = JsonStreamParser.class.getDeclaredField("lock");
      lockField.setAccessible(true);
      lockField.set(instance, new Object());

      return instance;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}