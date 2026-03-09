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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class JsonStreamParser_51_2Test {

  JsonReader mockReader;
  JsonStreamParser parser;

  @BeforeEach
  void setup() throws Exception {
    mockReader = mock(JsonReader.class);
    // Use reflection to instantiate JsonStreamParser with mocked JsonReader
    Constructor<JsonStreamParser> constructor = JsonStreamParser.class.getDeclaredConstructor(Reader.class);
    constructor.setAccessible(true);
    // We pass a dummy Reader because constructor creates a JsonReader internally
    parser = constructor.newInstance(new StringReader("{}"));

    // Replace private final field 'parser' with mocked JsonReader
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(parser, mockReader);

    // Replace private final field 'lock' with new Object (not strictly necessary)
    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    lockField.set(parser, new Object());
  }

  @Test
    @Timeout(8000)
  void testHasNextTrue() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    assertTrue(parser.hasNext());
    verify(mockReader).peek();
  }

  @Test
    @Timeout(8000)
  void testHasNextFalseAtEndDocument() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
    assertFalse(parser.hasNext());
    verify(mockReader).peek();
  }

  @Test
    @Timeout(8000)
  void testHasNextThrowsMalformedJsonException() throws IOException {
    when(mockReader.peek()).thenThrow(new MalformedJsonException("malformed"));
    assertThrows(JsonSyntaxException.class, () -> parser.hasNext());
  }

  @Test
    @Timeout(8000)
  void testHasNextThrowsIOException() throws IOException {
    when(mockReader.peek()).thenThrow(new IOException("io exception"));
    assertThrows(RuntimeException.class, () -> parser.hasNext());
  }

  @Test
    @Timeout(8000)
  void testNextReturnsJsonElement() throws IOException {
    // We mock Streams.parse(JsonReader) static method to return a JsonPrimitive("value")
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      JsonElement element = new JsonPrimitive("value");
      streamsMockedStatic.when(() -> Streams.parse(mockReader)).thenReturn(element);

      JsonElement result = parser.next();
      assertEquals(element, result);
      streamsMockedStatic.verify(() -> Streams.parse(mockReader));
    }
  }

  @Test
    @Timeout(8000)
  void testNextThrowsNoSuchElementExceptionWhenNoNext() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
    assertThrows(NoSuchElementException.class, () -> parser.next());
  }

  @Test
    @Timeout(8000)
  void testNextThrowsRuntimeExceptionOnIOException() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.parse(mockReader)).thenAnswer(invocation -> {
        throw new IOException("io error");
      });
      RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.next());
      // Check that the cause is IOException
      assertTrue(thrown.getCause() instanceof IOException);
    }
  }

  @Test
    @Timeout(8000)
  void testRemoveThrowsUnsupportedOperationException() {
    assertThrows(UnsupportedOperationException.class, () -> parser.remove());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithStringSetsLenient() throws Exception {
    String json = "{}";
    JsonStreamParser stringParser = new JsonStreamParser(json);
    // Use reflection to get private final parser field
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader internalReader = (JsonReader) parserField.get(stringParser);
    assertNotNull(internalReader);
    // JsonReader#setLenient(true) should be called - no direct way to verify but we can check lenient state by reflection
    Field lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    assertTrue(lenientField.getBoolean(internalReader));
  }
}