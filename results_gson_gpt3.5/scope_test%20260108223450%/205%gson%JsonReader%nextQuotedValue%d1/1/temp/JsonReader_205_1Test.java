package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_205_1Test {
  private JsonReader jsonReader;
  private Method nextQuotedValueMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    jsonReader = new JsonReader(mock(java.io.Reader.class));
    nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void nextQuotedValue_returnsStringWithoutEscape() throws Throwable {
    // Setup buffer with quoted string: "hello"
    setField("buffer", new char[] {'h', 'e', 'l', 'l', 'o', '"'});
    setField("pos", 0);
    setField("limit", 6);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    String result = invokeNextQuotedValue('"');
    assertEquals("hello", result);
    assertEquals(6, getField("pos"));
  }

  @Test
    @Timeout(8000)
  public void nextQuotedValue_returnsStringWithEscapedChar() throws Throwable {
    // Setup buffer with quoted string: "hel\nlo"
    // buffer: h e l \ n l o "
    // indexes:0 1 2 3 4 5 6 7
    setField("buffer", new char[] {'h', 'e', 'l', '\\', 'n', 'l', 'o', '"'});
    setField("pos", 0);
    setField("limit", 8);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    // We must mock readEscapeCharacter to return '\n'
    JsonReader spyReader = spy(jsonReader);
    doReturn('\n').when(spyReader).readEscapeCharacter();

    String result = (String) nextQuotedValueMethod.invoke(spyReader, '"');
    assertEquals("hel\nlo", result);
    assertEquals(8, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  public void nextQuotedValue_updatesLineNumberOnNewline() throws Throwable {
    // Setup buffer with quoted string containing newline: "he\nllo"
    setField("buffer", new char[] {'h', 'e', '\n', 'l', 'l', 'o', '"'});
    setField("pos", 0);
    setField("limit", 7);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    String result = invokeNextQuotedValue('"');
    assertEquals("he\nllo", result);
    assertEquals(7, getField("pos"));
    assertEquals(1, getField("lineNumber"));
    assertEquals(3, getField("lineStart"));
  }

  @Test
    @Timeout(8000)
  public void nextQuotedValue_throwsOnUnterminatedString() throws Throwable {
    // Setup buffer without closing quote
    setField("buffer", new char[] {'h', 'e', 'l', 'l', 'o'});
    setField("pos", 0);
    setField("limit", 5);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    // Mock fillBuffer to return false to simulate EOF
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      nextQuotedValueMethod.invoke(spyReader, '"');
    });
    assertTrue(thrown.getCause() instanceof IOException);
    assertTrue(thrown.getCause().getMessage().contains("Unterminated string"));
  }

  @Test
    @Timeout(8000)
  public void nextQuotedValue_appendsMultipleSegments() throws Throwable {
    // Setup buffer with partial string and fillBuffer to provide more chars
    // First buffer: "hel\
    // Second buffer after fillBuffer: lo"
    char[] firstBuffer = new char[] {'h', 'e', 'l', '\\'};
    char[] secondBuffer = new char[] {'n', 'l', 'o', '"'};
    setField("buffer", firstBuffer);
    setField("pos", 0);
    setField("limit", 4);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    JsonReader spyReader = spy(jsonReader);
    // readEscapeCharacter returns '\n' when called
    doReturn('\n').when(spyReader).readEscapeCharacter();

    // fillBuffer will update buffer and limit and return true once, then false
    doAnswer(invocation -> {
      setField(spyReader, "buffer", secondBuffer);
      setField(spyReader, "pos", 0);
      setField(spyReader, "limit", 4);
      return true;
    }).doReturn(false).when(spyReader).fillBuffer(1);

    String result = (String) nextQuotedValueMethod.invoke(spyReader, '"');
    assertEquals("hel\nlo", result);
  }

  // Helper methods for reflection field access
  private void setField(String fieldName, Object value) throws Exception {
    setField(jsonReader, fieldName, value);
  }

  private void setField(Object obj, String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }

  private <T> T getField(String fieldName) throws Exception {
    return getField(jsonReader, fieldName);
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object obj, String fieldName) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(obj);
  }

  private String invokeNextQuotedValue(char quote) throws Throwable {
    try {
      return (String) nextQuotedValueMethod.invoke(jsonReader, quote);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}