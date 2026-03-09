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

class JsonReader_223_4Test {

  private JsonReader jsonReader;
  private Method readEscapeCharacterMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    jsonReader = new JsonReader(mock(java.io.Reader.class));
    readEscapeCharacterMethod = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    readEscapeCharacterMethod.setAccessible(true);
  }

  private char invokeReadEscapeCharacter() throws Throwable {
    try {
      return (char) readEscapeCharacterMethod.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void setField(String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ThrowsUnterminatedEscapeSequenceWhenBufferEmpty() throws Throwable {
    setField("pos", 0);
    setField("limit", 0);
    // Override fillBuffer to return false
    setField("buffer", new char[1024]);
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);
    // Replace jsonReader with spyReader for method invocation
    readEscapeCharacterMethod.setAccessible(true);
    try {
      readEscapeCharacterMethod.invoke(spyReader);
      fail("Expected IOException");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IOException);
      assertEquals("Unterminated escape sequence", e.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ReturnsUnicodeChar() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'u';
    buffer[1] = '0';
    buffer[2] = '0';
    buffer[3] = '4';
    buffer[4] = '1'; // Unicode 0x0041 = 'A'
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 5);
    // fillBuffer returns true if needed
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(4);

    // Invoke on spyReader
    char result;
    try {
      var method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
      method.setAccessible(true);
      result = (char) method.invoke(spyReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
    assertEquals('A', result);
    assertEquals(5, spyReader.pos);
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ThrowsNumberFormatExceptionOnInvalidUnicode() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'u';
    buffer[1] = '0';
    buffer[2] = '0';
    buffer[3] = 'G'; // Invalid hex char
    buffer[4] = '1';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 5);
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(4);

    try {
      var method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
      method.setAccessible(true);
      method.invoke(spyReader);
      fail("Expected NumberFormatException");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof NumberFormatException);
      assertEquals("\\u001G1", e.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ReturnsTab() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 't';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);
    char result = invokeReadEscapeCharacter();
    assertEquals('\t', result);
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ReturnsBackspace() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'b';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);
    char result = invokeReadEscapeCharacter();
    assertEquals('\b', result);
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ReturnsNewline() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'n';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);
    char result = invokeReadEscapeCharacter();
    assertEquals('\n', result);
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ReturnsCarriageReturn() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'r';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);
    char result = invokeReadEscapeCharacter();
    assertEquals('\r', result);
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ReturnsFormFeed() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'f';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);
    char result = invokeReadEscapeCharacter();
    assertEquals('\f', result);
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ReturnsEscapedNewlineIncrementsLineNumber() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = '\n';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);
    setField("lineNumber", 5);
    setField("lineStart", 3);
    char result = invokeReadEscapeCharacter();
    assertEquals('\n', result);
    assertEquals(6, jsonReader.lineNumber);
    assertEquals(1, jsonReader.lineStart);
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ReturnsQuoteBackslashSlash() throws Throwable {
    char[] buffer = new char[1024];
    char[] escapes = {'\'', '"', '\\', '/'};
    for (char c : escapes) {
      buffer[0] = c;
      setField("buffer", buffer);
      setField("pos", 0);
      setField("limit", 1);
      char result = invokeReadEscapeCharacter();
      assertEquals(c, result);
    }
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_ThrowsSyntaxErrorOnInvalidEscape() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'x';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);

    try {
      invokeReadEscapeCharacter();
      fail("Expected IOException");
    } catch (IOException e) {
      assertEquals("Invalid escape sequence", e.getMessage());
    }
  }
}