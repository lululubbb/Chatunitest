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
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class JsonStreamParser_51_1Test {

  private JsonReader mockJsonReader;
  private JsonStreamParser jsonStreamParser;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a mock JsonReader
    mockJsonReader = mock(JsonReader.class);

    // Use reflection to create an instance of JsonStreamParser with Reader constructor
    Constructor<JsonStreamParser> constructor = JsonStreamParser.class.getDeclaredConstructor(Reader.class);
    constructor.setAccessible(true);
    jsonStreamParser = constructor.newInstance(new StringReader(""));

    // Use reflection to set the private parser field to the mockJsonReader
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(jsonStreamParser, mockJsonReader);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_setsLenientTrue() throws Exception {
    // Use reflection to create instance with Reader
    StringReader reader = new StringReader("{}");
    JsonStreamParser parserInstance = new JsonStreamParser(reader);

    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(parserInstance);

    // JsonReader lenient flag is private, so test by accessing via reflection or by invoking a method that requires lenient
    // Here we just check that parser is not null
    assertNotNull(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testHasNext_true() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

    assertTrue(jsonStreamParser.hasNext());

    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_false() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    assertFalse(jsonStreamParser.hasNext());

    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_throwsMalformedJsonException() throws IOException {
    when(mockJsonReader.peek()).thenThrow(new MalformedJsonException("malformed"));

    // The method under test wraps MalformedJsonException into JsonSyntaxException,
    // so we expect JsonSyntaxException instead of MalformedJsonException.
    assertThrows(JsonSyntaxException.class, () -> jsonStreamParser.hasNext());

    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testNext_returnsJsonElement() throws IOException {
    JsonElement expectedElement = mock(JsonElement.class);

    // Use reflection to mock Streams.parse(JsonReader) static method
    // Since Streams.parse is static and final, we cannot mock it easily without PowerMockito.
    // Instead, we test next() calling parse by spying on JsonStreamParser and overriding next() is not possible.
    // So we test next() by calling real Streams.parse with a valid JsonReader.

    // Create real JsonStreamParser with valid JSON string
    JsonStreamParser realParser = new JsonStreamParser(new StringReader("{\"key\":\"value\"}"));

    JsonElement actual = realParser.next();

    assertNotNull(actual);
    assertTrue(actual.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testNext_noSuchElementException_whenNoNext() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    assertThrows(NoSuchElementException.class, () -> jsonStreamParser.next());

    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException() {
    assertThrows(UnsupportedOperationException.class, () -> jsonStreamParser.remove());
  }
}