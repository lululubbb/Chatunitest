package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonStreamParser_53_6Test {

  private JsonStreamParser parser;
  private JsonReader mockJsonReader;
  private Object lockObject;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonStreamParser instance with dummy input
    parser = new JsonStreamParser("{}");
    // Use reflection to replace the private final JsonReader field with a mock
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    mockJsonReader = mock(JsonReader.class);
    parserField.set(parser, mockJsonReader);

    // Access the private final lock field
    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    lockObject = lockField.get(parser);
  }

  @Test
    @Timeout(8000)
  public void testHasNextReturnsTrueWhenNotEndDocument() throws Exception {
    // Setup mock to return a token other than END_DOCUMENT
    when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

    boolean result;
    synchronized (lockObject) {
      result = parser.hasNext();
    }
    assertTrue(result);

    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNextReturnsFalseWhenEndDocument() throws Exception {
    when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    boolean result;
    synchronized (lockObject) {
      result = parser.hasNext();
    }
    assertFalse(result);

    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNextThrowsJsonSyntaxExceptionOnMalformedJsonException() throws Exception {
    when(mockJsonReader.peek()).thenThrow(new MalformedJsonException("malformed"));

    JsonSyntaxException thrown;
    synchronized (lockObject) {
      thrown = assertThrows(JsonSyntaxException.class, () -> parser.hasNext());
    }
    assertNotNull(thrown);
    assertTrue(thrown.getCause() instanceof MalformedJsonException);

    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNextThrowsJsonIOExceptionOnIOException() throws Exception {
    when(mockJsonReader.peek()).thenThrow(new IOException("io error"));

    JsonIOException thrown;
    synchronized (lockObject) {
      thrown = assertThrows(JsonIOException.class, () -> parser.hasNext());
    }
    assertNotNull(thrown);
    assertTrue(thrown.getCause() instanceof IOException);

    verify(mockJsonReader, times(1)).peek();
  }
}