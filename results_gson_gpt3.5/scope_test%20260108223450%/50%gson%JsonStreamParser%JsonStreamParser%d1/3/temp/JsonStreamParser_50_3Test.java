package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class JsonStreamParser_50_3Test {

  private JsonStreamParser jsonStreamParser;

  @BeforeEach
  public void setUp() {
    // Using the constructor with String input for normal initialization
    jsonStreamParser = new JsonStreamParser("{\"key\":\"value\"}");
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithString_createsReader() throws Exception {
    String json = "{\"a\":1}";
    JsonStreamParser parser = new JsonStreamParser(json);

    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(parser);

    assertNotNull(jsonReader);
    // The JsonReader should be reading from a StringReader with the given JSON string
    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    Reader reader = (Reader) inField.get(jsonReader);
    assertTrue(reader instanceof StringReader);
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithReader_setsLockAndParser() throws Exception {
    Reader mockReader = mock(Reader.class);
    JsonStreamParser parser = new JsonStreamParser(mockReader);

    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    Object lock = lockField.get(parser);
    assertNotNull(lock);

    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(parser);
    assertNotNull(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testHasNext_trueAndFalse() throws Exception {
    // Create a spy to mock JsonReader behavior
    Reader reader = new StringReader("{\"key\":\"value\"}");
    JsonStreamParser parser = new JsonStreamParser(reader);

    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(parser);

    // Spy on the JsonReader to control peek() behavior
    JsonReader spyReader = spy(jsonReader);

    // Replace the parser field with spyReader
    parserField.set(parser, spyReader);

    when(spyReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT).thenReturn(JsonToken.END_DOCUMENT);

    assertTrue(parser.hasNext());
    assertFalse(parser.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testHasNext_throwsMalformedJsonException() throws Exception {
    Reader reader = new StringReader("");
    JsonStreamParser parser = new JsonStreamParser(reader);

    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(parser);

    JsonReader spyReader = spy(jsonReader);
    parserField.set(parser, spyReader);

    when(spyReader.peek()).thenThrow(new MalformedJsonException("malformed"));

    assertThrows(com.google.gson.JsonSyntaxException.class, parser::hasNext);
  }

  @Test
    @Timeout(8000)
  public void testNext_returnsJsonElement() throws Exception {
    JsonStreamParser parser = new JsonStreamParser("{\"key\":\"value\"}");
    assertTrue(parser.hasNext());
    JsonElement element = parser.next();
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testNext_noSuchElementException() throws Exception {
    JsonStreamParser parser = new JsonStreamParser("");
    assertFalse(parser.hasNext());
    assertThrows(NoSuchElementException.class, parser::next);
  }

  @Test
    @Timeout(8000)
  public void testNext_throwsJsonParseExceptionOnIOException() throws Exception {
    Reader mockReader = mock(Reader.class);
    JsonStreamParser parser = new JsonStreamParser(mockReader);

    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(parser);

    JsonReader spyReader = spy(jsonReader);
    parserField.set(parser, spyReader);

    when(spyReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    // Mock Streams.parse to throw IOException by spying on Streams class is complicated,
    // so instead we simulate IOException by making JsonReader throw on peek or next.

    doThrow(new IOException("io exception")).when(spyReader).peek();

    assertThrows(com.google.gson.JsonParseException.class, parser::next);
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException() {
    assertThrows(UnsupportedOperationException.class, () -> jsonStreamParser.remove());
  }
}