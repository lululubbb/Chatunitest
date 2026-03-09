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
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class JsonStreamParser_50_4Test {

  private static final String VALID_JSON = "{\"key\":\"value\"}";
  private static final String EMPTY_JSON = "";
  private static final String MALFORMED_JSON = "{key:\"value\""; // missing closing }

  private JsonStreamParser jsonStreamParser;

  @Test
    @Timeout(8000)
  public void testConstructor_String_shouldCreateReader() throws Exception {
    JsonStreamParser parser = new JsonStreamParser(VALID_JSON);
    assertNotNull(parser);
    // Use reflection to access private field parser
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(parser);
    assertNotNull(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Reader_shouldSetFields() throws Exception {
    Reader reader = new StringReader(VALID_JSON);
    JsonStreamParser parser = new JsonStreamParser(reader);
    assertNotNull(parser);

    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(parser);
    assertNotNull(jsonReader);

    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    Object lock = lockField.get(parser);
    assertNotNull(lock);
  }

  @Test
    @Timeout(8000)
  public void testHasNext_whenNextTokenIsNotEndDocument_shouldReturnTrue() throws Exception {
    // Mock JsonReader
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

    JsonStreamParser parser = createParserWithMockReader(mockReader);
    assertTrue(parser.hasNext());
    verify(mockReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_whenNextTokenIsEndDocument_shouldReturnFalse() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    JsonStreamParser parser = createParserWithMockReader(mockReader);
    assertFalse(parser.hasNext());
    verify(mockReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_whenIOException_shouldThrowJsonParseException() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenThrow(new IOException("io error"));

    JsonStreamParser parser = createParserWithMockReader(mockReader);

    assertThrows(JsonParseException.class, parser::hasNext);
  }

  @Test
    @Timeout(8000)
  public void testNext_whenHasNextTrue_shouldReturnJsonElement() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    // Mock Streams.parse to return a JsonElement
    JsonElement expectedElement = new JsonPrimitive("value");
    // Spy Streams class to mock static method parse
    // Since Streams.parse is static, we cannot mock easily without PowerMockito
    // Instead, create a minimal JsonReader that returns a JsonElement via Streams.parse
    // So we test next() integration

    // Use real JsonStreamParser with real JsonReader on VALID_JSON
    JsonStreamParser parser = new JsonStreamParser(VALID_JSON);
    assertTrue(parser.hasNext());
    JsonElement element = parser.next();
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testNext_whenHasNextFalse_shouldThrowNoSuchElementException() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    JsonStreamParser parser = createParserWithMockReader(mockReader);

    assertFalse(parser.hasNext());
    assertThrows(NoSuchElementException.class, parser::next);
  }

  @Test
    @Timeout(8000)
  public void testNext_whenMalformedJson_shouldThrowJsonParseException() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    // Simulate Streams.parse throwing MalformedJsonException by throwing it from JsonReader methods
    doThrow(new MalformedJsonException("malformed")).when(mockReader).beginObject();

    JsonStreamParser parser = createParserWithMockReader(mockReader);

    // Because Streams.parse calls parser.beginObject() internally, next() should throw JsonParseException
    assertThrows(JsonParseException.class, parser::next);
  }

  @Test
    @Timeout(8000)
  public void testRemove_shouldThrowUnsupportedOperationException() {
    JsonStreamParser parser = new JsonStreamParser(VALID_JSON);
    assertThrows(UnsupportedOperationException.class, parser::remove);
  }

  // Helper method to create JsonStreamParser with mocked JsonReader injected
  private JsonStreamParser createParserWithMockReader(JsonReader mockReader) throws Exception {
    Constructor<JsonStreamParser> constructor = JsonStreamParser.class.getDeclaredConstructor(Reader.class);
    constructor.setAccessible(true);
    JsonStreamParser parser = constructor.newInstance(new StringReader(VALID_JSON));

    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(parser, mockReader);

    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    lockField.set(parser, new Object());

    return parser;
  }
}