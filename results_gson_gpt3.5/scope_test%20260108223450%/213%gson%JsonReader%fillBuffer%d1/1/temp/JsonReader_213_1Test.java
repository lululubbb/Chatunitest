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

class JsonReaderFillBufferTest {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private boolean invokeFillBuffer(int minimum) throws Exception {
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    try {
      return (boolean) fillBufferMethod.invoke(jsonReader, minimum);
    } catch (java.lang.reflect.InvocationTargetException e) {
      // unwrap the underlying IOException if present
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  @Test
    @Timeout(8000)
  void fillBuffer_whenLimitNotEqualsPos_shouldShiftBufferAndReadSufficientChars() throws Exception {
    // Arrange
    char[] buffer = getBuffer(jsonReader);
    int pos = 2;
    int limit = 5;
    setField(jsonReader, "pos", pos);
    setField(jsonReader, "limit", limit);
    System.arraycopy("abcde".toCharArray(), 0, buffer, 0, 5);

    when(mockReader.read(buffer, limit - pos, buffer.length - (limit - pos))).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      int off = invocation.getArgument(1);
      buf[off] = 'x';
      buf[off + 1] = 'y';
      buf[off + 2] = 'z';
      return 3;
    });

    setField(jsonReader, "lineNumber", 1); // fix here: lineNumber should be 1 to prevent BOM consumption
    setField(jsonReader, "lineStart", 0); 

    // Act
    boolean result = invokeFillBuffer(5);

    // Assert
    assertTrue(result);
    assertEquals(0, getField(jsonReader, "pos"));
    assertEquals(6, getField(jsonReader, "limit")); // 5 - 2 + 3 = 6
    assertArrayEquals(new char[] {'c', 'd', 'e', 'x', 'y', 'z'}, 
      java.util.Arrays.copyOfRange(buffer, 0, 6));
    assertEquals(0, getField(jsonReader, "lineStart")); // lineStart remains 0
  }

  @Test
    @Timeout(8000)
  void fillBuffer_whenLimitEqualsPos_shouldResetLimitAndRead() throws Exception {
    // Arrange
    char[] buffer = getBuffer(jsonReader);
    int pos = 3;
    int limit = 3;
    setField(jsonReader, "pos", pos);
    setField(jsonReader, "limit", limit);

    when(mockReader.read(buffer, 0, buffer.length)).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      buf[0] = 'a';
      buf[1] = 'b';
      return 2;
    });

    setField(jsonReader, "lineNumber", 1); // fix here: lineNumber should be 1 to prevent BOM consumption
    setField(jsonReader, "lineStart", 0); 

    // Act
    boolean result = invokeFillBuffer(2);

    // Assert
    assertTrue(result);
    assertEquals(0, getField(jsonReader, "pos"));
    assertEquals(2, getField(jsonReader, "limit"));
    assertArrayEquals(new char[] {'a', 'b'}, java.util.Arrays.copyOfRange(buffer, 0, 2));
    assertEquals(0, getField(jsonReader, "lineStart")); // lineStart remains 0
  }

  @Test
    @Timeout(8000)
  void fillBuffer_shouldConsumeBOMOnFirstRead() throws Exception {
    // Arrange
    char[] buffer = getBuffer(jsonReader);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    when(mockReader.read(buffer, 0, buffer.length)).thenAnswer(invocation -> {
      char[] buf = invocation.getArgument(0);
      buf[0] = '\ufeff'; // BOM char
      buf[1] = 'a';
      buf[2] = 'b';
      return 3;
    });

    // Act
    boolean result = invokeFillBuffer(2);

    // Assert
    assertTrue(result);
    assertEquals(1, getField(jsonReader, "pos"));
    assertEquals(3, getField(jsonReader, "limit"));
    assertEquals(1, getField(jsonReader, "lineStart"));
  }

  @Test
    @Timeout(8000)
  void fillBuffer_whenReadReturnsMinusOne_shouldReturnFalse() throws Exception {
    // Arrange
    char[] buffer = getBuffer(jsonReader);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lineNumber", 1); // fix here: lineNumber should be 1 to prevent BOM consumption
    setField(jsonReader, "lineStart", 0);

    when(mockReader.read(buffer, 0, buffer.length)).thenReturn(-1);

    // Act
    boolean result = invokeFillBuffer(1);

    // Assert
    assertFalse(result);
    assertEquals(0, getField(jsonReader, "pos"));
    assertEquals(0, getField(jsonReader, "limit"));
  }

  // Utility methods for reflection access

  private static char[] getBuffer(JsonReader jsonReader) throws Exception {
    return (char[]) getField(jsonReader, "buffer");
  }

  private static Object getField(Object target, String fieldName) throws Exception {
    Field field = getFieldRecursive(target.getClass(), fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = getFieldRecursive(target.getClass(), fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static Field getFieldRecursive(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    while (clazz != null) {
      try {
        return clazz.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}