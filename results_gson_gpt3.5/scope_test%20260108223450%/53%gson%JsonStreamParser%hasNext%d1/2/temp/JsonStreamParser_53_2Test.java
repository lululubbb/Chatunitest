package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonStreamParser_53_2Test {

  private JsonStreamParser jsonStreamParser;
  private JsonReader mockJsonReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a dummy JsonStreamParser instance with a dummy String constructor
    jsonStreamParser = new JsonStreamParser("");

    // Create a mock JsonReader
    mockJsonReader = mock(JsonReader.class);

    // Use reflection to set the private final parser field to the mock
    Field parserField = JsonStreamParser.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(jsonStreamParser, mockJsonReader);

    // Use reflection to set the private final lock field to a new Object
    Field lockField = JsonStreamParser.class.getDeclaredField("lock");
    lockField.setAccessible(true);
    lockField.set(jsonStreamParser, new Object());
  }

  @Test
    @Timeout(8000)
  void hasNext_returnsTrue_whenParserPeekIsNotEndDocument() throws Exception {
    when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

    boolean result = jsonStreamParser.hasNext();

    assertTrue(result);
    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void hasNext_returnsFalse_whenParserPeekIsEndDocument() throws Exception {
    when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    boolean result = jsonStreamParser.hasNext();

    assertFalse(result);
    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void hasNext_throwsJsonSyntaxException_whenMalformedJsonExceptionThrown() throws Exception {
    when(mockJsonReader.peek()).thenThrow(new MalformedJsonException("malformed"));

    JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> {
      jsonStreamParser.hasNext();
    });

    assertTrue(exception.getCause() instanceof MalformedJsonException);
    verify(mockJsonReader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void hasNext_throwsJsonIOException_whenIOExceptionThrown() throws Exception {
    when(mockJsonReader.peek()).thenThrow(new IOException("io error"));

    JsonIOException exception = assertThrows(JsonIOException.class, () -> {
      jsonStreamParser.hasNext();
    });

    assertTrue(exception.getCause() instanceof IOException);
    verify(mockJsonReader, times(1)).peek();
  }
}