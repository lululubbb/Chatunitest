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

public class JsonReader_223_1Test {
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

  private void setBufferAndPos(char[] buf, int pos, int limit) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buf, 0, bufferField.get(jsonReader), 0, buf.length);

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

  private void mockFillBuffer(boolean returnValue) throws Exception {
    Method fillBuffer = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBuffer.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);

    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());

    var field = JsonReader.class.getDeclaredField("in");
    field.setAccessible(true);
    field.set(jsonReader, mockReader);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeValid() throws Throwable {
    // Setup buffer with 'u' + 4 hex digits '0041' = 'A'
    char[] buf = new char[1024];
    buf[0] = 'u';
    buf[1] = '0';
    buf[2] = '0';
    buf[3] = '4';
    buf[4] = '1';

    setBufferAndPos(buf, 0, 5);
    // fillBuffer returns true if needed, but here limit is enough
    // so no fillBuffer call needed

    char result = invokeReadEscapeCharacter();
    assertEquals('A', result);

    // pos should have advanced by 5 (1 for 'u' + 4 for hex digits)
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = posField.getInt(jsonReader);
    assertEquals(5, pos);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeInvalidHex() throws Throwable {
    char[] buf = new char[1024];
    buf[0] = 'u';
    buf[1] = '0';
    buf[2] = '0';
    buf[3] = 'G'; // invalid hex char
    buf[4] = '1';

    setBufferAndPos(buf, 0, 5);

    NumberFormatException ex = assertThrows(NumberFormatException.class, this::invokeReadEscapeCharacter);
    assertTrue(ex.getMessage().contains("\\u004G"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeTab() throws Throwable {
    char[] buf = new char[] { 't' };
    setBufferAndPos(buf, 0, 1);

    char c = invokeReadEscapeCharacter();
    assertEquals('\t', c);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(1, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeBackspace() throws Throwable {
    char[] buf = new char[] { 'b' };
    setBufferAndPos(buf, 0, 1);

    char c = invokeReadEscapeCharacter();
    assertEquals('\b', c);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(1, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeNewline() throws Throwable {
    char[] buf = new char[] { 'n' };
    setBufferAndPos(buf, 0, 1);

    char c = invokeReadEscapeCharacter();
    assertEquals('\n', c);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(1, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeCarriageReturn() throws Throwable {
    char[] buf = new char[] { 'r' };
    setBufferAndPos(buf, 0, 1);

    char c = invokeReadEscapeCharacter();
    assertEquals('\r', c);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(1, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeFormFeed() throws Throwable {
    char[] buf = new char[] { 'f' };
    setBufferAndPos(buf, 0, 1);

    char c = invokeReadEscapeCharacter();
    assertEquals('\f', c);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(1, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeNewlineLineNumberIncrement() throws Throwable {
    char[] buf = new char[] { '\n', '"' };
    setBufferAndPos(buf, 0, 2);

    setLineNumber(10);
    setLineStart(20);

    char c = invokeReadEscapeCharacter();
    assertEquals('\n', c);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    int lineNumber = lineNumberField.getInt(jsonReader);
    assertEquals(11, lineNumber);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    int lineStart = lineStartField.getInt(jsonReader);
    assertEquals(1, lineStart); // pos was incremented to 1

    // Now test fall-through to return escaped char '"'
    char[] buf2 = new char[] { '\n', '"' };
    setBufferAndPos(buf2, 1, 2);
    char c2 = invokeReadEscapeCharacter();
    assertEquals('"', c2);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeQuoteBackslashSlash() throws Throwable {
    char[] chars = new char[] { '\'', '"', '\\', '/' };
    for (char ch : chars) {
      setBufferAndPos(new char[] { ch }, 0, 1);
      char c = invokeReadEscapeCharacter();
      assertEquals(ch, c);
      var posField = JsonReader.class.getDeclaredField("pos");
      posField.setAccessible(true);
      assertEquals(1, posField.getInt(jsonReader));
    }
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_invalidEscapeSequence() throws Throwable {
    char[] buf = new char[] { 'z' };
    setBufferAndPos(buf, 0, 1);

    IOException ex = assertThrows(IOException.class, this::invokeReadEscapeCharacter);
    assertTrue(ex.getMessage().contains("Invalid escape sequence"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unterminatedEscapeSequenceAtBufferEnd() throws Throwable {
    // pos == limit and fillBuffer returns false
    setBufferAndPos(new char[0], 0, 0);

    // Spy jsonReader to mock fillBuffer returning false
    JsonReader spyReader = spy(jsonReader);
    var fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    doReturn(false).when(spyReader).fillBuffer(1);

    // Replace jsonReader with spyReader for reflection invocation
    Method method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    method.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(spyReader));
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof IOException);
    assertTrue(cause.getMessage().contains("Unterminated escape sequence"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeUnterminatedEscapeSequenceFillBufferFalse() throws Throwable {
    // Setup buffer with 'u' but less than 4 hex digits, pos+4 > limit
    char[] buf = new char[] { 'u', '0', '0' };
    setBufferAndPos(buf, 0, 3);

    // Spy jsonReader to mock fillBuffer returning false for 4 chars
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(4);

    // Replace jsonReader with spyReader for reflection invocation
    Method method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    method.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(spyReader));
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof IOException);
    assertTrue(cause.getMessage().contains("Unterminated escape sequence"));
  }
}