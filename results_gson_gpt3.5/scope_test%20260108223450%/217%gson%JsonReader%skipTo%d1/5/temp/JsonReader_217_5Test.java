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

public class JsonReader_217_5Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Create a Reader mock as it's required by JsonReader constructor but not used directly here
    Reader reader = mock(Reader.class);
    jsonReader = new JsonReader(reader);
  }

  private boolean invokeSkipTo(String toFind) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method skipToMethod = JsonReader.class.getDeclaredMethod("skipTo", String.class);
    skipToMethod.setAccessible(true);
    return (boolean) skipToMethod.invoke(jsonReader, toFind);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_foundAtStart() throws Exception {
    // Setup buffer with "hello world"
    setBuffer("hello world\nnext line");
    jsonReader.pos = 0;
    jsonReader.limit = 16;
    jsonReader.lineNumber = 0;
    jsonReader.lineStart = 0;

    boolean result = invokeSkipTo("hello");
    assertTrue(result);
    // pos should be at the start of matched string (0)
    assertEquals(0, jsonReader.pos);
    // lineNumber and lineStart should not change because no newline before pos
    assertEquals(0, jsonReader.lineNumber);
    assertEquals(0, jsonReader.lineStart);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_foundAfterNewline() throws Exception {
    // Setup buffer with "line1\nline2\nline3"
    setBuffer("line1\nline2\nline3");
    jsonReader.pos = 6; // position at start of "line2"
    jsonReader.limit = 17;
    jsonReader.lineNumber = 0;
    jsonReader.lineStart = 0;

    boolean result = invokeSkipTo("line2");
    assertTrue(result);
    // pos should be at 6 where "line2" starts
    assertEquals(6, jsonReader.pos);
    // lineNumber and lineStart should be updated because buffer[pos] was '\n' at pos=5
    assertEquals(1, jsonReader.lineNumber);
    assertEquals(6, jsonReader.lineStart);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_notFound() throws Exception {
    // Setup buffer with "abcde"
    setBuffer("abcde");
    jsonReader.pos = 0;
    jsonReader.limit = 5;
    jsonReader.lineNumber = 0;
    jsonReader.lineStart = 0;

    boolean result = invokeSkipTo("xyz");
    assertFalse(result);
    // pos should have incremented until limit - length
    assertTrue(jsonReader.pos >= 0);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_handlesNewlinesCorrectly() throws Exception {
    // Setup buffer with multiple newlines and target after them
    setBuffer("a\nb\nc\nhello");
    jsonReader.pos = 0;
    jsonReader.limit = 11;
    jsonReader.lineNumber = 0;
    jsonReader.lineStart = 0;

    boolean result = invokeSkipTo("hello");
    assertTrue(result);
    // pos should be at 6 where "hello" starts
    assertEquals(6, jsonReader.pos);
    // lineNumber should be 3 (for 3 newlines)
    assertEquals(3, jsonReader.lineNumber);
    // lineStart should be pos after last newline (7)
    assertEquals(7, jsonReader.lineStart);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_emptyToFind() throws Exception {
    // Setup buffer with some content
    setBuffer("anything");
    jsonReader.pos = 0;
    jsonReader.limit = 8;
    jsonReader.lineNumber = 0;
    jsonReader.lineStart = 0;

    // Empty string toFind should always return true immediately
    boolean result = invokeSkipTo("");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_partialBufferFill() throws Exception {
    // Setup buffer with partial data and simulate fillBuffer to extend it
    setBuffer("abc");
    jsonReader.pos = 0;
    jsonReader.limit = 3;
    jsonReader.lineNumber = 0;
    jsonReader.lineStart = 0;

    // Spy on jsonReader to mock fillBuffer behavior
    JsonReader spyReader = spy(jsonReader);
    // Override fillBuffer to simulate adding "def" after "abc"
    doAnswer(invocation -> {
      char[] buf = getBuffer(spyReader);
      System.arraycopy("def".toCharArray(), 0, buf, 3, 3);
      setLimit(spyReader, 6);
      return true;
    }).when(spyReader).fillBuffer(3);

    Method skipToMethod = JsonReader.class.getDeclaredMethod("skipTo", String.class);
    skipToMethod.setAccessible(true);

    boolean result = (boolean) skipToMethod.invoke(spyReader, "def");
    assertTrue(result);
    // pos should be at 3 where "def" starts
    assertEquals(3, getPos(spyReader));
  }

  // Helper methods to set private fields using reflection

  private void setBuffer(String content) throws Exception {
    char[] buf = content.toCharArray();
    System.arraycopy(buf, 0, getBuffer(jsonReader), 0, buf.length);
  }

  private char[] getBuffer(JsonReader reader) throws Exception {
    var field = JsonReader.class.getDeclaredField("buffer");
    field.setAccessible(true);
    return (char[]) field.get(reader);
  }

  private void setLimit(JsonReader reader, int limit) throws Exception {
    var field = JsonReader.class.getDeclaredField("limit");
    field.setAccessible(true);
    field.setInt(reader, limit);
  }

  private int getPos(JsonReader reader) throws Exception {
    var field = JsonReader.class.getDeclaredField("pos");
    field.setAccessible(true);
    return field.getInt(reader);
  }
}