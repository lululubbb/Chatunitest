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

class JsonReader_214_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  /**
   * Utility method to set private fields via reflection.
   */
  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  /**
   * Utility method to get private fields via reflection.
   */
  private Object getField(Object target, String fieldName) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  /**
   * Utility method to invoke the private nextNonWhitespace method.
   */
  private int invokeNextNonWhitespace(boolean throwOnEof) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    return (int) method.invoke(jsonReader, throwOnEof);
  }

  /**
   * Utility method to set buffer, pos, limit fields.
   */
  private void setBufferAndPosLimit(char[] buffer, int pos, int limit) throws Exception {
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", pos);
    setField(jsonReader, "limit", limit);
  }

  /**
   * Utility method to set lenient mode.
   */
  private void setLenient(boolean lenient) {
    jsonReader.setLenient(lenient);
  }

  /**
   * Utility method to set lineNumber and lineStart.
   */
  private void setLineNumberAndStart(int lineNumber, int lineStart) throws Exception {
    setField(jsonReader, "lineNumber", lineNumber);
    setField(jsonReader, "lineStart", lineStart);
  }

  /**
   * Test nextNonWhitespace returns non-whitespace character normally.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceReturnsNonWhitespace() throws Exception {
    char[] buf = new char[] { ' ', '\n', '\r', '\t', 'x', 'y' };
    setBufferAndPosLimit(buf, 0, buf.length);
    setLineNumberAndStart(0, 0);

    int c = invokeNextNonWhitespace(true);
    assertEquals('x', c);
    assertEquals(4, getField(jsonReader, "pos"));
    assertEquals(1, getField(jsonReader, "lineNumber"));
    assertEquals(4, getField(jsonReader, "lineStart"));
  }

  /**
   * Test nextNonWhitespace returns -1 on EOF without throwOnEof.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceReturnsMinusOneOnEofNoThrow() throws Exception {
    // buffer empty, pos == limit
    setBufferAndPosLimit(new char[1024], 0, 0);
    setLineNumberAndStart(0, 0);

    // Override fillBuffer to return false (simulate EOF)
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader, false);
    assertEquals(-1, result);
  }

  /**
   * Test nextNonWhitespace throws EOFException on EOF with throwOnEof.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceThrowsEofExceptionOnEofThrow() throws Exception {
    setBufferAndPosLimit(new char[1024], 0, 0);
    setLineNumberAndStart(0, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    EOFException thrown = assertThrows(EOFException.class, () -> {
      method.invoke(spyReader, true);
    });
    assertTrue(thrown.getCause() == null || thrown.getCause() instanceof Throwable);
    assertTrue(thrown.getMessage().contains("End of input"));
  }

  /**
   * Test nextNonWhitespace skips c-style comment /* ... *\/ correctly.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceSkipsCStyleComment() throws Exception {
    // buffer content: '/' '*' 'a' '*' '/' 'x'
    char[] buf = new char[] { '/', '*', 'a', '*', '/', 'x' };
    setBufferAndPosLimit(buf, 0, buf.length);
    setLineNumberAndStart(0, 0);
    setLenient(true);

    // Mock skipTo to return true (comment terminated)
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).skipTo("*/");
    doReturn(true).when(spyReader).fillBuffer(1);

    // Replace buffer, pos, limit in spyReader
    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buf.length);
    setField(spyReader, "lenient", true);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader, true);
    assertEquals('x', result);
    assertEquals(6, getField(spyReader, "pos")); // pos advanced past comment and 'x'
  }

  /**
   * Test nextNonWhitespace throws on unterminated c-style comment.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceThrowsOnUnterminatedCStyleComment() throws Exception {
    char[] buf = new char[] { '/', '*', 'a', 'b', 'c' };
    setBufferAndPosLimit(buf, 0, buf.length);
    setLineNumberAndStart(0, 0);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).skipTo("*/");
    doReturn(true).when(spyReader).fillBuffer(1);

    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buf.length);
    setField(spyReader, "lenient", true);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);

    Exception ex = assertThrows(Exception.class, () -> {
      method.invoke(spyReader, true);
    });
    // InvocationTargetException wraps the actual exception
    Throwable cause = ex.getCause();
    assertNotNull(cause);
    assertTrue(cause.getMessage().contains("Unterminated comment"));
  }

  /**
   * Test nextNonWhitespace skips end-of-line // comment.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceSkipsEndOfLineComment() throws Exception {
    char[] buf = new char[] { '/', '/', 'x', '\n', 'y' };
    setBufferAndPosLimit(buf, 0, buf.length);
    setLineNumberAndStart(0, 0);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipToEndOfLine();
    doReturn(true).when(spyReader).fillBuffer(1);

    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buf.length);
    setField(spyReader, "lenient", true);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader, true);
    assertEquals('y', result);
  }

  /**
   * Test nextNonWhitespace skips # comment in lenient mode.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceSkipsHashComment() throws Exception {
    char[] buf = new char[] { '#', 'a', '\n', 'b' };
    setBufferAndPosLimit(buf, 0, buf.length);
    setLineNumberAndStart(0, 0);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipToEndOfLine();
    doReturn(true).when(spyReader).fillBuffer(1);

    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buf.length);
    setField(spyReader, "lenient", true);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader, true);
    assertEquals('b', result);
  }

  /**
   * Test nextNonWhitespace returns '/' when next char is not comment start.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceReturnsSlashWhenNotComment() throws Exception {
    char[] buf = new char[] { '/', 'x' };
    setBufferAndPosLimit(buf, 0, 2);
    setLineNumberAndStart(0, 0);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(2);

    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 2);
    setField(spyReader, "lenient", true);

    Method method = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader, true);
    assertEquals('/', result);
  }

  /**
   * Test nextNonWhitespace handles multiple whitespace and newlines.
   */
  @Test
    @Timeout(8000)
  void testNextNonWhitespaceMultipleWhitespaceAndNewlines() throws Exception {
    char[] buf = new char[] { ' ', '\r', '\t', '\n', 'a' };
    setBufferAndPosLimit(buf, 0, buf.length);
    setLineNumberAndStart(0, 0);

    int c = invokeNextNonWhitespace(true);
    assertEquals('a', c);
    assertEquals(5, getField(jsonReader, "pos"));
    assertEquals(1, getField(jsonReader, "lineNumber"));
    assertEquals(4, getField(jsonReader, "lineStart"));
  }
}