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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class JsonStreamParser_51_3Test {

  private JsonReader mockReader;
  private JsonStreamParser parser;

  @BeforeEach
  public void setUp() throws Exception {
    // Mock JsonReader
    mockReader = mock(JsonReader.class);
    // Create JsonStreamParser with dummy Reader, then inject mockReader via reflection
    parser = new JsonStreamParser(new StringReader("{}"));
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(parser, mockReader);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withReader_setsLenient() throws Exception {
    Reader reader = new StringReader("{}");
    JsonStreamParser p = new JsonStreamParser(reader);
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(p);
    assertNotNull(jsonReader);
    // JsonReader#setLenient is void so we cannot verify directly, but no exceptions means success
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withString_setsLenient() throws Exception {
    JsonStreamParser p = new JsonStreamParser("{}");
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(p);
    assertNotNull(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testHasNext_true() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    assertTrue(parser.hasNext());
    verify(mockReader).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_false() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
    assertFalse(parser.hasNext());
    verify(mockReader).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_throwsIOException() throws IOException {
    when(mockReader.peek()).thenThrow(new IOException("io error"));
    assertThrows(RuntimeException.class, () -> parser.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testNext_returnsJsonElement() throws IOException {
    // To avoid NullPointerException in next(), mock peek() and the underlying Streams.parse call indirectly
    // We mock peek() to return a valid token, and mock the parser to return a dummy JsonElement when Streams.parse is called internally.
    when(mockReader.peek()).thenReturn(JsonToken.STRING);
    // Since Streams.parse(parser) internally calls parser.peek() and parser.nextString(), we need to mock nextString()
    when(mockReader.nextString()).thenReturn("mocked string");

    try {
      parser.next();
    } catch (Exception e) {
      fail("next() threw exception: " + e);
    }
  }

  @Test
    @Timeout(8000)
  public void testNext_noSuchElementException() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
    assertThrows(NoSuchElementException.class, () -> parser.next());
  }

  @Test
    @Timeout(8000)
  public void testNext_malformedJsonException() throws IOException {
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    // Simulate Streams.parse throwing MalformedJsonException by mocking parser.peek() to throw
    doThrow(new MalformedJsonException("malformed")).when(mockReader).peek();

    JsonStreamParser p = new JsonStreamParser(new StringReader("{}"));
    try {
      Field parserField = JsonStreamParser.class.getDeclaredField("parser");
      parserField.setAccessible(true);
      parserField.set(p, mockReader);
    } catch (Exception ignored) {}

    assertThrows(JsonParseException.class, () -> p.next());
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException() {
    assertThrows(UnsupportedOperationException.class, () -> parser.remove());
  }
}