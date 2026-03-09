package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_207_2Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object getField(Object target, String fieldName) throws Exception {
    var field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private void invokeSkipQuotedValue(char quote) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    try {
      method.invoke(jsonReader, quote);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_terminatesOnQuote() throws Throwable {
    // buffer with quoted string: "abc"
    char quote = '"';
    char[] buffer = new char[] { 'a', 'b', 'c', quote };
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    // fillBuffer returns false (no refill needed)
    setField(jsonReader, "in", mockReader);
    // Spy jsonReader to mock fillBuffer and readEscapeCharacter
    JsonReader spyReader = spy(jsonReader);
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);

    doReturn(false).when(spyReader).fillBuffer(1);
    doNothing().when(spyReader).readEscapeCharacter();

    // Set spyReader buffer, pos, limit fields same as jsonReader
    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buffer.length);
    setField(spyReader, "lineNumber", 0);
    setField(spyReader, "lineStart", 0);

    method.invoke(spyReader, quote);

    int pos = (int) getField(spyReader, "pos");
    assertEquals(buffer.length, pos, "pos should be after the closing quote");
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_handlesEscapeCharacter() throws Throwable {
    // buffer with quoted string containing escape: "ab\c"
    char quote = '"';
    // 'a', 'b', '\\', 'c', '"'
    char[] buffer = new char[] { 'a', 'b', '\\', 'c', quote };
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = spy(jsonReader);
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);

    // On fillBuffer, return false (no refill)
    doReturn(false).when(spyReader).fillBuffer(1);

    // readEscapeCharacter advances pos by 1 for test
    doAnswer(invocation -> {
      int pos = (int) getField(spyReader, "pos");
      setField(spyReader, "pos", pos + 1);
      return null;
    }).when(spyReader).readEscapeCharacter();

    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buffer.length);
    setField(spyReader, "lineNumber", 0);
    setField(spyReader, "lineStart", 0);

    method.invoke(spyReader, quote);

    int pos = (int) getField(spyReader, "pos");
    assertEquals(buffer.length, pos, "pos should be after the closing quote");
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_handlesNewlineUpdatesLineNumberAndLineStart() throws Throwable {
    char quote = '"';
    // buffer with newline before closing quote: a \n "
    char[] buffer = new char[] { 'a', '\n', quote };
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lineNumber", 1);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = spy(jsonReader);
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);

    doReturn(false).when(spyReader).fillBuffer(1);
    doNothing().when(spyReader).readEscapeCharacter();

    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buffer.length);
    setField(spyReader, "lineNumber", 1);
    setField(spyReader, "lineStart", 0);

    method.invoke(spyReader, quote);

    int lineNumber = (int) getField(spyReader, "lineNumber");
    int lineStart = (int) getField(spyReader, "lineStart");
    int pos = (int) getField(spyReader, "pos");

    assertEquals(2, lineNumber, "lineNumber should be incremented on newline");
    assertEquals(2, lineStart, "lineStart should be updated to position after newline");
    assertEquals(buffer.length, pos, "pos should be after the closing quote");
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_throwsOnUnterminatedString() throws Throwable {
    char quote = '"';
    char[] buffer = new char[] { 'a', 'b', 'c' };
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = spy(jsonReader);
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);

    // fillBuffer returns false, simulating no more input
    doReturn(false).when(spyReader).fillBuffer(1);
    doNothing().when(spyReader).readEscapeCharacter();

    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buffer.length);
    setField(spyReader, "lineNumber", 0);
    setField(spyReader, "lineStart", 0);

    IOException thrown = assertThrows(IOException.class, () -> method.invoke(spyReader, quote));
    // InvocationTargetException wraps IOException, unwrap it
    assertTrue(thrown.getMessage().contains("Unterminated string"));
  }
}