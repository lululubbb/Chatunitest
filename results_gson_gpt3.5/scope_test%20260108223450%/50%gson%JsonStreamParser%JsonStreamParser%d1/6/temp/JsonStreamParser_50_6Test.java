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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonStreamParser_50_6Test {

  JsonStreamParser parser;

  @BeforeEach
  public void setUp() {
    // no setup needed here
  }

  @Test
    @Timeout(8000)
  public void testConstructor_String_validJson() {
    String json = "{\"key\":\"value\"}";
    JsonStreamParser p = new JsonStreamParser(json);
    assertNotNull(p);
    assertTrue(p.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Reader_validJson() {
    Reader reader = new StringReader("[1,2,3]");
    JsonStreamParser p = new JsonStreamParser(reader);
    assertNotNull(p);
    assertTrue(p.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testHasNext_trueAndFalse() throws Exception {
    String json = "{\"key\":\"value\"}";
    JsonStreamParser p = new JsonStreamParser(json);
    assertTrue(p.hasNext());
    p.next();
    assertFalse(p.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testNext_returnsJsonElement() {
    String json = "{\"key\":\"value\"}";
    JsonStreamParser p = new JsonStreamParser(json);
    JsonElement element = p.next();
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testNext_noSuchElementException() {
    String json = "{}";
    JsonStreamParser p = new JsonStreamParser(json);
    p.next();
    assertThrows(NoSuchElementException.class, p::next);
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException() {
    String json = "{}";
    JsonStreamParser p = new JsonStreamParser(json);
    assertThrows(UnsupportedOperationException.class, p::remove);
  }

  @Test
    @Timeout(8000)
  public void testNext_malformedJsonException() throws Exception {
    String malformedJson = "{";
    JsonStreamParser p = new JsonStreamParser(malformedJson);
    // Using reflection to get private field 'parser'
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader reader = (JsonReader) parserField.get(p);
    // Move reader to simulate malformed JSON causing MalformedJsonException on peek
    // But since the real JsonReader will throw MalformedJsonException on next(), we can test that
    assertThrows(JsonParseException.class, p::next);
  }

  @Test
    @Timeout(8000)
  public void testHasNext_ioExceptionWrapped() throws Exception {
    // Create a mock JsonReader that throws IOException on peek
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenThrow(new IOException("IO error"));

    // Use reflection to create JsonStreamParser with mockReader
    Constructor<JsonStreamParser> ctor = JsonStreamParser.class.getDeclaredConstructor(Reader.class);
    ctor.setAccessible(true);
    JsonStreamParser p = ctor.newInstance(new StringReader("{}"));
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(p, mockReader);

    // hasNext should wrap IOException in JsonParseException
    assertThrows(JsonParseException.class, p::hasNext);
  }

  @Test
    @Timeout(8000)
  public void testNext_ioExceptionWrapped() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    doThrow(new IOException("IO error")).when(mockReader).beginObject();

    Constructor<JsonStreamParser> ctor = JsonStreamParser.class.getDeclaredConstructor(Reader.class);
    ctor.setAccessible(true);
    JsonStreamParser p = ctor.newInstance(new StringReader("{}"));
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(p, mockReader);

    // next should wrap IOException in JsonParseException
    assertThrows(JsonParseException.class, p::next);
  }
}