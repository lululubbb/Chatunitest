package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class JsonStreamParser_51_5Test {

  private JsonReader mockReader;
  private JsonStreamParser parser;

  @BeforeEach
  public void setUp() {
    mockReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withReader_setsLenientTrue() throws Exception {
    Reader reader = new StringReader("{}");
    JsonStreamParser jsonStreamParser = new JsonStreamParser(reader);

    // Use reflection to access private final field parser and verify lenient is true
    JsonReader internalReader = (JsonReader) getField(jsonStreamParser, "parser");
    assertNotNull(internalReader);
    assertTrue(getLenient(internalReader));
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withString_setsLenientTrue() throws Exception {
    JsonStreamParser jsonStreamParser = new JsonStreamParser("{}");
    JsonReader internalReader = (JsonReader) getField(jsonStreamParser, "parser");
    assertNotNull(internalReader);
    assertTrue(getLenient(internalReader));
  }

  @Test
    @Timeout(8000)
  public void testHasNext_returnsTrueWhenNextTokenIsNotEndDocument() throws Exception {
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

    parser = createWithMockReader(mockReader);

    assertTrue(parser.hasNext());
    verify(mockReader).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_returnsFalseWhenNextTokenIsEndDocument() throws Exception {
    when(mockReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    parser = createWithMockReader(mockReader);

    assertFalse(parser.hasNext());
    verify(mockReader).peek();
  }

  @Test
    @Timeout(8000)
  public void testHasNext_throwsRuntimeExceptionOnIOException() throws Exception {
    when(mockReader.peek()).thenThrow(new IOException("io error"));

    parser = createWithMockReader(mockReader);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.hasNext());
    assertTrue(thrown.getCause() instanceof IOException);
  }

  @Test
    @Timeout(8000)
  public void testNext_returnsJsonElement() throws Exception {
    // Setup mockReader to simulate reading a JSON element
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    doAnswer(invocation -> {
      // Simulate JsonReader reading - no operation needed here
      return null;
    }).when(mockReader).beginObject();

    // We cannot mock Streams.parse as it is static, so we test real behavior with real reader
    String json = "{\"key\":\"value\"}";
    parser = new JsonStreamParser(new StringReader(json));

    assertTrue(parser.hasNext());
    JsonElement element = parser.next();
    assertNotNull(element);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testNext_throwsNoSuchElementExceptionWhenNoNext() throws Exception {
    when(mockReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
    parser = createWithMockReader(mockReader);

    assertFalse(parser.hasNext());
    assertThrows(NoSuchElementException.class, () -> parser.next());
  }

  @Test
    @Timeout(8000)
  public void testNext_throwsJsonParseExceptionOnMalformedJsonException() throws Exception {
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    doThrow(new MalformedJsonException("malformed")).when(mockReader).beginObject();

    parser = createWithMockReader(mockReader);

    assertThrows(JsonParseException.class, () -> parser.next());
  }

  @Test
    @Timeout(8000)
  public void testNext_throwsJsonParseExceptionOnIOException() throws Exception {
    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    doThrow(new IOException("io error")).when(mockReader).beginObject();

    parser = createWithMockReader(mockReader);

    assertThrows(JsonParseException.class, () -> parser.next());
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException() throws Exception {
    parser = createWithMockReader(mockReader);

    assertThrows(UnsupportedOperationException.class, () -> parser.remove());
  }

  // Helper to create JsonStreamParser with mocked JsonReader injected via reflection
  private JsonStreamParser createWithMockReader(JsonReader reader) throws Exception {
    Constructor<JsonStreamParser> constructor = JsonStreamParser.class.getDeclaredConstructor(Reader.class);
    constructor.setAccessible(true);
    JsonStreamParser instance = constructor.newInstance(new StringReader("{}"));
    setField(instance, "parser", reader);
    return instance;
  }

  // Reflection helpers to get/set private fields
  private Object getField(Object object, String fieldName) throws Exception {
    var field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(object);
  }

  private void setField(Object object, String fieldName, Object value) throws Exception {
    var field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(object, value);
  }

  // Reflection helper to get lenient flag from JsonReader (private field)
  private boolean getLenient(JsonReader reader) throws Exception {
    var field = JsonReader.class.getDeclaredField("lenient");
    field.setAccessible(true);
    return field.getBoolean(reader);
  }
}