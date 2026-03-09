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

public class JsonReader_207_3Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Create JsonReader with dummy Reader (not used in this test)
    jsonReader = new JsonReader(new java.io.StringReader(""));
  }

  /**
   * Helper to set private fields via reflection.
   */
  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  /**
   * Helper to get private fields via reflection.
   */
  private Object getField(Object target, String fieldName) throws Exception {
    var field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  /**
   * Invokes the private skipQuotedValue method reflectively.
   */
  private void invokeSkipQuotedValue(char quote) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    try {
      method.invoke(jsonReader, quote);
    } catch (InvocationTargetException e) {
      // Unwrap the underlying exception thrown by the method
      throw e.getCause();
    }
  }

  /**
   * Test skipQuotedValue successfully skips a quoted string terminated by the quote character.
   */
  @Test
    @Timeout(8000)
  public void testSkipQuotedValue_simpleTermination() throws Throwable {
    // Setup buffer with a quoted string: 'a', 'b', 'c', quote
    char quote = '"';
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = 'c';
    buffer[3] = quote;

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 4);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    // Override fillBuffer to return false (no refill needed)
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    // Use spyReader for invocation
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(spyReader, quote);

    // After skipping, pos should be at index after quote (4)
    int pos = (int) getField(spyReader, "pos");
    assertEquals(4, pos);
  }

  /**
   * Test skipQuotedValue handles escaped characters correctly.
   */
  @Test
    @Timeout(8000)
  public void testSkipQuotedValue_withEscapedCharacters() throws Throwable {
    char quote = '"';
    char[] buffer = new char[1024];
    // String: a \ " b "
    buffer[0] = 'a';
    buffer[1] = '\\';
    buffer[2] = '"';
    buffer[3] = 'b';
    buffer[4] = quote;

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 5);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = spy(jsonReader);

    // Mock readEscapeCharacter to simulate reading the escaped char and advancing pos by 1
    doAnswer(invocation -> {
      int pos = (int) getField(spyReader, "pos");
      setField(spyReader, "pos", pos + 1);
      return (char) '"';
    }).when(spyReader).readEscapeCharacter();

    // fillBuffer returns false (no refill)
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(spyReader, quote);

    int pos = (int) getField(spyReader, "pos");
    assertEquals(5, pos);
  }

  /**
   * Test skipQuotedValue increments lineNumber and updates lineStart on newline.
   */
  @Test
    @Timeout(8000)
  public void testSkipQuotedValue_newlineUpdatesLineNumberAndLineStart() throws Throwable {
    char quote = '"';
    char[] buffer = new char[1024];
    // String: a \n b "
    buffer[0] = 'a';
    buffer[1] = '\n';
    buffer[2] = 'b';
    buffer[3] = quote;

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 4);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = spy(jsonReader);

    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(spyReader, quote);

    int lineNumber = (int) getField(spyReader, "lineNumber");
    int lineStart = (int) getField(spyReader, "lineStart");
    int pos = (int) getField(spyReader, "pos");

    assertEquals(1, lineNumber);
    assertEquals(2, lineStart);
    assertEquals(4, pos);
  }

  /**
   * Test skipQuotedValue throws syntaxError on unterminated string.
   */
  @Test
    @Timeout(8000)
  public void testSkipQuotedValue_unterminatedStringThrows() throws Throwable {
    char quote = '"';
    char[] buffer = new char[1024];
    // String without terminating quote
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = 'c';

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = spy(jsonReader);

    // fillBuffer returns false to simulate EOF
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);

    Throwable thrown = assertThrows(Throwable.class, () -> {
      try {
        method.invoke(spyReader, quote);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    assertTrue(thrown instanceof IOException);
    assertTrue(thrown.getMessage().contains("Unterminated string"));
  }

  /**
   * Test skipQuotedValue with fillBuffer returning true (buffer refill).
   */
  @Test
    @Timeout(8000)
  public void testSkipQuotedValue_bufferRefill() throws Throwable {
    char quote = '"';

    // First buffer with no terminating quote, second buffer with quote
    char[] buffer1 = new char[1024];
    buffer1[0] = 'a';
    buffer1[1] = 'b';
    buffer1[2] = 'c';

    char[] buffer2 = new char[1024];
    buffer2[0] = quote;

    setField(jsonReader, "buffer", buffer1);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = spy(jsonReader);

    // fillBuffer first call returns true and sets buffer to buffer2, limit=1, pos=0
    doAnswer(invocation -> {
      setField(spyReader, "buffer", buffer2);
      setField(spyReader, "limit", 1);
      setField(spyReader, "pos", 0);
      return true;
    }).when(spyReader).fillBuffer(1);

    // On second call, fillBuffer returns false (simulate no further refill)
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(spyReader, quote);

    int pos = (int) getField(spyReader, "pos");
    assertEquals(1, pos);
  }
}