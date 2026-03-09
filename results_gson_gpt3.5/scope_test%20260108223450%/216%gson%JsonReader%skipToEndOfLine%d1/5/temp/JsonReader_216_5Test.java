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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_216_5Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testSkipToEndOfLine_withNewLineInBuffer() throws Exception {
    // Setup buffer with chars including '\n' at pos 2
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = '\n';
    buffer[3] = 'c';
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 4);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    invokeSkipToEndOfLine();

    int pos = getField(jsonReader, "pos");
    int lineNumber = getField(jsonReader, "lineNumber");
    int lineStart = getField(jsonReader, "lineStart");

    assertEquals(3, pos);
    assertEquals(1, lineNumber);
    assertEquals(3, lineStart);
  }

  @Test
    @Timeout(8000)
  public void testSkipToEndOfLine_withCarriageReturnInBuffer() throws Exception {
    // Setup buffer with chars including '\r' at pos 1
    char[] buffer = new char[1024];
    buffer[0] = 'x';
    buffer[1] = '\r';
    buffer[2] = 'y';
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 5);
    setField(jsonReader, "lineStart", 3);

    invokeSkipToEndOfLine();

    int pos = getField(jsonReader, "pos");
    int lineNumber = getField(jsonReader, "lineNumber");
    int lineStart = getField(jsonReader, "lineStart");

    assertEquals(2, pos);
    assertEquals(5, lineNumber);
    assertEquals(3, lineStart);
  }

  @Test
    @Timeout(8000)
  public void testSkipToEndOfLine_withBufferEmptyAndFillBufferReturnsFalse() throws Exception {
    // Setup pos == limit, fillBuffer(1) returns false, so loop exits immediately
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);

    // Use Mockito spy to override fillBuffer(int) to return false
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);
    jsonReader = spyReader;

    invokeSkipToEndOfLine();

    int pos = getField(jsonReader, "pos");
    assertEquals(0, pos);
  }

  @Test
    @Timeout(8000)
  public void testSkipToEndOfLine_withFillBufferReturnsTrue() throws Exception {
    // Setup pos == limit initially, fillBuffer(1) returns true once then false
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);

    char[] buffer = new char[1024];
    buffer[0] = '\n';

    // Use Mockito spy to override fillBuffer(int) to set buffer and limit once, then return false
    JsonReader spyReader = spy(jsonReader);
    doAnswer(invocation -> {
      setField(spyReader, "buffer", buffer);
      setField(spyReader, "limit", 1);
      return true;
    }).doReturn(false).when(spyReader).fillBuffer(1);

    jsonReader = spyReader;

    invokeSkipToEndOfLine();

    int pos = getField(jsonReader, "pos");
    int lineNumber = getField(jsonReader, "lineNumber");
    int lineStart = getField(jsonReader, "lineStart");

    assertEquals(1, pos);
    assertEquals(1, lineNumber);
    assertEquals(1, lineStart);
  }

  private void invokeSkipToEndOfLine() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
    method.setAccessible(true);
    method.invoke(jsonReader);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    T value = (T) field.get(target);
    return value;
  }
}