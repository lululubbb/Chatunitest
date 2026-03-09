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

public class JsonReader_206_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_simpleLiteral() throws Throwable {
    // Setup buffer with "true," and pos=0, limit=5
    setField(jsonReader, "buffer", "true,".toCharArray());
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 5);
    setField(jsonReader, "lenient", false);

    String result = invokeNextUnquotedValue(jsonReader);

    assertEquals("true", result);
    assertEquals(4, getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_literalWithWhitespace() throws Throwable {
    setField(jsonReader, "buffer", "null \n".toCharArray());
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 5);
    setField(jsonReader, "lenient", false);

    String result = invokeNextUnquotedValue(jsonReader);

    assertEquals("null", result);
    assertEquals(4, getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_withLenientCheck() throws Throwable {
    setField(jsonReader, "buffer", "foo#bar".toCharArray());
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 7);
    setField(jsonReader, "lenient", true);

    // We spy jsonReader to verify checkLenient() is called
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "buffer", "foo#bar".toCharArray());
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 7);
    setField(spyReader, "lenient", true);

    String result = invokeNextUnquotedValue(spyReader);

    assertEquals("foo", result);
    assertEquals(3, getField(spyReader, "pos"));
    verify(spyReader, atLeastOnce()).checkLenient();
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_builderUsage() throws Throwable {
    // Prepare a buffer that requires multiple fillBuffer calls and uses builder
    // We'll simulate fillBuffer to return true once and then false
    char[] largeBuffer = new char[1024];
    for (int i = 0; i < 1024; i++) {
      largeBuffer[i] = 'a';
    }
    setField(jsonReader, "buffer", largeBuffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 1024);
    setField(jsonReader, "lenient", false);

    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "buffer", largeBuffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 1024);
    setField(spyReader, "lenient", false);

    doReturn(true).doReturn(false).when(spyReader).fillBuffer(anyInt());

    String result = invokeNextUnquotedValue(spyReader);

    assertNotNull(result);
    assertTrue(result.length() >= 1024);
    assertEquals(1024, getField(spyReader, "pos"));
    verify(spyReader, times(2)).fillBuffer(anyInt());
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_breaksOnSpecialCharacters() throws Throwable {
    String testString = "abc:def";
    setField(jsonReader, "buffer", testString.toCharArray());
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", testString.length());
    setField(jsonReader, "lenient", false);

    String result = invokeNextUnquotedValue(jsonReader);

    assertEquals("abc", result);
    assertEquals(3, getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_emptyBuffer() throws Throwable {
    setField(jsonReader, "buffer", "".toCharArray());
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lenient", false);

    // fillBuffer returns false to simulate EOF
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());

    String result = invokeNextUnquotedValue(spyReader);

    assertEquals("", result);
    assertEquals(0, getField(spyReader, "pos"));
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> T getField(Object target, String fieldName) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      @SuppressWarnings("unchecked")
      T value = (T) field.get(target);
      return value;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String invokeNextUnquotedValue(JsonReader reader) throws Throwable {
    try {
      Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
      method.setAccessible(true);
      return (String) method.invoke(reader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}