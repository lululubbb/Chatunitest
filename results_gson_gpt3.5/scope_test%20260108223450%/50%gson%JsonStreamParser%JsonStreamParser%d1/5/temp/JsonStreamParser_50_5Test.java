package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class JsonStreamParser_50_5Test {

  private static final String VALID_JSON = "{\"key\":\"value\"}";
  private static final String EMPTY_JSON = "null"; // Removed newline to avoid extra token
  private static final String MALFORMED_JSON = "{key:\"value\"";

  @Test
    @Timeout(8000)
  public void testConstructorWithString() throws Exception {
    JsonStreamParser p = new JsonStreamParser(VALID_JSON);
    assertNotNull(p);
    // Use reflection to check that parser field is non-null
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    Object jsonReader = parserField.get(p);
    assertNotNull(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithReader() throws Exception {
    Reader reader = new StringReader(VALID_JSON);
    JsonStreamParser p = new JsonStreamParser(reader);
    assertNotNull(p);
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    Object jsonReader = parserField.get(p);
    assertNotNull(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testHasNextTrue() throws Exception {
    JsonStreamParser p = new JsonStreamParser(VALID_JSON);
    assertTrue(p.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testHasNextFalse() throws Exception {
    JsonStreamParser p = new JsonStreamParser(EMPTY_JSON);
    // Consume the 'null' token so hasNext returns false
    while (p.hasNext()) {
      p.next();
    }
    assertFalse(p.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testNextReturnsJsonElement() {
    JsonStreamParser p = new JsonStreamParser(VALID_JSON);
    assertTrue(p.hasNext());
    JsonElement element = p.next();
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsNoSuchElementExceptionWhenNoNext() {
    JsonStreamParser p = new JsonStreamParser(EMPTY_JSON);
    // Consume the 'null' token to reach end
    while (p.hasNext()) {
      p.next();
    }
    assertFalse(p.hasNext());
    assertThrows(NoSuchElementException.class, p::next);
  }

  @Test
    @Timeout(8000)
  public void testRemoveThrowsUnsupportedOperationException() {
    JsonStreamParser p = new JsonStreamParser(VALID_JSON);
    assertThrows(UnsupportedOperationException.class, p::remove);
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsMalformedJsonException() throws Exception {
    Reader reader = spy(new StringReader(MALFORMED_JSON));
    JsonStreamParser p = new JsonStreamParser(reader);
    // Use reflection to get the parser field (JsonReader) and spy it to throw MalformedJsonException on peek
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(p);
    JsonReader spyReader = spy(jsonReader);
    doThrow(new MalformedJsonException("Malformed")).when(spyReader).peek();
    parserField.set(p, spyReader);

    // The next() method wraps MalformedJsonException inside JsonSyntaxException,
    // so expecting JsonSyntaxException here
    assertThrows(com.google.gson.JsonSyntaxException.class, p::next);
  }

  @Test
    @Timeout(8000)
  public void testNextThrowsIOException() throws Exception {
    Reader reader = spy(new StringReader(VALID_JSON));
    JsonStreamParser p = new JsonStreamParser(reader);
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    JsonReader jsonReader = (JsonReader) parserField.get(p);
    JsonReader spyReader = spy(jsonReader);
    doThrow(new IOException("IO error")).when(spyReader).peek();
    parserField.set(p, spyReader);

    assertThrows(RuntimeException.class, p::next);
  }
}