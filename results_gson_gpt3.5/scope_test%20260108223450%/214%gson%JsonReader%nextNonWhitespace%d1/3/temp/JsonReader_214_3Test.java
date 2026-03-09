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

class JsonReader_214_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  /**
   * Utility method to invoke private nextNonWhitespace(boolean) method via reflection.
   */
  private int invokeNextNonWhitespace(boolean throwOnEof)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    return (int) method.invoke(jsonReader, throwOnEof);
  }

  /**
   * Sets the internal buffer, pos, limit, lineNumber, and lineStart fields via reflection.
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
   * Gets the current pos field value.
   */
  private int getPos() throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

  /**
   * Gets the current lineNumber field value.
   */
  private int getLineNumber() throws Exception {
    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    return lineNumberField.getInt(jsonReader);
  }

  /**
   * Gets the current lineStart field value.
   */
  private int getLineStart() throws Exception {
    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    return lineStartField.getInt(jsonReader);
  }

  /**
   * Mocks fillBuffer(int) method to return specified value.
   */
  private void mockFillBuffer(boolean returnValue) throws Exception {
    var fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);

    // Spy on jsonReader to override fillBuffer
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());

    // Replace jsonReader with spyReader
    jsonReader = spyReader;
  }

  /**
   * Mocks fillBuffer(int) method to throw IOException.
   */
  private void mockFillBufferThrows(IOException e) throws Exception {
    JsonReader spyReader = spy(jsonReader);
    doThrow(e).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;
  }

  /**
   * Mocks checkLenient() method to do nothing.
   */
  private void mockCheckLenient() throws Exception {
    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();
    jsonReader = spyReader;
  }

  /**
   * Mocks skipTo(String) method to return specified value.
   */
  private void mockSkipTo(boolean returnValue) throws Exception {
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).skipTo(anyString());
    jsonReader = spyReader;
  }

  /**
   * Mocks skipToEndOfLine() method to do nothing.
   */
  private void mockSkipToEndOfLine() throws Exception {
    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipToEndOfLine();
    jsonReader = spyReader;
  }

  /**
   * Mocks syntaxError(String) method to throw IOException with the message.
   */
  private void mockSyntaxErrorThrows(String message) throws Exception {
    JsonReader spyReader = spy(jsonReader);
    doThrow(new IOException(message)).when(spyReader).syntaxError(anyString());
    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_returnsChar_skipsSpacesAndReturnsNext() throws Exception {
    // buffer contains spaces and a letter 'a'
    char[] buf = new char[1024];
    buf[0] = ' ';
    buf[1] = '\t';
    buf[2] = 'a';
    setInternalState(buf, 0, 3, 0, 0);

    int result = invokeNextNonWhitespace(true);
    assertEquals('a', result);
    assertEquals(3, getPos());
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_incrementsLineNumberOnNewLine() throws Exception {
    char[] buf = new char[1024];
    buf[0] = '\n';
    buf[1] = 'b';
    setInternalState(buf, 0, 2, 0, 0);

    int result = invokeNextNonWhitespace(true);
    assertEquals('b', result);
    assertEquals(2, getPos());
    assertEquals(1, getLineNumber());
    assertEquals(1, getLineStart());
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_handlesSlashStarComment_skipsComment() throws Exception {
    // Setup buffer with '/' '*' ... '*/' and then 'c'
    char[] buf = new char[1024];
    buf[0] = '/';
    buf[1] = '*';
    buf[2] = 'x';
    buf[3] = 'y';
    buf[4] = '*';
    buf[5] = '/';
    buf[6] = 'c';
    setInternalState(buf, 0, 7, 0, 0);

    mockCheckLenient();
    mockSkipTo(true);

    int result = invokeNextNonWhitespace(true);
    assertEquals('c', result);
    assertTrue(getPos() >= 7);
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_handlesSlashSlashComment_skipsComment() throws Exception {
    // Setup buffer with '/' '/' ... end of line then 'd'
    char[] buf = new char[1024];
    buf[0] = '/';
    buf[1] = '/';
    buf[2] = 'x';
    buf[3] = '\n';
    buf[4] = 'd';
    setInternalState(buf, 0, 5, 0, 0);

    mockCheckLenient();
    mockSkipToEndOfLine();

    int result = invokeNextNonWhitespace(true);
    assertEquals('d', result);
    assertTrue(getPos() >= 5);
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_handlesSlashAtBufferEnd_fillBufferCalled() throws Exception {
    // buffer ends with '/'
    char[] buf = new char[1024];
    buf[0] = '/';
    setInternalState(buf, 0, 1, 0, 0);

    mockCheckLenient();
    mockSkipToEndOfLine();
    // Spy fillBuffer to return false after first call, true after second
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).doReturn(false).when(spyReader).fillBuffer(anyInt());
    doNothing().when(spyReader).checkLenient();
    doNothing().when(spyReader).skipToEndOfLine();
    jsonReader = spyReader;

    int result = invokeNextNonWhitespace(false);
    assertEquals('/', result);
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_handlesHashComment_skipsComment() throws Exception {
    // buffer starts with '#' then newline then 'e'
    char[] buf = new char[1024];
    buf[0] = '#';
    buf[1] = 'x';
    buf[2] = '\n';
    buf[3] = 'e';
    setInternalState(buf, 0, 4, 0, 0);

    mockCheckLenient();
    mockSkipToEndOfLine();

    int result = invokeNextNonWhitespace(true);
    assertEquals('e', result);
    assertTrue(getPos() >= 4);
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_returnsCharIfNotSpecial() throws Exception {
    char[] buf = new char[1024];
    buf[0] = 'z';
    setInternalState(buf, 0, 1, 0, 0);

    int result = invokeNextNonWhitespace(true);
    assertEquals('z', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_throwsEOFException_whenThrowOnEofTrue() throws Exception {
    setInternalState(new char[1024], 0, 0, 0, 0);

    // Spy fillBuffer to return false (simulate EOF)
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());
    // Override locationString to return known string
    var locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    doReturn(" at line 1 column 1").when(spyReader).locationString();
    jsonReader = spyReader;

    NoSuchMethodException noSuchMethodException = null;
    try {
      invokeNextNonWhitespace(true);
      fail("Expected EOFException");
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof EOFException);
      assertTrue(cause.getMessage().contains("End of input"));
    }
  }

  @Test
    @Timeout(8000)
  void nextNonWhitespace_returnsMinusOne_whenThrowOnEofFalse() throws Exception {
    setInternalState(new char[1024], 0, 0, 0, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;

    int result = invokeNextNonWhitespace(false);
    assertEquals(-1, result);
  }
}