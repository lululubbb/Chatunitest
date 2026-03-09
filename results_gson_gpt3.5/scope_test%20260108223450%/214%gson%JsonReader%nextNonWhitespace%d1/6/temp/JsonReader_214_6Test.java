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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_214_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  /**
   * Helper to set the private fields buffer, pos, limit, lineNumber, lineStart via reflection.
   */
  private void setBufferAndPositions(char[] buffer, int pos, int limit, int lineNumber, int lineStart) throws Exception {
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
   * Helper to set lenient flag via reflection.
   */
  private void setLenient(boolean lenient) throws Exception {
    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  /**
   * Helper to mock fillBuffer(boolean) method.
   */
  private void mockFillBuffer(boolean returnValue) throws Exception {
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);

    // Spy jsonReader to override fillBuffer
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());

    // Replace jsonReader with spyReader for reflection calls
    jsonReader = spyReader;
  }

  /**
   * Helper to set pos field.
   */
  private void setPos(int pos) throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  /**
   * Helper to invoke nextNonWhitespace(boolean) via reflection.
   */
  private int invokeNextNonWhitespace(boolean throwOnEof) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    return (int) method.invoke(jsonReader, throwOnEof);
  }

  /**
   * Helper to mock skipTo(String) method.
   */
  private void mockSkipTo(boolean returnValue) throws Exception {
    Method skipToMethod = JsonReader.class.getDeclaredMethod("skipTo", String.class);
    skipToMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).skipTo(anyString());
    jsonReader = spyReader;
  }

  /**
   * Helper to mock skipToEndOfLine() method.
   */
  private void mockSkipToEndOfLine() throws Exception {
    Method skipToEndOfLineMethod = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
    skipToEndOfLineMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipToEndOfLine();
    jsonReader = spyReader;
  }

  /**
   * Helper to mock checkLenient() method.
   */
  private void mockCheckLenient() throws Exception {
    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).checkLenient();
    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_ReturnsNonWhitespaceChar() throws Exception {
    // buffer with spaces and a letter 'a' at pos 3
    char[] buffer = new char[1024];
    buffer[0] = ' ';
    buffer[1] = '\t';
    buffer[2] = '\n';
    buffer[3] = 'a';
    setBufferAndPositions(buffer, 0, 4, 0, 0);

    int c = invokeNextNonWhitespace(true);
    assertEquals('a', c);
    // pos should be updated to 4 after reading 'a'
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = posField.getInt(jsonReader);
    assertEquals(4, pos);
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_SkipsWhitespaceAndNewlines() throws Exception {
    char[] buffer = new char[1024];
    buffer[0] = ' ';
    buffer[1] = '\r';
    buffer[2] = '\t';
    buffer[3] = '\n';
    buffer[4] = 'b';
    setBufferAndPositions(buffer, 0, 5, 5, 0);

    int c = invokeNextNonWhitespace(true);
    assertEquals('b', c);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    int lineNumber = lineNumberField.getInt(jsonReader);
    assertEquals(6, lineNumber);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    int lineStart = lineStartField.getInt(jsonReader);
    assertEquals(4, lineStart);
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_ReturnsMinusOneOnEofWithoutThrow() throws Exception {
    setBufferAndPositions(new char[1024], 0, 0, 0, 0);
    // mock fillBuffer to return false (EOF)
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;

    int c = invokeNextNonWhitespace(false);
    assertEquals(-1, c);
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_ThrowsEOFExceptionOnEofWithThrow() throws Exception {
    setBufferAndPositions(new char[1024], 0, 0, 0, 0);
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;

    Exception exception = assertThrows(EOFException.class, () -> invokeNextNonWhitespace(true));
    String msg = exception.getMessage();
    assertTrue(msg.contains("End of input"));
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_SkipsSingleLineComment() throws Exception {
    // buffer with '/' followed by '/' comment start, then 'x'
    char[] buffer = new char[1024];
    buffer[0] = '/';
    buffer[1] = '/';
    buffer[2] = 'x';
    setBufferAndPositions(buffer, 0, 3, 0, 0);

    mockCheckLenient();
    mockSkipToEndOfLine();

    int c = invokeNextNonWhitespace(true);
    // After skipping comment, pos should be at 2 and return 'x'
    assertEquals('x', c);
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_SkipsMultiLineComment() throws Exception {
    // buffer with '/' followed by '*' comment start, then 'x'
    char[] buffer = new char[1024];
    buffer[0] = '/';
    buffer[1] = '*';
    buffer[2] = ' ';
    buffer[3] = '*';
    buffer[4] = '/';
    buffer[5] = 'x';
    setBufferAndPositions(buffer, 0, 6, 0, 0);

    mockCheckLenient();
    mockSkipTo(true);

    int c = invokeNextNonWhitespace(true);
    // After skipping comment, pos should be at 7 (5+2)
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = posField.getInt(jsonReader);
    assertEquals(7, pos);

    // The next char returned should be 'x'
    // We need to put 'x' at pos 7, so fix buffer accordingly
    buffer[7] = 'x';
    setBufferAndPositions(buffer, pos, 8, 0, 0);

    c = invokeNextNonWhitespace(true);
    assertEquals('x', c);
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_UnterminatedMultiLineComment_Throws() throws Exception {
    char[] buffer = new char[1024];
    buffer[0] = '/';
    buffer[1] = '*';
    setBufferAndPositions(buffer, 0, 2, 0, 0);

    mockCheckLenient();
    mockSkipTo(false);

    Exception ex = assertThrows(IOException.class, () -> invokeNextNonWhitespace(true));
    assertTrue(ex.getMessage().contains("Unterminated comment"));
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_SlashesAtBufferEnd_FillBufferFalse() throws Exception {
    char[] buffer = new char[1024];
    buffer[0] = '/';
    setBufferAndPositions(buffer, 0, 1, 0, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(2);
    doNothing().when(spyReader).checkLenient();
    jsonReader = spyReader;

    int c = invokeNextNonWhitespace(true);
    assertEquals('/', c);
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_SlashesAtBufferEnd_FillBufferTrue() throws Exception {
    char[] buffer = new char[1024];
    buffer[0] = '/';
    buffer[1] = '*';
    setBufferAndPositions(buffer, 0, 1, 0, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(2);
    doNothing().when(spyReader).checkLenient();
    doReturn(true).when(spyReader).skipTo(anyString());
    jsonReader = spyReader;

    int c = invokeNextNonWhitespace(true);
    // Because of skipTo("*/"), the method should continue and eventually return next char or loop.
    // We can't predict exact char but it should not return '/'
    assertTrue(c != '/');
  }

  @Test
    @Timeout(8000)
  public void testNextNonWhitespace_HashComment_LenientFalse_Throws() throws Exception {
    char[] buffer = new char[1024];
    buffer[0] = '#';
    buffer[1] = 'x';
    setBufferAndPositions(buffer, 0, 2, 0, 0);

    // lenient false by default
    Exception ex = assertThrows(IOException.class, () -> invokeNextNonWhitespace(true));
    assertTrue(ex.getMessage().contains("Use JsonReader.setLenient(true) to accept malformed JSON"));

    // Now set lenient true and test it skips
    setLenient(true);
    mockCheckLenient();
    mockSkipToEndOfLine();

    int c = invokeNextNonWhitespace(true);
    assertEquals('x', c);
  }
}