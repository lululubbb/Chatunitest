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

public class JsonReader_217_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private boolean invokeSkipTo(String toFind) throws Exception {
    Method skipToMethod = JsonReader.class.getDeclaredMethod("skipTo", String.class);
    skipToMethod.setAccessible(true);
    try {
      return (boolean) skipToMethod.invoke(jsonReader, toFind);
    } catch (InvocationTargetException e) {
      // Unwrap IOException if thrown
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  private boolean invokeFillBuffer(int length) throws Exception {
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    try {
      return (boolean) fillBufferMethod.invoke(jsonReader, length);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  private void mockFillBufferReturnValues(boolean... returns) throws Exception {
    // Replace jsonReader with a spy that overrides fillBuffer via reflection
    JsonReader spyReader = spy(jsonReader);
    // Use doAnswer to call the private fillBuffer method and override its return values
    final boolean[] retVals = returns;
    final int[] callCount = {0};

    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);

    doAnswer(invocation -> {
      if (callCount[0] < retVals.length) {
        return retVals[callCount[0]++];
      }
      // fallback to actual method if calls exceed retVals length
      return fillBufferMethod.invoke(spyReader, invocation.getArgument(0));
    }).when(spyReader).fillBuffer(anyInt());

    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  public void skipTo_foundAtStart() throws Exception {
    // Setup buffer with "hello" starting at pos 0, limit at least 5
    String toFind = "hello";
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    System.arraycopy(toFind.toCharArray(), 0, buf, 0, toFind.length());

    setInternalState(buf, 0, toFind.length(), 0, 0);

    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
    // pos should be at 0 (start of found string)
    assertEquals(0, getInternalPos());
  }

  @Test
    @Timeout(8000)
  public void skipTo_foundAfterNewLine() throws Exception {
    String toFind = "world";
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    // Fill buffer with newline at pos=0 and "world" at pos=1
    buf[0] = '\n';
    System.arraycopy(toFind.toCharArray(), 0, buf, 1, toFind.length());

    setInternalState(buf, 0, toFind.length() + 1, 0, 0);

    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
    // lineNumber incremented by 1 because of newline at pos=0
    assertEquals(1, getInternalLineNumber());
    // lineStart updated to pos+1 after newline (pos=0, so lineStart=1)
    assertEquals(1, getInternalLineStart());
  }

  @Test
    @Timeout(8000)
  public void skipTo_notFoundWithinBufferButFillBufferTrue() throws Exception {
    String toFind = "abcde";
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    // Fill buffer with "abzzz"
    buf[0] = 'a';
    buf[1] = 'b';
    buf[2] = 'z';
    buf[3] = 'z';
    buf[4] = 'z';

    setInternalState(buf, 0, 5, 0, 0);

    // Mock fillBuffer to return true once then false
    mockFillBufferReturnValues(true, false);

    boolean result = invokeSkipTo(toFind);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void skipTo_emptyToFind() throws Exception {
    // Empty string toFind should always return true immediately
    String toFind = "";
    setInternalState(new char[JsonReader.BUFFER_SIZE], 0, 0, 0, 0);

    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void skipTo_bufferEndsBeforeToFindAndFillBufferFalse() throws Exception {
    String toFind = "abcdef";
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    // Buffer contains only 'a', 'b', 'c'
    buf[0] = 'a';
    buf[1] = 'b';
    buf[2] = 'c';

    setInternalState(buf, 0, 3, 0, 0);

    // Mock fillBuffer to return false (cannot fill more)
    mockFillBufferReturnValues(false);

    boolean result = invokeSkipTo(toFind);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void skipTo_withMultipleLinesAndMatch() throws Exception {
    String toFind = "test";
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    // buffer: "\n\nte\nst" at pos 0, limit 7
    buf[0] = '\n';
    buf[1] = '\n';
    buf[2] = 't';
    buf[3] = 'e';
    buf[4] = '\n';
    buf[5] = 's';
    buf[6] = 't';

    setInternalState(buf, 0, 7, 0, 0);

    boolean result = invokeSkipTo(toFind);

    assertTrue(result);
    // lineNumber should be incremented for each '\n' before match
    assertEquals(3, getInternalLineNumber());
    // lineStart should be updated to position after last newline before match
    assertEquals(5, getInternalLineStart());
  }

  // Helper methods to set and get private fields using reflection

  private void setInternalState(char[] buffer, int pos, int limit, int lineNumber, int lineStart) throws Exception {
    setField("buffer", buffer);
    setField("pos", pos);
    setField("limit", limit);
    setField("lineNumber", lineNumber);
    setField("lineStart", lineStart);
  }

  private int getInternalPos() throws Exception {
    return (int) getField("pos");
  }

  private int getInternalLineNumber() throws Exception {
    return (int) getField("lineNumber");
  }

  private int getInternalLineStart() throws Exception {
    return (int) getField("lineStart");
  }

  private void setField(String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  private Object getField(String fieldName) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(jsonReader);
  }
}