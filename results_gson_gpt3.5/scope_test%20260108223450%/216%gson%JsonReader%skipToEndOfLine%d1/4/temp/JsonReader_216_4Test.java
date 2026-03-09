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

public class JsonReader_216_4Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object invokeSkipToEndOfLine() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
    method.setAccessible(true);
    return method.invoke(jsonReader);
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posLessThanLimit_withNewLine() throws Exception {
    // Setup buffer with chars: 'a', '\n', 'b'
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = '\n';
    buffer[2] = 'b';

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    invokeSkipToEndOfLine();

    int pos = (int) jsonReader.getClass().getDeclaredField("pos").get(jsonReader);
    int lineNumber = (int) jsonReader.getClass().getDeclaredField("lineNumber").get(jsonReader);
    int lineStart = (int) jsonReader.getClass().getDeclaredField("lineStart").get(jsonReader);

    assertEquals(2, pos, "pos should be after newline");
    assertEquals(1, lineNumber, "lineNumber should increment");
    assertEquals(pos, lineStart, "lineStart should be updated to pos");
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posLessThanLimit_withCarriageReturn() throws Exception {
    // Setup buffer with chars: 'a', '\r', 'b'
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = '\r';
    buffer[2] = 'b';

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    invokeSkipToEndOfLine();

    int pos = (int) jsonReader.getClass().getDeclaredField("pos").get(jsonReader);
    int lineNumber = (int) jsonReader.getClass().getDeclaredField("lineNumber").get(jsonReader);
    int lineStart = (int) jsonReader.getClass().getDeclaredField("lineStart").get(jsonReader);

    assertEquals(2, pos, "pos should be after carriage return");
    assertEquals(0, lineNumber, "lineNumber should not increment");
    assertEquals(0, lineStart, "lineStart should not change");
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posLessThanLimit_noLineBreak() throws Exception {
    // Setup buffer with chars: 'a', 'b', 'c'
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = 'c';

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    invokeSkipToEndOfLine();

    int pos = (int) jsonReader.getClass().getDeclaredField("pos").get(jsonReader);
    int lineNumber = (int) jsonReader.getClass().getDeclaredField("lineNumber").get(jsonReader);
    int lineStart = (int) jsonReader.getClass().getDeclaredField("lineStart").get(jsonReader);

    // pos should be at limit since no line break found
    assertEquals(3, pos, "pos should be at limit");
    assertEquals(0, lineNumber, "lineNumber should not increment");
    assertEquals(0, lineStart, "lineStart should not change");
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posEqualsLimit_fillBufferTrue() throws Exception {
    // Setup buffer with chars: 'a', 'b', '\n' (limit 3)
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = '\n';

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 3);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    // Spy on jsonReader to mock fillBuffer
    JsonReader spyReader = spy(jsonReader);

    // When fillBuffer(1) is called, simulate filling buffer with '\n' at pos 3 and limit 4
    doAnswer(invocation -> {
      char[] buf = (char[]) spyReader.getClass().getDeclaredField("buffer").get(spyReader);
      buf[3] = '\n';
      spyReader.getClass().getDeclaredField("limit").set(spyReader, 4);
      return true;
    }).when(spyReader).fillBuffer(1);

    // Use reflection to invoke skipToEndOfLine on spyReader
    Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
    method.setAccessible(true);
    method.invoke(spyReader);

    int pos = (int) spyReader.getClass().getDeclaredField("pos").get(spyReader);
    int lineNumber = (int) spyReader.getClass().getDeclaredField("lineNumber").get(spyReader);
    int lineStart = (int) spyReader.getClass().getDeclaredField("lineStart").get(spyReader);

    assertEquals(4, pos, "pos should be incremented after newline");
    assertEquals(1, lineNumber, "lineNumber should increment");
    assertEquals(pos, lineStart, "lineStart should be updated to pos");
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posEqualsLimit_fillBufferFalse() throws Exception {
    // Setup buffer with chars: 'a', 'b', 'c' (limit 3)
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = 'c';

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 3);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    // Spy on jsonReader to mock fillBuffer
    JsonReader spyReader = spy(jsonReader);

    // When fillBuffer(1) is called, return false to simulate EOF or no more data
    doReturn(false).when(spyReader).fillBuffer(1);

    // Use reflection to invoke skipToEndOfLine on spyReader
    Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
    method.setAccessible(true);
    method.invoke(spyReader);

    int pos = (int) spyReader.getClass().getDeclaredField("pos").get(spyReader);
    int lineNumber = (int) spyReader.getClass().getDeclaredField("lineNumber").get(spyReader);
    int lineStart = (int) spyReader.getClass().getDeclaredField("lineStart").get(spyReader);

    // pos should remain unchanged because no chars to consume
    assertEquals(3, pos, "pos should remain at limit");
    assertEquals(0, lineNumber, "lineNumber should not increment");
    assertEquals(0, lineStart, "lineStart should not change");
  }
}