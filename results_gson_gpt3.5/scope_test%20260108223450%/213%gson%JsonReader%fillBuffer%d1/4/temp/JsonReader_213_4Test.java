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
import org.mockito.Mockito;

class JsonReaderFillBufferTest {

  private JsonReader jsonReader;
  private Reader mockReader;

  private Method fillBufferMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void fillBuffer_returnsTrue_whenBufferAlreadyHasEnoughData() throws Throwable {
    // Setup initial state: limit != pos, pos = 1, limit = 3
    setField(jsonReader, "pos", 1);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineStart", 5);
    setField(jsonReader, "lineNumber", 1);

    char[] buffer = new char[1024];
    buffer[1] = 'a';
    buffer[2] = 'b';
    setField(jsonReader, "buffer", buffer);

    // Mock Reader.read to return -1 (EOF) immediately
    when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

    // minimum is 2, limit - pos = 2, so after shift buffer length is 2 which is >= minimum
    boolean result = (boolean) invokeFillBuffer(2);

    assertTrue(result);

    // pos reset to 0
    assertEquals(0, getField(jsonReader, "pos"));
    // limit adjusted after copy
    assertEquals(2, getField(jsonReader, "limit"));

    // buffer content shifted left by pos (1)
    char[] bufAfter = (char[]) getField(jsonReader, "buffer");
    assertEquals('a', bufAfter[0]);
    assertEquals('b', bufAfter[1]);

    verify(mockReader, never()).read(any(char[].class), anyInt(), anyInt());
  }

  @Test
    @Timeout(8000)
  void fillBuffer_readsMoreData_untilMinimumReached() throws Throwable {
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lineStart", 0);
    setField(jsonReader, "lineNumber", 1);

    char[] buffer = new char[1024];
    setField(jsonReader, "buffer", buffer);

    // Simulate two reads: first returns 1 char, second returns 2 chars
    when(mockReader.read(any(char[].class), eq(0), eq(1024))).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      int off = invocation.getArgument(1);
      buf[off] = 'x';
      return 1;
    });
    when(mockReader.read(any(char[].class), eq(1), eq(1023))).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      int off = invocation.getArgument(1);
      buf[off] = 'y';
      buf[off + 1] = 'z';
      return 2;
    });
    when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

    boolean result = (boolean) invokeFillBuffer(3);

    assertTrue(result);

    assertEquals(0, getField(jsonReader, "pos"));
    assertEquals(3, getField(jsonReader, "limit"));

    char[] bufAfter = (char[]) getField(jsonReader, "buffer");
    assertEquals('x', bufAfter[0]);
    assertEquals('y', bufAfter[1]);
    assertEquals('z', bufAfter[2]);

    verify(mockReader, times(2)).read(any(char[].class), anyInt(), anyInt());
  }

  @Test
    @Timeout(8000)
  void fillBuffer_consumesBOM_ifPresentAtStart() throws Throwable {
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lineStart", 0);
    setField(jsonReader, "lineNumber", 0);

    char[] buffer = new char[1024];
    setField(jsonReader, "buffer", buffer);

    // First read returns BOM char and 2 more chars
    when(mockReader.read(any(char[].class), eq(0), eq(1024))).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      int off = invocation.getArgument(1);
      buf[off] = '\ufeff';
      buf[off + 1] = 'a';
      buf[off + 2] = 'b';
      return 3;
    });
    when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

    boolean result = (boolean) invokeFillBuffer(3);

    assertTrue(result);

    assertEquals(1, getField(jsonReader, "pos"));
    assertEquals(1, getField(jsonReader, "lineStart"));

    int limit = (int) getField(jsonReader, "limit");
    // limit increased by 3 (read chars) but pos incremented by 1 (BOM consumed)
    assertEquals(3, limit);

    char[] bufAfter = (char[]) getField(jsonReader, "buffer");
    assertEquals('\ufeff', bufAfter[0]);
    assertEquals('a', bufAfter[1]);
    assertEquals('b', bufAfter[2]);

    verify(mockReader, times(1)).read(any(char[].class), anyInt(), anyInt());
  }

  @Test
    @Timeout(8000)
  void fillBuffer_returnsFalse_whenEOFBeforeMinimum() throws Throwable {
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lineStart", 0);
    setField(jsonReader, "lineNumber", 1);

    char[] buffer = new char[1024];
    setField(jsonReader, "buffer", buffer);

    // read returns -1 immediately (EOF)
    when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

    boolean result = (boolean) invokeFillBuffer(1);

    assertFalse(result);

    assertEquals(0, getField(jsonReader, "pos"));
    assertEquals(0, getField(jsonReader, "limit"));

    verify(mockReader, times(1)).read(any(char[].class), anyInt(), anyInt());
  }

  // Helper to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get private fields via reflection
  private Object getField(Object target, String fieldName) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to invoke private fillBuffer method and unwrap exceptions
  private Object invokeFillBuffer(int minimum) throws Throwable {
    try {
      return fillBufferMethod.invoke(jsonReader, minimum);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}