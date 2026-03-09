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

public class JsonReader_223_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private char invokeReadEscapeCharacter() throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    method.setAccessible(true);
    try {
      return (char) method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void setBufferAndPos(char[] buffer, int pos, int limit) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
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

  private void setFillBufferBehavior(boolean returnValue) throws Exception {
    // Spy on jsonReader to stub fillBuffer method
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());

    // Replace jsonReader with spyReader for reflection calls
    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeValidLowercase() throws Throwable {
    // Setup buffer for \u0041 (A)
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'u';
    buffer[1] = '0';
    buffer[2] = '0';
    buffer[3] = '4';
    buffer[4] = '1';
    setBufferAndPos(buffer, 0, 5);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('A', result);
    // pos should have advanced 5 chars (1 for 'u' + 4 for hex digits)
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeValidUppercase() throws Throwable {
    // Setup buffer for \u0041 (A) uppercase hex digits
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'u';
    buffer[1] = '0';
    buffer[2] = '0';
    buffer[3] = '4';
    buffer[4] = '1';
    setBufferAndPos(buffer, 0, 5);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('A', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeInvalidHex() throws Throwable {
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'u';
    buffer[1] = 'G'; // invalid hex digit
    buffer[2] = '0';
    buffer[3] = '0';
    buffer[4] = '1';
    setBufferAndPos(buffer, 0, 5);
    setFillBufferBehavior(true);

    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> invokeReadEscapeCharacter());
    assertTrue(ex.getMessage().startsWith("\\u"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeTab() throws Throwable {
    char[] buffer = new char[] {'t'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('\t', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeBackspace() throws Throwable {
    char[] buffer = new char[] {'b'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('\b', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeNewline() throws Throwable {
    char[] buffer = new char[] {'n'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('\n', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeCarriageReturn() throws Throwable {
    char[] buffer = new char[] {'r'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('\r', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeFormFeed() throws Throwable {
    char[] buffer = new char[] {'f'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('\f', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeNewlineIncrementLineNumber() throws Throwable {
    char[] buffer = new char[] {'\n', '"'};
    setBufferAndPos(buffer, 0, 2);
    setFillBufferBehavior(true);
    setLineNumber(5);
    setLineStart(10);

    char result = invokeReadEscapeCharacter();
    assertEquals('"', result);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    assertEquals(6, lineNumberField.getInt(jsonReader));

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    assertEquals(1, lineStartField.getInt(jsonReader)); // pos was incremented to 1
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeSingleQuote() throws Throwable {
    char[] buffer = new char[] {'\''};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('\'', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeDoubleQuote() throws Throwable {
    char[] buffer = new char[] {'"'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('"', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeBackslash() throws Throwable {
    char[] buffer = new char[] {'\\'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('\\', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeSlash() throws Throwable {
    char[] buffer = new char[] {'/'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    char result = invokeReadEscapeCharacter();
    assertEquals('/', result);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unterminatedEscapeSequenceThrows() throws Exception {
    // pos == limit and fillBuffer returns false
    setPos(10);
    setLimit(10);

    setFillBufferBehavior(false);

    IOException ex = assertThrows(IOException.class, () -> invokeReadEscapeCharacter());
    assertTrue(ex.getMessage().contains("Unterminated escape sequence"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unterminatedUnicodeEscapeSequenceThrows() throws Exception {
    // pos + 4 > limit and fillBuffer returns false
    char[] buffer = new char[] {'u', '0', '0'};
    setBufferAndPos(buffer, 0, 3);
    setFillBufferBehavior(false);

    IOException ex = assertThrows(IOException.class, () -> invokeReadEscapeCharacter());
    assertTrue(ex.getMessage().contains("Unterminated escape sequence"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_invalidEscapeSequenceThrows() throws Exception {
    char[] buffer = new char[] {'z'};
    setBufferAndPos(buffer, 0, 1);
    setFillBufferBehavior(true);

    IOException ex = assertThrows(IOException.class, () -> invokeReadEscapeCharacter());
    assertTrue(ex.getMessage().contains("Invalid escape sequence"));
  }
}