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

public class JsonReader_217_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // We use a dummy Reader since we won't rely on it for skipTo tests
    Reader dummyReader = mock(Reader.class);
    jsonReader = new JsonReader(dummyReader);
  }

  private boolean invokeSkipTo(String toFind) throws Throwable {
    Method skipToMethod = JsonReader.class.getDeclaredMethod("skipTo", String.class);
    skipToMethod.setAccessible(true);
    try {
      return (boolean) skipToMethod.invoke(jsonReader, toFind);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_FoundAtStart() throws Throwable {
    String toFind = "test";
    char[] buffer = "test123".toCharArray();
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
    assertEquals(0, getIntField("pos")); // pos should not have moved after finding
    assertEquals(0, getIntField("lineNumber"));
    assertEquals(0, getIntField("lineStart"));
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_FoundAfterNewLine() throws Throwable {
    String toFind = "abc";
    // buffer contains a newline at pos=1, toFind starts at pos=2
    char[] buffer = {'x', '\n', 'a', 'b', 'c', 'y'};
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 1);
    setField("lineStart", 0);

    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
    assertEquals(2, getIntField("pos")); // pos where toFind starts
    assertEquals(2, getIntField("lineNumber")); // lineNumber incremented once
    assertEquals(2, getIntField("lineStart")); // lineStart updated after newline
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_NotFound() throws Throwable {
    String toFind = "xyz";
    char[] buffer = "abcdefgh".toCharArray();
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    boolean result = invokeSkipTo(toFind);

    assertFalse(result);
    assertEquals(buffer.length, getIntField("pos")); // pos advanced to limit
    assertEquals(0, getIntField("lineNumber"));
    assertEquals(0, getIntField("lineStart"));
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_FoundMultipleLines() throws Throwable {
    String toFind = "end";
    // buffer with multiple newlines before "end"
    char[] buffer = {'a', '\n', 'b', '\n', 'e', 'n', 'd', 'x'};
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
    assertEquals(4, getIntField("pos")); // pos at start of "end"
    assertEquals(2, getIntField("lineNumber")); // two newlines before
    assertEquals(4, getIntField("lineStart")); // updated after last newline
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_EmptyStringToFind() throws Throwable {
    String toFind = "";
    char[] buffer = "anything".toCharArray();
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    // According to code, length=0, so loop will check pos + 0 <= limit or fillBuffer(0)
    // The inner loop for c=0 to length=0 won't run, so it immediately returns true at pos=0
    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
    assertEquals(0, getIntField("pos"));
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_NewlineUpdatesLineNumberAndLineStart() throws Throwable {
    String toFind = "z";
    char[] buffer = {'a', '\n', 'b', 'z'};
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 5);
    setField("lineStart", 3);

    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
    assertEquals(3, getIntField("pos")); // pos where 'z' is found
    assertEquals(6, getIntField("lineNumber")); // incremented once for newline
    assertEquals(2, getIntField("lineStart")); // set to pos+1 at newline (pos=1+1=2)
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_FillBufferReturnsFalse() throws Throwable {
    // To test fillBuffer returning false, we subclass JsonReader and override fillBuffer
    JsonReader readerWithFillBuffer = new JsonReader(mock(Reader.class)) {
      @Override
      protected boolean fillBuffer(int minimum) {
        return false;
      }
    };
    // set fields via reflection
    setField(readerWithFillBuffer, "buffer", "abc".toCharArray());
    setField(readerWithFillBuffer, "pos", 0);
    setField(readerWithFillBuffer, "limit", 3);
    setField(readerWithFillBuffer, "lineNumber", 0);
    setField(readerWithFillBuffer, "lineStart", 0);

    Method skipToMethod = JsonReader.class.getDeclaredMethod("skipTo", String.class);
    skipToMethod.setAccessible(true);

    // toFind string longer than buffer to force fillBuffer call
    String toFind = "abcd";

    try {
      skipToMethod.invoke(readerWithFillBuffer, toFind);
      fail("Expected IllegalAccessException or exception due to fillBuffer override");
    } catch (InvocationTargetException e) {
      // We expect no exception, but the method returns false if fillBuffer returns false and no match
      Throwable cause = e.getCause();
      if (cause != null && cause instanceof IOException) {
        fail("Unexpected IOException");
      }
    }
  }

  private void setField(String fieldName, Object value) {
    setField(jsonReader, fieldName, value);
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getIntField(String fieldName) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.getInt(jsonReader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}