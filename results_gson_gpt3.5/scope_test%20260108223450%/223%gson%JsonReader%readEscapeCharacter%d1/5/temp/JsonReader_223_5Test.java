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

class JsonReader_223_5Test {

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

  private void setPos(int pos) throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  private void setLimit(int limit) throws Exception {
    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void setBuffer(char[] buffer) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buffer, 0, bufferField.get(jsonReader), 0, buffer.length);
  }

  private void setLineNumber(int lineNumber) throws Exception {
    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(jsonReader, lineNumber);
  }

  private void setLineStart(int lineStart) throws Exception {
    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(jsonReader, lineStart);
  }

  private void setFillBufferReturn(boolean returnValue) throws Exception {
    // Spy jsonReader to override fillBuffer behavior
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;
    readEscapeCharacterMethod = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    readEscapeCharacterMethod.setAccessible(true);
  }

  private IOException invokeSyntaxError(String message) throws Throwable {
    Method syntaxErrorMethod = JsonReader.class.getDeclaredMethod("syntaxError", String.class);
    syntaxErrorMethod.setAccessible(true);
    try {
      syntaxErrorMethod.invoke(jsonReader, message);
      return null;
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof IOException) {
        return (IOException) cause;
      }
      throw cause;
    }
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_throwsUnterminatedEscapeSequenceWhenFillBufferFalse() throws Throwable {
    setPos(0);
    setLimit(0);
    setFillBufferReturn(false);

    IOException thrown = assertThrows(IOException.class, this::invokeReadEscapeCharacter);
    assertTrue(thrown.getMessage().contains("Unterminated escape sequence"));
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_unicodeEscapeCharacter_success() throws Throwable {
    // Buffer contains 'u' + 4 hex digits '1a2B'
    char[] buf = new char[10];
    buf[0] = 'u';
    buf[1] = '1';
    buf[2] = 'a';
    buf[3] = '2';
    buf[4] = 'B';
    setBuffer(buf);
    setPos(0);
    setLimit(5);
    setFillBufferReturn(true);

    char result = invokeReadEscapeCharacter();
    // Unicode 0x1a2B = decimal 6699
    assertEquals((char)0x1a2B, result);
    // pos advanced by 5 (1 for 'u' + 4 for hex digits)
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_unicodeEscapeCharacter_throwsNumberFormatException() throws Throwable {
    // Buffer contains 'u' + invalid hex digits '1a2G'
    char[] buf = new char[10];
    buf[0] = 'u';
    buf[1] = '1';
    buf[2] = 'a';
    buf[3] = '2';
    buf[4] = 'G'; // invalid hex digit
    setBuffer(buf);
    setPos(0);
    setLimit(5);
    setFillBufferReturn(true);

    NumberFormatException thrown = assertThrows(NumberFormatException.class, this::invokeReadEscapeCharacter);
    assertTrue(thrown.getMessage().contains("\\u1a2G"));
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_knownEscapeCharacters() throws Throwable {
    char[] escapes = {'t', 'b', 'n', 'r', 'f', '\n', '\'', '"', '\\', '/'};
    char[] expected = {'\t', '\b', '\n', '\r', '\f', '\n', '\'', '"', '\\', '/'};

    for (int i = 0; i < escapes.length; i++) {
      char esc = escapes[i];
      char exp = expected[i];
      char[] buf = new char[10];
      buf[0] = esc;
      setBuffer(buf);
      setPos(0);
      setLimit(1);
      setFillBufferReturn(true);
      if (esc == '\n') {
        setLineNumber(5);
        setLineStart(3);
      }

      char result = invokeReadEscapeCharacter();
      assertEquals(exp, result);

      if (esc == '\n') {
        var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
        lineNumberField.setAccessible(true);
        assertEquals(6, lineNumberField.getInt(jsonReader));
        var lineStartField = JsonReader.class.getDeclaredField("lineStart");
        lineStartField.setAccessible(true);
        assertEquals(1, lineStartField.getInt(jsonReader)); // pos was 0 then incremented to 1
      }
    }
  }

  @Test
    @Timeout(8000)
  void testReadEscapeCharacter_invalidEscapeCharacter_throwsIOException() throws Throwable {
    char[] buf = new char[10];
    buf[0] = 'z'; // invalid escape
    setBuffer(buf);
    setPos(0);
    setLimit(1);
    setFillBufferReturn(true);

    IOException thrown = assertThrows(IOException.class, this::invokeReadEscapeCharacter);
    assertTrue(thrown.getMessage().contains("Invalid escape sequence"));
  }
}