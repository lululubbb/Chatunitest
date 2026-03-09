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

class JsonReader_206_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private String invokeNextUnquotedValue() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    try {
      return (String) method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      // unwrap IOException thrown by nextUnquotedValue
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_simpleLiteral() throws Exception {
    // Setup buffer with unquoted value terminated by a delimiter (comma)
    String value = "unquotedValue";
    char[] buffer = new char[1024];
    System.arraycopy(value.toCharArray(), 0, buffer, 0, value.length());
    buffer[value.length()] = ',';
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", value.length() + 1);

    String result = invokeNextUnquotedValue();

    assertEquals(value, result);
    assertEquals(value.length() + 1, getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_withLenientCheckLenientTriggers() throws Exception {
    // Setup buffer with a character that triggers checkLenient: '/'
    char[] buffer = new char[1024];
    String value = "abc/def";
    System.arraycopy(value.toCharArray(), 0, buffer, 0, value.length());
    buffer[value.length()] = ',';
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", value.length() + 1);

    // Spy on jsonReader to verify checkLenient() is called
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", value.length() + 1);

    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    method.invoke(spyReader);

    verify(spyReader, atLeastOnce()).checkLenient();
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_bufferFillBufferReturnsTrue() throws Exception {
    // Setup buffer full, pos + i == limit, fillBuffer returns true once then false
    char[] buffer = new char[16];
    String part1 = "abcdefghijklmnop";
    System.arraycopy(part1.toCharArray(), 0, buffer, 0, part1.length());
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 16);

    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 16);

    doReturn(true).doReturn(false).when(spyReader).fillBuffer(anyInt());

    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    String result = (String) method.invoke(spyReader);

    assertNotNull(result);
    assertTrue(result.length() >= 16);
    verify(spyReader, times(2)).fillBuffer(anyInt());
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_builderAppendsMultipleTimes() throws Exception {
    // Setup buffer so that literal is longer than buffer length forcing StringBuilder usage
    char[] buffer = new char[8];
    String part1 = "abcdefgh";
    System.arraycopy(part1.toCharArray(), 0, buffer, 0, part1.length());
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 8);

    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 8);

    // fillBuffer returns true once to simulate more data, then false
    doReturn(true).doReturn(false).when(spyReader).fillBuffer(anyInt());

    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    String result = (String) method.invoke(spyReader);

    assertNotNull(result);
    assertTrue(result.length() >= 8);
    verify(spyReader, atLeast(2)).fillBuffer(anyInt());
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_emptyBufferReturnsEmptyString() throws Exception {
    setField(jsonReader, "buffer", new char[1024]);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);

    // fillBuffer returns false immediately simulating EOF
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "buffer", new char[1024]);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 0);
    doReturn(false).when(spyReader).fillBuffer(anyInt());

    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    String result = (String) method.invoke(spyReader);

    assertEquals("", result);
    assertEquals(0, getField(spyReader, "pos"));
  }

  // Helper methods to set and get private fields via reflection
  private static void setField(Object target, String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static <T> T getField(Object target, String fieldName) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    T value = (T) field.get(target);
    return value;
  }
}