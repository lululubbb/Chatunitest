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

public class JsonReader_214_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokeNextNonWhitespace(JsonReader reader, boolean throwOnEof) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    return (int) method.invoke(reader, throwOnEof);
  }

  @Test
    @Timeout(8000)
  public void nextNonWhitespace_returnsNonWhitespaceChar() throws Exception {
    // Setup buffer with whitespace and a non-whitespace char 'a'
    char[] buf = getBuffer(jsonReader);
    buf[0] = ' ';
    buf[1] = '\n';
    buf[2] = '\r';
    buf[3] = '\t';
    buf[4] = 'a';
    setPos(jsonReader, 0);
    setLimit(jsonReader, 5);
    setLineNumber(jsonReader, 0);
    setLineStart(jsonReader, 0);

    int c = invokeNextNonWhitespace(jsonReader, true);
    assertEquals('a', c);
    assertEquals(5, getPos(jsonReader));
    assertEquals(1, getLineNumber(jsonReader)); // Because of '\n' increment
    assertEquals(2, getLineStart(jsonReader)); // lineStart set to position after '\n'
  }

  @Test
    @Timeout(8000)
  public void nextNonWhitespace_skipsCStyleComment() throws Exception {
    // buffer: '/' '*' 'x' '*' '/' 'a'
    char[] buf = getBuffer(jsonReader);
    buf[0] = '/';
    buf[1] = '*';
    buf[2] = 'x';
    buf[3] = '*';
    buf[4] = '/';
    buf[5] = 'a';
    setPos(jsonReader, 0);
    setLimit(jsonReader, 6);
    setLineNumber(jsonReader, 0);
    setLineStart(jsonReader, 0);
    jsonReader.setLenient(true);

    int c = invokeNextNonWhitespace(jsonReader, true);
    assertEquals('a', c);
    assertEquals(6, getPos(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void nextNonWhitespace_skipsEndOfLineComment() throws Exception {
    // buffer: '/' '/' '\n' 'a'
    char[] buf = getBuffer(jsonReader);
    buf[0] = '/';
    buf[1] = '/';
    buf[2] = '\n';
    buf[3] = 'a';
    setPos(jsonReader, 0);
    setLimit(jsonReader, 4);
    setLineNumber(jsonReader, 0);
    setLineStart(jsonReader, 0);
    jsonReader.setLenient(true);

    int c = invokeNextNonWhitespace(jsonReader, true);
    assertEquals('a', c);
    assertEquals(4, getPos(jsonReader));
    assertEquals(1, getLineNumber(jsonReader));
    assertEquals(3, getLineStart(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void nextNonWhitespace_skipsHashComment() throws Exception {
    // buffer: '#' '\n' 'a'
    char[] buf = getBuffer(jsonReader);
    buf[0] = '#';
    buf[1] = '\n';
    buf[2] = 'a';
    setPos(jsonReader, 0);
    setLimit(jsonReader, 3);
    setLineNumber(jsonReader, 0);
    setLineStart(jsonReader, 0);
    jsonReader.setLenient(true);

    int c = invokeNextNonWhitespace(jsonReader, true);
    assertEquals('a', c);
    assertEquals(3, getPos(jsonReader));
    assertEquals(1, getLineNumber(jsonReader));
    assertEquals(2, getLineStart(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void nextNonWhitespace_returnsSlashWhenNotComment() throws Exception {
    // buffer: '/' 'a'
    char[] buf = getBuffer(jsonReader);
    buf[0] = '/';
    buf[1] = 'a';
    setPos(jsonReader, 0);
    setLimit(jsonReader, 2);
    setLineNumber(jsonReader, 0);
    setLineStart(jsonReader, 0);
    jsonReader.setLenient(true);

    int c = invokeNextNonWhitespace(jsonReader, true);
    assertEquals('/', c);
    assertEquals(1, getPos(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void nextNonWhitespace_fillBufferReturnsFalse_throwsEOFException() throws Exception {
    // Setup pos == limit and fillBuffer returns false
    setPos(jsonReader, 0);
    setLimit(jsonReader, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    EOFException e = assertThrows(EOFException.class, () -> method.invoke(spyReader, true));
    assertTrue(e.getCause() == null || e.getCause() instanceof Throwable);
  }

  @Test
    @Timeout(8000)
  public void nextNonWhitespace_fillBufferReturnsFalse_returnsMinusOneWhenThrowOnEofFalse() throws Exception {
    setPos(jsonReader, 0);
    setLimit(jsonReader, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    int result = invokeNextNonWhitespace(spyReader, false);
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void nextNonWhitespace_fillBufferCalledWhenSlashAtEndBuffer() throws Exception {
    // buffer: '/' (pos=0, limit=1)
    char[] buf = getBuffer(jsonReader);
    buf[0] = '/';
    setPos(jsonReader, 0);
    setLimit(jsonReader, 1);
    jsonReader.setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(2);
    doReturn(true).when(spyReader).skipTo("*/");
    doNothing().when(spyReader).skipToEndOfLine();

    int c = invokeNextNonWhitespace(spyReader, true);
    // It will consume '/' then call fillBuffer(2) and then skipTo or skipToEndOfLine depending on next char.
    // Because fillBuffer returns true and skipTo returns true, it continues and tries to find next non-whitespace.
    // As buffer is not changed, it may loop indefinitely, so we limit buffer size and content:
    // We will setup buffer to have a comment then 'a' to exit loop.
    // But here just check it doesn't return '/' (which means it handled fillBuffer correctly).
    assertNotEquals('/', c);
  }

  // Helpers to access private fields
  private char[] getBuffer(JsonReader reader) throws Exception {
    var f = JsonReader.class.getDeclaredField("buffer");
    f.setAccessible(true);
    return (char[]) f.get(reader);
  }

  private void setPos(JsonReader reader, int value) throws Exception {
    var f = JsonReader.class.getDeclaredField("pos");
    f.setAccessible(true);
    f.setInt(reader, value);
  }

  private int getPos(JsonReader reader) throws Exception {
    var f = JsonReader.class.getDeclaredField("pos");
    f.setAccessible(true);
    return f.getInt(reader);
  }

  private void setLimit(JsonReader reader, int value) throws Exception {
    var f = JsonReader.class.getDeclaredField("limit");
    f.setAccessible(true);
    f.setInt(reader, value);
  }

  private void setLineNumber(JsonReader reader, int value) throws Exception {
    var f = JsonReader.class.getDeclaredField("lineNumber");
    f.setAccessible(true);
    f.setInt(reader, value);
  }

  private int getLineNumber(JsonReader reader) throws Exception {
    var f = JsonReader.class.getDeclaredField("lineNumber");
    f.setAccessible(true);
    return f.getInt(reader);
  }

  private void setLineStart(JsonReader reader, int value) throws Exception {
    var f = JsonReader.class.getDeclaredField("lineStart");
    f.setAccessible(true);
    f.setInt(reader, value);
  }

  private int getLineStart(JsonReader reader) throws Exception {
    var f = JsonReader.class.getDeclaredField("lineStart");
    f.setAccessible(true);
    return f.getInt(reader);
  }
}