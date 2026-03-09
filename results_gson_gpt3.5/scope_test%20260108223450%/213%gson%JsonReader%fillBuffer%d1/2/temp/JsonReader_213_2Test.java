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

class JsonReaderFillBufferTest {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private boolean invokeFillBuffer(int minimum) throws Throwable {
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    try {
      return (boolean) fillBufferMethod.invoke(jsonReader, minimum);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  void testFillBuffer_NoData_ReadReturnsMinusOne() throws Throwable {
    when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

    boolean result = invokeFillBuffer(1);

    assertFalse(result);
    assertEquals(0, (int) getField("limit"));
    assertEquals(0, (int) getField("pos"));
  }

  @Test
    @Timeout(8000)
  void testFillBuffer_PartialBuffer_ShiftsData() throws Throwable {
    // Setup internal buffer with some data and pos/limit set to simulate partial consumption
    char[] buffer = getField("buffer");
    buffer[0] = 'a';
    buffer[1] = 'b';
    setField("pos", 1);
    setField("limit", 2);
    setField("lineStart", 5);

    // Reader returns 1 char 'c'
    when(mockReader.read(any(char[].class), eq(1), anyInt())).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      int off = invocation.getArgument(1);
      buf[off] = 'c';
      return 1;
    });

    boolean result = invokeFillBuffer(2);

    assertTrue(result);
    assertEquals(0, (int) getField("pos"));
    assertEquals(2, (int) getField("limit"));
    // buffer should now be ['b', 'c', ...]
    assertEquals('b', buffer[0]);
    assertEquals('c', buffer[1]);
  }

  @Test
    @Timeout(8000)
  void testFillBuffer_ConsumesBomOnFirstRead() throws Throwable {
    // Prepare buffer with BOM char at start and simulate first read
    char[] buffer = getField("buffer");
    buffer[0] = '\ufeff';

    setField("pos", 0);
    setField("limit", 0);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    // Reader returns 1 char (the BOM)
    when(mockReader.read(any(char[].class), eq(0), anyInt())).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      int off = invocation.getArgument(1);
      buf[off] = '\ufeff';
      return 1;
    }).thenReturn(-1);

    // minimum is 1, after BOM increment minimum becomes 2, so fillBuffer returns false
    boolean result = invokeFillBuffer(1);

    assertFalse(result);
    assertEquals(1, (int) getField("pos"));
    assertEquals(1, (int) getField("lineStart"));
  }

  @Test
    @Timeout(8000)
  void testFillBuffer_SuccessfulReadWithoutBom() throws Throwable {
    char[] buffer = getField("buffer");

    setField("pos", 0);
    setField("limit", 0);
    setField("lineNumber", 1);
    setField("lineStart", 0);

    when(mockReader.read(any(char[].class), eq(0), anyInt())).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      int off = invocation.getArgument(1);
      buf[off] = 'x';
      return 1;
    });

    boolean result = invokeFillBuffer(1);

    assertTrue(result);
    assertEquals(0, (int) getField("pos"));
    assertEquals(1, (int) getField("limit"));
    assertEquals('x', buffer[0]);
  }

  private <T> T getField(String fieldName) throws Throwable {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    T value = (T) field.get(jsonReader);
    return value;
  }

  private void setField(String fieldName, Object value) throws Throwable {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }
}