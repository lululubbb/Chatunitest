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

public class JsonReader_214_5Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Mock Reader to avoid real IO operations
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  /**
   * Helper to invoke private nextNonWhitespace method via reflection.
   */
  private int invokeNextNonWhitespace(boolean throwOnEof) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    try {
      return (int) method.invoke(jsonReader, throwOnEof);
    } catch (InvocationTargetException e) {
      // Unwrap and throw the underlying exception
      throw e.getCause();
    }
  }

  /**
   * Helper to invoke private fillBuffer(int) method via reflection.
   */
  private boolean invokeFillBuffer(int minimum) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    method.setAccessible(true);
    try {
      return (boolean) method.invoke(jsonReader, minimum);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  /**
   * Helper to invoke private checkLenient() method via reflection.
   */
  private void invokeCheckLenient() throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("checkLenient");
    method.setAccessible(true);
    try {
      method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  /**
   * Helper to invoke private skipToEndOfLine() method via reflection.
   */
  private void invokeSkipToEndOfLine() throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
    method.setAccessible(true);
    try {
      method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  /**
   * Helper to invoke private skipTo(String) method via reflection.
   */
  private boolean invokeSkipTo(String toFind) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("skipTo", String.class);
    method.setAccessible(true);
    try {
      return (boolean) method.invoke(jsonReader, toFind);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  /**
   * Set internal buffer, pos, limit, lineNumber, lineStart fields via reflection.
   */
  private void setInternalState(char[] buffer, int pos, int limit, int lineNumber, int lineStart) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buffer, 0, (char[]) bufferField.get(jsonReader), 0, buffer.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(jsonReader, lineNumber);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(jsonReader, lineStart);
  }

  /**
   * Get pos field value.
   */
  private int getPos() throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

  /**
   * Get lineNumber field value.
   */
  private int getLineNumber() throws Exception {
    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    return lineNumberField.getInt(jsonReader);
  }

  /**
   * Get lineStart field value.
   */
  private int getLineStart() throws Exception {
    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    return lineStartField.getInt(jsonReader);
  }

  /**
   * Get lenient field value.
   */
  private boolean getLenient() throws Exception {
    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    return lenientField.getBoolean(jsonReader);
  }

  /**
   * Set lenient field value.
   */
  private void setLenient(boolean lenient) throws Exception {
    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  /**
   * Test nextNonWhitespace returns first non-whitespace character.
   */
  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_simpleNonWhitespace() throws Throwable {
    char[] buf = new char[1024];
    buf[0] = ' ';
    buf[1] = '\r';
    buf[2] = '\n';
    buf[3] = '\t';
    buf[4] = 'a';
    setInternalState(buf, 0, 5, 0, 0);

    int c = invokeNextNonWhitespace(true);
    assertEquals('a', c);
    assertEquals(5, getPos());
    assertEquals(1, getLineNumber());
    assertEquals(3, getLineStart());
  }

  /**
   * Test nextNonWhitespace returns -1 on EOF when throwOnEof is false.
   */
  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_eofNoThrow() throws Throwable {
    setInternalState(new char[1024], 0, 0, 0, 0);

    // Override fillBuffer to return false to simulate EOF
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    // Use reflection to call nextNonWhitespace on spyReader
    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    int c = (int) method.invoke(spyReader, false);
    assertEquals(-1, c);
  }

  /**
   * Test nextNonWhitespace throws EOFException on EOF with throwOnEof true.
   */
  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_eofThrows() throws Throwable {
    setInternalState(new char[1024], 0, 0, 0, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    EOFException thrown = assertThrows(EOFException.class, () -> {
      try {
        method.invoke(spyReader, true);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(thrown.getMessage().contains("End of input"));
  }

  /**
   * Test nextNonWhitespace skips c-style comment /* ... *\/
   */
  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_skipsCStyleComment() throws Throwable {
    // buffer content: '/' '*' ' ' '*' '/' 'a'
    char[] buf = new char[1024];
    buf[0] = '/';
    buf[1] = '*';
    buf[2] = ' ';
    buf[3] = '*';
    buf[4] = '/';
    buf[5] = 'a';
    setInternalState(buf, 0, 6, 0, 0);

    // Spy to mock skipTo("*/") returning true
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).skipTo("*/");
    doNothing().when(spyReader).checkLenient();

    // Override fillBuffer to return true to avoid EOF
    doReturn(true).when(spyReader).fillBuffer(1);

    // Set buffer, pos, limit, lineNumber, lineStart on spyReader
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buf, 0, (char[]) bufferField.get(spyReader), 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 6);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spyReader, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spyReader, 0);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    int c = (int) method.invoke(spyReader, true);
    // After skipping comment, next char is 'a'
    assertEquals('a', c);

    // pos should be after '*/' (pos + 2)
    int posValue = posField.getInt(spyReader);
    assertEquals(6, posValue);
  }

  /**
   * Test nextNonWhitespace skips end-of-line comment // ...
   */
  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_skipsEndOfLineComment() throws Throwable {
    // buffer content: '/' '/' '\n' 'a'
    char[] buf = new char[1024];
    buf[0] = '/';
    buf[1] = '/';
    buf[2] = '\n';
    buf[3] = 'a';
    setInternalState(buf, 0, 4, 0, 0);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();
    doNothing().when(spyReader).skipToEndOfLine();
    doReturn(true).when(spyReader).fillBuffer(1);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buf, 0, (char[]) bufferField.get(spyReader), 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 4);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spyReader, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spyReader, 0);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    int c = (int) method.invoke(spyReader, true);
    // After skipping comment, next char is 'a'
    assertEquals('a', c);

    int posValue = posField.getInt(spyReader);
    assertEquals(4, posValue);
  }

  /**
   * Test nextNonWhitespace handles '#' comment when lenient is true.
   */
  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_skipsHashCommentLenient() throws Throwable {
    // buffer content: '#' '\n' 'a'
    char[] buf = new char[1024];
    buf[0] = '#';
    buf[1] = '\n';
    buf[2] = 'a';
    setInternalState(buf, 0, 3, 0, 0);

    JsonReader spyReader = spy(jsonReader);
    setLenient(true);
    doNothing().when(spyReader).checkLenient();
    doNothing().when(spyReader).skipToEndOfLine();
    doReturn(true).when(spyReader).fillBuffer(1);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buf, 0, (char[]) bufferField.get(spyReader), 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 3);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spyReader, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spyReader, 0);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    int c = (int) method.invoke(spyReader, true);
    assertEquals('a', c);

    int posValue = posField.getInt(spyReader);
    assertEquals(3, posValue);
  }

  /**
   * Test nextNonWhitespace returns '/' if not followed by '*' or '/'.
   */
  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_slashFollowedByOther() throws Throwable {
    // buffer content: '/' 'a'
    char[] buf = new char[1024];
    buf[0] = '/';
    buf[1] = 'a';
    setInternalState(buf, 0, 2, 0, 0);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buf, 0, (char[]) bufferField.get(spyReader), 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 2);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    int c = (int) method.invoke(spyReader, true);
    assertEquals('/', c);

    int posValue = posField.getInt(spyReader);
    assertEquals(1, posValue);
  }

  /**
   * Test nextNonWhitespace with whitespace only and fillBuffer returning false.
   */
  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_whitespaceThenEofNoThrow() throws Throwable {
    char[] buf = new char[1024];
    buf[0] = ' ';
    buf[1] = '\n';
    setInternalState(buf, 0, 2, 0, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buf, 0, (char[]) bufferField.get(spyReader), 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 2);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spyReader, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spyReader, 0);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    int c = (int) method.invoke(spyReader, false);
    assertEquals(-1, c);

    assertEquals(2, posField.getInt(spyReader));
    assertEquals(1, lineNumberField.getInt(spyReader));
    assertEquals(2, lineStartField.getInt(spyReader));
  }
}