package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_214_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    jsonReader = new JsonReader(mock(Reader.class));
  }

  private int invokeNextNonWhitespace(JsonReader reader, boolean throwOnEof)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    return (int) method.invoke(reader, throwOnEof);
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_returnsChar_skipsWhitespaceAndNewlines() throws Exception {
    // Setup buffer with whitespace, newline, then a non-whitespace char 'a'
    char[] buffer = new char[] {' ', '\r', '\n', '\t', 'a'};
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    int result = invokeNextNonWhitespace(jsonReader, true);

    assertEquals('a', result);
    assertEquals(4, getField(jsonReader, "pos"));
    assertEquals(1, getField(jsonReader, "lineNumber")); // incremented due to '\n'
    assertEquals(4, getField(jsonReader, "lineStart")); // updated to pos after '\n'
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_returnsMinusOne_whenEofAndThrowOnEofFalse() throws Exception {
    // Setup empty buffer and fillBuffer returns false to simulate EOF
    setField(jsonReader, "buffer", new char[JsonReader.BUFFER_SIZE]);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    int result = invokeNextNonWhitespace(spyReader, false);

    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_throwsEOFException_whenEofAndThrowOnEofTrue() throws Exception {
    setField(jsonReader, "buffer", new char[JsonReader.BUFFER_SIZE]);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);
    doReturn(" at line 0 column 0 path $").when(spyReader).locationString();

    EOFException thrown = assertThrows(EOFException.class, () -> {
      invokeNextNonWhitespace(spyReader, true);
    });
    assertTrue(thrown.getMessage().contains("End of input"));
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_skipsSingleLineComment() throws Exception {
    // buffer: '/' '/' 'a'
    char[] buffer = new char[] {'/', '/', 'a'};
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lenient", true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();
    doNothing().when(spyReader).skipToEndOfLine();

    int result = invokeNextNonWhitespace(spyReader, true);

    // After skipping comment, pos should be at 2 and next char returned is 'a'
    assertEquals('a', result);
    assertEquals(2, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_skipsMultiLineComment() throws Exception {
    // buffer: '/' '*' 'a' '*' '/' 'b'
    char[] buffer = new char[] {'/', '*', 'a', '*', '/', 'b'};
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lenient", true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();
    doReturn(true).when(spyReader).skipTo("*/");

    int result = invokeNextNonWhitespace(spyReader, true);

    // After skipping comment, pos should be after "*/" (pos + 2)
    assertEquals('b', result);
    assertEquals(6, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_throwsSyntaxError_whenUnterminatedMultiLineComment() throws Exception {
    // buffer: '/' '*' 'a'
    char[] buffer = new char[] {'/', '*', 'a'};
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lenient", true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();
    doReturn(false).when(spyReader).skipTo("*/");

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      invokeNextNonWhitespace(spyReader, true);
    });
    assertTrue(ex.getCause() instanceof IOException);
    assertTrue(ex.getCause().getMessage().contains("Unterminated comment"));
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_skipsHashComment() throws Exception {
    // buffer: '#' 'a'
    char[] buffer = new char[] {'#', 'a'};
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lenient", true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();
    doNothing().when(spyReader).skipToEndOfLine();

    int result = invokeNextNonWhitespace(spyReader, true);

    assertEquals('a', result);
    assertEquals(2, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_returnsSlash_whenSlashNotFollowedByComment() throws Exception {
    // buffer: '/' 'a'
    char[] buffer = new char[] {'/', 'a'};
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);
    setField(jsonReader, "lenient", true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();

    int result = invokeNextNonWhitespace(spyReader, true);

    assertEquals('/', result);
    assertEquals(1, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_fillBufferCalledWhenPosEqualsLimit() throws Exception {
    char[] buffer = new char[] {'a'};
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 1);
    setField(jsonReader, "limit", 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(1);
    // When fillBuffer called, pos and limit updated to 0 and 1 respectively
    doAnswer(invocation -> {
      setField(spyReader, "pos", 0);
      setField(spyReader, "limit", 1);
      return true;
    }).when(spyReader).fillBuffer(1);

    int result = invokeNextNonWhitespace(spyReader, true);

    assertEquals('a', result);
  }

  // Helper methods to set and get private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T getField(Object target, String fieldName) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      @SuppressWarnings("unchecked")
      T value = (T) field.get(target);
      return value;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}