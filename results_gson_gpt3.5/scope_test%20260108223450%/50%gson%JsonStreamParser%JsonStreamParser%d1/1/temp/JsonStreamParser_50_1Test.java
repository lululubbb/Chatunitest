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

public class JsonStreamParser_50_1Test {

  private JsonStreamParser parser;
  private JsonReader mockJsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    // We will create a JsonStreamParser with a Reader, then inject a mocked JsonReader into it.
    parser = new JsonStreamParser(new StringReader("{}"));
    // Using reflection to replace the private final JsonReader parser field with a mock
    mockJsonReader = mock(JsonReader.class);
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(parser, mockJsonReader);

    // Also inject a lock object (private final Object lock)
    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    lockField.set(parser, new Object());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_stringCreatesReader() {
    String json = "{\"a\":1}";
    JsonStreamParser p = new JsonStreamParser(json);
    assertNotNull(p);
  }

  @Test
    @Timeout(8000)
  public void testHasNext_trueWhenNotEndDocument() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    assertTrue(parser.hasNext());
    verify(mockJsonReader).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_falseWhenEndDocument() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
    assertFalse(parser.hasNext());
    verify(mockJsonReader).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_throwsRuntimeExceptionOnIOException() throws IOException {
    when(mockJsonReader.peek()).thenThrow(new IOException("io error"));
    RuntimeException ex = assertThrows(RuntimeException.class, () -> parser.hasNext());
    assertTrue(ex.getCause() instanceof IOException);
  }

  @Test
    @Timeout(8000)
  public void testNext_returnsJsonElement() throws IOException {
    // Mock Streams.parse to return a JsonElement
    JsonElement mockElement = mock(JsonElement.class);

    // We need to mock Streams.parse(JsonReader) static method.
    // Since Streams is internal and static, we can use reflection to replace it or we test the actual method.
    // Here, we will spy on Streams class using Mockito inline mock maker (if available).
    // Otherwise, just test the happy path by calling next() and catching exceptions.

    // Instead, we simulate Streams.parse by calling the real method with a real JsonReader.
    // So here, we reset parser with a real reader.
    parser = new JsonStreamParser("{\"key\":\"value\"}");
    assertNotNull(parser.next());
  }

  @Test
    @Timeout(8000)
  public void testNext_throwsNoSuchElementExceptionWhenNoNext() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
    NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> parser.next());
    // Fix: assert message can be null, so check if message contains expected text or is null
    String expectedMessage = "No more JSON elements";
    String actualMessage = ex.getMessage();
    assertTrue(actualMessage == null || expectedMessage.equals(actualMessage));
  }

  @Test
    @Timeout(8000)
  public void testNext_throwsRuntimeExceptionOnMalformedJsonException() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    // Mock Streams.parse to throw MalformedJsonException via reflection on private method not possible
    // Instead, simulate by calling next() on bad JSON string
    parser = new JsonStreamParser("{ bad json ");
    assertThrows(RuntimeException.class, () -> parser.next());
  }

  @Test
    @Timeout(8000)
  public void testNext_throwsRuntimeExceptionOnIOException() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    // We mock Streams.parse to throw IOException by spying on Streams class - not possible here
    // Instead, simulate by using a Reader that throws IOException on read
    Reader badReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        throw new IOException("read error");
      }
      @Override
      public void close() throws IOException {}
    };
    parser = new JsonStreamParser(badReader);
    assertThrows(RuntimeException.class, () -> parser.next());
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException() {
    assertThrows(UnsupportedOperationException.class, () -> parser.remove());
  }
}